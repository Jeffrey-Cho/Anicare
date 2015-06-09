package sep.software.anicare.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import de.greenrobot.event.EventBus;
import sep.software.anicare.AniCareException;
import sep.software.anicare.R;
import sep.software.anicare.activity.MainActivity;
import sep.software.anicare.interfaces.EntityCallback;
import sep.software.anicare.model.AniCarePet;
import sep.software.anicare.model.AniCareUser;
import sep.software.anicare.service.AniCareAsyncTask;
import sep.software.anicare.util.AniCareLogger;
import sep.software.anicare.util.AsyncChainer;
import sep.software.anicare.util.FileUtil;
import sep.software.anicare.util.ImageUtil;

/**
 * Created by Jeffrey on 2015. 6. 6..
 */
public class PetEditFragment extends AniCareFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = PetEditFragment.class.getSimpleName();
    public static final int GALLERY = 10;
    public static final int CAMERA = 11;

    private ImageView petImage;
    private Uri mProfileImageUri;
    private TextView petName;
    private Spinner petCategory;
    private RadioGroup petSize;
    private RadioGroup petSex;
    private RadioGroup petPersonality;
    private RadioGroup petNeutralized;
    private RadioGroup petFeed;
    private Button submitBtn;
    private Button cancleBtn;

    private AniCarePet.Category Category;
    private String profileImageUrl;

    private Bitmap petImageBitmap;
    private ImageAsyncTask imageTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_pet_edit, container, false);

        petImage = (ImageView) rootView.findViewById(R.id.petProfileImage);
        petName = (TextView) rootView.findViewById(R.id.pet_setting_name);

        petCategory = (Spinner)rootView.findViewById(R.id.pet_setting_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mThisActivity,R.array.pet_category,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        petCategory.setAdapter(adapter);
        petCategory.setOnItemSelectedListener(this);

        petSize = (RadioGroup) rootView.findViewById(R.id.pet_size);
        petSex = (RadioGroup) rootView.findViewById(R.id.pet_sexuality);
        petPersonality = (RadioGroup) rootView.findViewById(R.id.pet_personality);
        petNeutralized = (RadioGroup) rootView.findViewById(R.id.pet_neutralized);
        petFeed = (RadioGroup) rootView.findViewById(R.id.pet_feed);

        submitBtn = (Button) rootView.findViewById(R.id.pet_setting_submit);
        submitBtn.setOnClickListener(this);

        cancleBtn = (Button) rootView.findViewById(R.id.pet_setting_cancle);
        cancleBtn.setOnClickListener(this);

        setImageButton();

        try {
            enableEdit();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {


        if (v.equals(submitBtn)) {


            if (checkContent()) {
                mAppContext.showProgressDialog(mThisActivity);
                final AniCarePet pet = new AniCarePet();
                AniCareLogger.log(mThisUser, pet);
                pet.setName(petName.getText().toString());
                pet.setCategory(Category);
                pet.setUserId(mThisUser.getId());
                pet.setUserName(mThisUser.getName());
                pet.setLocation(mThisUser.getLocation());
                pet.setHouseType(mThisUser.getHouseType());
                pet.setLatitude(mThisUser.getLatitude());
                pet.setLongitude(mThisUser.getLongitude());
                pet.setPersonality(checkPersonality());
                pet.setSize(checkSize());

                pet.setMale(petSex.getCheckedRadioButtonId() == R.id.pet_male);
                pet.setNeutralized(petNeutralized.getCheckedRadioButtonId() == R.id.pet_neutralized_yes);
                pet.setPetFood(petFeed.getCheckedRadioButtonId() == R.id.pet_feed_yes);

                Log.d(TAG, "pet id: " + mThisPet.getId());
                Log.d(TAG, "pet image url: "+mThisPet.getImageURL());
                Log.d(TAG, "pet image url from service: " + mAniCareService.getPetImageUrl(mThisPet.getId()));
                Picasso.with(mAppContext).invalidate(mAniCareService.getPetImageUrl(pet.getId()));
                AsyncChainer.asyncChain(mThisActivity, new AsyncChainer.Chainable() {
                    @Override
                    public void doNext(final Object obj, Object... params) {
                        mAniCareService.putPet(pet, new EntityCallback<AniCarePet>() {
                            @Override
                            public void onCompleted(AniCarePet entity) {
                                AsyncChainer.notifyNext(obj, entity.getId());
                            }
                        });

                    }
                }, new AsyncChainer.Chainable() {
                    @Override
                    public void doNext(Object obj, Object... params) {
                        String id = (String) params[0];
                        Log.d(TAG, "uploadPetImage id: " + id);

                        // Please check this routine for modified pet image bitmap to Hongkun
                        mAniCareService.uploadPetImage(id, petImageBitmap, new EntityCallback<String>() {
                            @Override
                            public void onCompleted(String entity) {
                                Log.d(TAG, "uploadPetImage completed: " + entity.toString());
                                Log.d(TAG, "uploadPetImage url: " + mAniCareService.getPetImageUrl(entity));

                                Fragment settingFragment = new SettingFragment();
                                String tag = settingFragment.getClass().getSimpleName();
                                FragmentManager fragmentManager = getFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.content_frame, settingFragment, tag).commit();
                                EventBus.getDefault().post(new Exception());
                                mAppContext.dismissProgressDialog();
                            }
                        });
                    }
                });

            }  else {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mThisActivity);
                builder1.setTitle("Alert");
                builder1.setMessage("Pet Name is mandatory!");
                builder1.setCancelable(true);
                builder1.setNeutralButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        } else if (v.equals(cancleBtn)) {
            Fragment settingFragment = new SettingFragment();
            String tag = settingFragment.getClass().getSimpleName();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, settingFragment, tag).commit();
            mAppContext.dismissProgressDialog();

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getSelectedItemPosition()) {
            case 0:
                Category = AniCarePet.Category.DOG;
                break;
            case 1:
                Category = AniCarePet.Category.CAT;
                break;
            case 2:
                Category = AniCarePet.Category.BIRD;
                break;
            case 3:
                Category = AniCarePet.Category.ETC;
                break;
        }
        String s = Category.toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Category = AniCarePet.Category.DOG;
    }

    private void enableEdit() throws IOException {

        petName.setText(mThisPet.getName());

        switch(mThisPet.getRawCategory()) {
            case 0:
                petCategory.setSelection(0);
                break;
            case 1:
                petCategory.setSelection(1);
                break;
            case 2:
                petCategory.setSelection(2);
                break;
            case 3:
                petCategory.setSelection(3);
                break;
        }

        switch(mThisPet.getRawSize()) {
            case 3:
                petSize.check(R.id.pet_size_large);
                break;
            case 2:
                petSize.check(R.id.pet_size_medium);
                break;
            case 1:
                petSize.check(R.id.pet_size_small);
                break;
        }

        switch(mThisPet.getRawPersonality()) {
            case 1:
                petPersonality.check(R.id.pet_bright);
                break;
            case 2:
                petPersonality.check(R.id.pet_normal);
                break;
            case 3:
                petPersonality.check(R.id.pet_shy);
                break;
        }


        if(mThisPet.isMale()) {
            petSex.check(R.id.pet_male);
        } else {
            petSex.check(R.id.pet_female);
        }

        if(mThisPet.isNeutralized()) {
            petNeutralized.check(R.id.pet_neutralized_yes);
        } else {
            petNeutralized.check(R.id.pet_neutralized_no);
        }

        if(mThisPet.isPetFood()) {
            petFeed.check(R.id.pet_feed_yes);
        } else {
            petFeed.check(R.id.pet_feed_no);
        }

        profileImageUrl = mAniCareService.getPetImageUrl(mThisPet.getId());

        Picasso.with(mThisActivity).invalidate(profileImageUrl);
        mThisPet.setImageURL(profileImageUrl);
        mAniCareService.setPetImageInto(mThisPet, petImage);

        imageTask = new ImageAsyncTask();
        imageTask.execute(profileImageUrl);

    }

    private boolean checkContent() {
        if (petName.getText().toString().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private AniCarePet.Personality checkPersonality() {
        AniCarePet.Personality personality;

        switch(petPersonality.getCheckedRadioButtonId()) {
            case R.id.pet_bright:
                personality = AniCarePet.Personality.BRIGHT;
                break;
            case R.id.pet_shy:
                personality = AniCarePet.Personality.SHY;
                break;
            case R.id.pet_normal:
                personality = AniCarePet.Personality.NORMAL;
                break;
            default:
                personality = AniCarePet.Personality.NORMAL;
                break;
        }

        return personality;

    }

    private AniCarePet.Size checkSize() {
        AniCarePet.Size size;

        switch(petSize.getCheckedRadioButtonId()) {
            case R.id.pet_size_large:
                size = AniCarePet.Size.BIG;
                break;
            case R.id.pet_size_medium:
                size = AniCarePet.Size.MIDDLE;
                break;
            case R.id.pet_size_small:
                size = AniCarePet.Size.SMALL;
                break;
            default:
                size = AniCarePet.Size.MIDDLE;
                break;
        }

        return size;
    }

    private void setImageButton(){
        petImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FileUtil.getMediaFromGallery(mThisActivity);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_picture)), GALLERY);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            String imagePath = null;

            switch(requestCode){
                case FileUtil.GALLERY:
                    mProfileImageUri = data.getData();
                    imagePath = FileUtil.getMediaPathFromGalleryUri(mThisActivity, mProfileImageUri);
                    break;
                case FileUtil.CAMERA:
                    mProfileImageUri = FileUtil.getMediaUriFromCamera(mThisActivity, data, mProfileImageUri);
                    imagePath = mProfileImageUri.getPath();
                    break;
            }

            updateProfileImage(imagePath);
        }
    }

    private void updateProfileImage(String imagePath){
//        mAppContext.showProgressDialog(mThisActivity);
        petImageBitmap = ImageUtil.refineSquareImage(imagePath, ImageUtil.PROFILE_IMAGE_SIZE);
//        Bitmap profileThumbnailImageBitmap = ImageUtil.refineSquareImage(imagePath, ImageUtil.PROFILE_THUMBNAIL_IMAGE_SIZE);
//        updateProfileImage(profileImageBitmap, profileThumbnailImageBitmap);
        petImage.setImageBitmap(petImageBitmap);
    }

    public class ImageAsyncTask extends AniCareAsyncTask<String, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                return Picasso.with(mThisActivity).load(profileImageUrl).get();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.NETWORK_UNAVAILABLE));
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            petImageBitmap = ImageUtil.refineSquareImage(bitmap, ImageUtil.PROFILE_IMAGE_SIZE);
            petImage.setImageBitmap(petImageBitmap);
        }
    }
}
