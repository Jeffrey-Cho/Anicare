package sep.software.anicare.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import de.greenrobot.event.EventBus;
import sep.software.anicare.R;
import sep.software.anicare.interfaces.EntityCallback;
import sep.software.anicare.AniCareException;
import sep.software.anicare.model.AniCarePet;
import sep.software.anicare.model.AniCareUser;
import sep.software.anicare.service.AniCareAsyncTask;
import sep.software.anicare.util.AniCareLogger;
import sep.software.anicare.util.AsyncChainer;
import sep.software.anicare.util.FileUtil;
import sep.software.anicare.util.ImageUtil;
import sep.software.anicare.view.DynamicHeightImageView;

public class UserSettingActivity extends AniCareActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private static final String TAG = UserSettingActivity.class.getSimpleName();

    private String profileImageUrl;
    private Uri mProfileImageUri;
    private Bitmap defaultPetImageBitmap;


    private ImageAsyncTask imageTask;

    private ImageView userImage;
    private TextView userName;
    private TextView userLocation;
    private Spinner userLivingType;
    private RadioGroup havePet;
    private TextView selfIntro;
    private TextView phonenumber;
    private Button submitBtn;

    private double longitude;
    private double latitude;
    private String location;
    private String address1;
    private String address2;
    private String address3;

    private AniCareUser.HouseType livingType;
    private int defaultPoint = 1000;
    private String defaultPetImageUrl = "http://portalvhdsj2ksq9qld7v06.blob.core.windows.net/anicare-profile/question.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        getActionBar().hide();

        userImage = (ImageView) findViewById(R.id.profileImage);
        userImage.setScaleType(ImageView.ScaleType.FIT_XY);
        userName = (TextView) findViewById(R.id.user_setting_name);
        userLocation = (TextView) findViewById(R.id.user_setting_location);

        userLivingType = (Spinner) findViewById(R.id.user_setting_living_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.user_living_type,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userLivingType.setAdapter(adapter);
        userLivingType.setOnItemSelectedListener(this);

        phonenumber = (TextView) findViewById(R.id.user_setting_phone_number);
        phonenumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        havePet = (RadioGroup) findViewById(R.id.is_pet);
        selfIntro = (TextView) findViewById(R.id.user_setting_self_intro);
        submitBtn = (Button) findViewById(R.id.user_setting_submit);
        submitBtn.setOnClickListener(this);

        userName.setText(mThisUser.getName());
        profileImageUrl = mAniCareService.getUserImageUrl(mThisUser.getId());

        setImageButton();

        Picasso.with(mThisActivity).invalidate(mAniCareService.getUserImageUrl(mThisUser.getId()));
        mAniCareService.setUserImageInto(mThisUser.getId(), userImage);

        userLocation.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getActionMasked()) {
                    case MotionEvent.ACTION_UP:
                        Intent intent = new Intent();
                        intent.setClass(mThisActivity, MapActivity.class);
                        startActivityForResult(intent, MapActivity.MAP_REQUEST);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

    }

    private boolean checkContent() {
        if (userName.getText().toString().isEmpty()) {
            return false;
        }
        if (userLocation.getText().toString().isEmpty()) {
            return false;
        }

        if (phonenumber.getText().toString().isEmpty()) {
            return false;
        }


        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getSelectedItemPosition()) {
            case 0:
                livingType = AniCareUser.HouseType.HOUSE;
            break;
            case 1:
                livingType = AniCareUser.HouseType.APART;
            break;
            case 2:
                livingType = AniCareUser.HouseType.OFFICE_TEL;
            break;
            case 3:
                livingType = AniCareUser.HouseType.Etc;
            break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        livingType = AniCareUser.HouseType.HOUSE;
    }

    @Override
    public void onClick(View v) {

        if (v.equals(submitBtn)) {

            if (checkContent()) {
                mAppContext.showProgressDialog(mThisActivity);
                final AniCareUser user = mAniCareService.getCurrentUser();

                user.setName(userName.getText().toString());
                user.setLocation(userLocation.getText().toString());
                if (selfIntro != null && !selfIntro.getText().toString().isEmpty()) {
                    user.setSelfIntro(selfIntro.getText().toString());
                } else {
                    user.setSelfIntro(null);
                }

                user.setHouseType(livingType);
                user.setHasPet(havePet.getCheckedRadioButtonId() == R.id.user_setting_pet_yes);
                user.setPhoneNumber(phonenumber.getText().toString());

                user.setLongitude(this.longitude);
                user.setLatitude((this.latitude));
                user.setLocation(this.location);
                user.setAddress1(this.address1);
                user.setAddress2(this.address2);
                user.setAddress3(this.address3);

                 if (havePet.getCheckedRadioButtonId() == R.id.user_setting_pet_no) { // when the user has no pet
                     // for default pet
                     imageTask = new ImageAsyncTask();
                     imageTask.execute(defaultPetImageUrl);
                     user.setPoint(defaultPoint);

                     mAniCareService.putUser(user, new EntityCallback<AniCareUser>() {
                        @Override
                        public void onCompleted(AniCareUser entity) {
                            // we need define no pet action
                            setDefaultPet(user);
                        }
                    });
                } else { // when user login with facebook or kakaotalk
                    user.setPoint(defaultPoint); // initial point
                    mAniCareService.putUser(user, new EntityCallback<AniCareUser>() {
                        @Override
                        public void onCompleted(AniCareUser entity) {
                            Intent intent = new Intent();
                            intent.setClass(mThisActivity, PetSettingActivity.class);
                            startActivity(intent);
                            finish();
                            mAppContext.dismissProgressDialog();

                        }
                    });
                }

            } else {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mThisActivity);
                builder1.setTitle("Alert");
                builder1.setMessage("User Name, User Location and Phone Number are mandatory!");
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
//        userImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FileUtil.getMediaFromGallery(mThisActivity);
//            }
//        });

        userImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                longitude = 126.97690095752479;
                latitude = 37.48355207373753;
                location = "서울특별시 종로구 세종로 1-72";
                address1 = "서울특별시";
                address2 = "종로구";
                address3 = "세종로";

                userLocation.setText(location);

                return true;
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
                    updateProfileImage(imagePath);
                    break;
                case FileUtil.CAMERA:
                    mProfileImageUri = FileUtil.getMediaUriFromCamera(mThisActivity, data, mProfileImageUri);
                    imagePath = mProfileImageUri.getPath();
                    updateProfileImage(imagePath);
                    break;
                case MapActivity.MAP_REQUEST:
                    double longitude = data.getDoubleExtra(MapActivity.RESULT_LONGITUDE, 0);
                    double latitude = data.getDoubleExtra(MapActivity.RESULT_LATITUDE, 0);
                    Address address = getLocationName(latitude, longitude);

                    this.location = address.getAddressLine(0).replace("한국 ", "");
                    this.longitude = longitude;
                    this.latitude = latitude;
                    this.address1 = address.getAdminArea();
                    this.address2 = address.getLocality();
                    this.address3 = address.getThoroughfare();

                    userLocation.setText(this.location);

                    break;
            }


        }
    }

    private void setDefaultPet(AniCareUser thisUser) {

        final AniCarePet defaultPet = new AniCarePet();

        defaultPet.setUserId(thisUser.getId());
        defaultPet.setName("");
        defaultPet.setCategory(AniCarePet.Category.ETC);
        defaultPet.setUserName(thisUser.getName());
        defaultPet.setLocation(thisUser.getLocation());
        defaultPet.setHouseType(thisUser.getHouseType());
        defaultPet.setLatitude(thisUser.getLatitude());
        defaultPet.setLongitude(thisUser.getLongitude());
        defaultPet.setPersonality(AniCarePet.Personality.BRIGHT);
        defaultPet.setSize(AniCarePet.Size.MIDDLE);
        defaultPet.setSelfIntro(thisUser.getSelfIntro());

        defaultPet.setMale(true);
        defaultPet.setNeutralized(true);
        defaultPet.setPetFood(true);

        AsyncChainer.asyncChain(mThisActivity, new AsyncChainer.Chainable() {
            @Override
            public void doNext(final Object obj, Object... params) {
                mAniCareService.putPet(defaultPet, new EntityCallback<AniCarePet>() {
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

                mAniCareService.uploadPetImage(id, defaultPetImageBitmap, new EntityCallback<String>() {
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


    }

    private Address getLocationName(double latitude, double longitude) {
        Geocoder gc = new Geocoder(mThisActivity, Locale.KOREA);
        try {
            List<Address> addressList = gc.getFromLocation(latitude, longitude, 1);
            if (addressList != null && addressList.size() == 1) {
                return addressList.get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void updateProfileImage(String imagePath){
        mAppContext.showProgressDialog(mThisActivity);
        Bitmap profileImageBitmap = ImageUtil.refineSquareImage(imagePath, ImageUtil.PROFILE_IMAGE_SIZE);
        Bitmap profileThumbnailImageBitmap = ImageUtil.refineSquareImage(imagePath, ImageUtil.PROFILE_THUMBNAIL_IMAGE_SIZE);
//        updateProfileImage(profileImageBitmap, profileThumbnailImageBitmap);
    }

    public class ImageAsyncTask extends AniCareAsyncTask<String, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                return Picasso.with(mThisActivity).load(defaultPetImageUrl).get();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.NETWORK_UNAVAILABLE));
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            defaultPetImageBitmap = ImageUtil.refineSquareImage(bitmap, ImageUtil.PROFILE_IMAGE_SIZE);
            //userImage.setImageBitmap(defaultPetImageBitmap);
        }
    }

}
