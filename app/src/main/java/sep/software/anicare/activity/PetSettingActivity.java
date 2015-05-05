package sep.software.anicare.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
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
import sep.software.anicare.callback.EntityCallback;
import sep.software.anicare.event.AniCareException;
import sep.software.anicare.model.AniCareUser;
import sep.software.anicare.model.AniCarePet;
import sep.software.anicare.service.AniCareAsyncTask;
import sep.software.anicare.util.ImageUtil;

public class PetSettingActivity extends AniCareActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    private static final String TAG = PetSettingActivity.class.getSimpleName();

    private ImageView petImage;
    private TextView petName;
    private Spinner petCategory;
    private RadioGroup petSize;
    private RadioGroup petSex;
    private RadioGroup petPersonanlity;
    private RadioGroup petNeutralized;
    private RadioGroup petFeed;
    private Button submitBtn;

    private AniCarePet.Category Category;
    private ImageAsyncTask imageTask;
    private String profileImageUrl;


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
        petPersonanlity = (RadioGroup) findViewById(R.id.pet_personality);
        petNeutralized = (RadioGroup) findViewById(R.id.pet_neutralized);
        petFeed = (RadioGroup) findViewById(R.id.pet_feed);

        submitBtn = (Button) findViewById(R.id.pet_setting_submit);
        submitBtn.setOnClickListener(this);

        profileImageUrl = mAniCareService.getPetImageUrl(mThisUser.getId());

        imageTask = new ImageAsyncTask();
        imageTask.execute(profileImageUrl);

//        textView = (TextView) findViewById(R.id.pet_setting_text_view);
//        randSet = (Button) findViewById(R.id.pet_setting_rand_set);
//        gotoMainBtn = (Button) findViewById(R.id.pet_setting_go_back_to_main);
//
//        randSet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AniCarePet pet = AniCarePet.rand();
//                mAniCareService.putPet(pet, new EntityCallback<AniCarePet>() {
//                    @Override
//                    public void onCompleted(AniCarePet entity) {
//                        textView.setText(entity.toString());
//                    }
//                });
//            }
//        });
//
//        gotoMainBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
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

        switch(petPersonanlity.getCheckedRadioButtonId()) {
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

        switch(petPersonanlity.getCheckedRadioButtonId()) {
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
                AniCarePet pet = new AniCarePet();

                pet.setName(petName.getText().toString());
                pet.setCategory(Category);
                //pet.setUserid(mThisUser.getId());

                pet.setPersonality(checkPersonality());
                pet.setSize(checkSize());

                pet.setMale(petSex.getCheckedRadioButtonId() == R.id.pet_male);
                pet.setNeutralized(petNeutralized.getCheckedRadioButtonId() == R.id.pet_neutralized_yes);
                pet.setPetFood(petFeed.getCheckedRadioButtonId() == R.id.pet_feed_yes);


                mAniCareService.putPet(pet, new EntityCallback<AniCarePet>() {
                    @Override
                    public void onCompleted(AniCarePet entity) {
                        Intent intent = new Intent();
                        intent.setClass(mThisActivity, MainActivity.class);
                        startActivity(intent);
                        //finish();
                    }
                });


            } else {
                // alert window show
                Log.d(TAG, "Empty name");
            }
        }
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
