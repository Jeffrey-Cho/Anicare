package sep.software.anicare.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import sep.software.anicare.R;
import sep.software.anicare.interfaces.EntityCallback;
import sep.software.anicare.AniCareException;
import sep.software.anicare.model.AniCarePet;
import sep.software.anicare.service.AniCareAsyncTask;
import sep.software.anicare.util.AniCareLogger;
import sep.software.anicare.util.AsyncChainer;
import sep.software.anicare.util.FileUtil;
import sep.software.anicare.util.ImageUtil;

public class PetSettingActivity extends AniCareActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    private static final String TAG = PetSettingActivity.class.getSimpleName();

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

    private AniCarePet.Category Category;
    private String profileImageUrl;

    private Bitmap petImageBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_setting);

        getActionBar().hide();

        petImage = (ImageView) findViewById(R.id.petProfileImage);
        petName = (TextView) findViewById(R.id.pet_setting_name);

        petCategory = (Spinner)findViewById(R.id.pet_setting_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.pet_category,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        petCategory.setAdapter(adapter);
        petCategory.setOnItemSelectedListener(this);

        petSize = (RadioGroup) findViewById(R.id.pet_size);
        petSex = (RadioGroup) findViewById(R.id.pet_sexuality);
        petPersonality = (RadioGroup) findViewById(R.id.pet_personality);
        petNeutralized = (RadioGroup) findViewById(R.id.pet_neutralized);
        petFeed = (RadioGroup) findViewById(R.id.pet_feed);

        submitBtn = (Button) findViewById(R.id.pet_setting_submit);
        submitBtn.setOnClickListener(this);

        setImageButton();

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

    @Override
    public void onClick(View v) {

        if (v.equals(submitBtn)) {

            if (checkContent()) {
                mAppContext.showProgressDialog(mThisActivity);
                final AniCarePet pet = new AniCarePet();

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

                    if (petImageBitmap == null) {
                        new AlertDialog.Builder(mThisActivity)
                                .setMessage("You should upload your pet image")
                                .setPositiveButton("ok", null)
                                .show();
                        return;
                    }
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

                            mAniCareService.uploadPetImage(id, petImageBitmap, new EntityCallback<String>() {
                                @Override
                                public void onCompleted(String entity) {
                                    mAppContext.dismissProgressDialog();
                                    Intent intent = new Intent();
                                    intent.setClass(mThisActivity, MainActivity.class);
                                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    });



            } else {
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
        }
    }

    private void setImageButton(){
        petImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtil.getMediaFromGallery(mThisActivity);
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
            Bitmap profileImageBitmap = ImageUtil.refineSquareImage(bitmap, ImageUtil.PROFILE_IMAGE_SIZE);
            petImage.setImageBitmap(profileImageBitmap);
        }
    }

}
