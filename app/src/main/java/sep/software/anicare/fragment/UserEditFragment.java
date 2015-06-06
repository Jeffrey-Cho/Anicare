package sep.software.anicare.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import java.util.List;
import java.util.Locale;

import sep.software.anicare.R;
import sep.software.anicare.activity.MapActivity;
import sep.software.anicare.interfaces.EntityCallback;
import sep.software.anicare.model.AniCareUser;
import sep.software.anicare.util.FileUtil;
import sep.software.anicare.util.ImageUtil;

/**
 * Created by Jeffrey on 2015. 6. 6..
 */
public class UserEditFragment extends AniCareFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private static final String TAG = UserEditFragment.class.getSimpleName();

    private String profileImageUrl;
    private Uri mProfileImageUri;

    private ImageView userImage;
    private TextView userName;
    private TextView userLocation;
    private Spinner userLivingType;
    private RadioGroup havePet;
    private TextView selfIntro;
    private Button submitBtn;
    private Button cancleBtn;

    private double longitude;
    private double latitude;
    private String location;
    private String address1;
    private String address2;
    private String address3;

    private AniCareUser.HouseType livingType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_user_edit, container, false);

        userImage = (ImageView) rootView.findViewById(R.id.profileImage);
        userImage.setScaleType(ImageView.ScaleType.FIT_XY);
        userName = (TextView) rootView.findViewById(R.id.user_setting_name);
        userLocation = (TextView) rootView.findViewById(R.id.user_setting_location);

        userLivingType = (Spinner) rootView.findViewById(R.id.user_setting_living_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mThisActivity,R.array.user_living_type,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userLivingType.setAdapter(adapter);
        userLivingType.setOnItemSelectedListener(this);

        havePet = (RadioGroup) rootView.findViewById(R.id.is_pet);
        selfIntro = (TextView) rootView.findViewById(R.id.user_setting_self_intro);
        submitBtn = (Button) rootView.findViewById(R.id.user_setting_submit);
        submitBtn.setOnClickListener(this);

        cancleBtn = (Button) rootView.findViewById(R.id.user_setting_cancle);
        cancleBtn.setOnClickListener(this);

        userName.setText(mThisUser.getName());
        profileImageUrl = mAniCareService.getUserImageUrl(mThisUser.getId());

        setImageButton();

        Picasso.with(mThisActivity).invalidate(mAniCareService.getUserImageUrl(mThisUser.getId()));
        mAniCareService.setUserImageInto(mThisUser.getId(), userImage);

        userLocation.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
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

        enableEdit();

        return rootView;
    }

    private void setImageButton(){
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtil.getMediaFromGallery(mThisActivity);
            }
        });
    }

    @Override
    public void onClick(View v) {


        if (v.equals(submitBtn)) {

            if (checkContent()) {
                mAppContext.showProgressDialog(mThisActivity);
                AniCareUser user = mAniCareService.getCurrentUser();

                user.setName(userName.getText().toString());
                user.setLocation(userLocation.getText().toString());
                if (selfIntro != null && !selfIntro.getText().toString().isEmpty()) {
                    user.setSelfIntro(selfIntro.getText().toString());
                } else {
                    user.setSelfIntro(null);
                }

                user.setHouseType(livingType);
                user.setHasPet(havePet.getCheckedRadioButtonId() == R.id.user_setting_pet_yes);

                user.setLongitude(this.longitude);
                user.setLatitude((this.latitude));
                user.setLocation(this.location);
                user.setAddress1(this.address1);
                user.setAddress2(this.address2);
                user.setAddress3(this.address3);

                if (havePet.getCheckedRadioButtonId() == R.id.user_setting_pet_no) { // when the user has no pet

                    mAniCareService.putUser(user, new EntityCallback<AniCareUser>() {
                        @Override
                        public void onCompleted(AniCareUser entity) {
                            // we need define no pet action
                        }
                    });

                } else {

                    mAniCareService.putUser(user, new EntityCallback<AniCareUser>() {
                        @Override
                        public void onCompleted(AniCareUser entity) {
                            Fragment settingFragment = new SettingFragment();
                            String tag = settingFragment.getClass().getSimpleName();
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.content_frame, settingFragment, tag).commit();
                            mAppContext.dismissProgressDialog();
                        }
                    });
                }

            }  else {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mThisActivity);
                builder1.setTitle("Alert");
                builder1.setMessage("User Name and User Location are mandatory!");
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

    private boolean checkContent() {
        if (userName.getText().toString().isEmpty()) {
            return false;
        }
        if (userLocation.getText().toString().isEmpty()) {
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

    private void enableEdit() {
        userName.setText(mThisUser.getName());
        userLocation.setText(mThisUser.getLocation());

        switch(mThisUser.getRawHouseType()) {
            case 1:
                userLivingType.setSelection(0);
                break;
            case 2:
                userLivingType.setSelection(1);
                break;
            case 3:
                userLivingType.setSelection(2);
                break;
            case 4:
                userLivingType.setSelection(3);
                break;
        }

        selfIntro.setText(mThisUser.getSelfIntro());

        Picasso.with(mThisActivity).invalidate(mAniCareService.getUserImageUrl(mThisUser.getId()));
        mAniCareService.setUserImageInto(mThisUser.getId(), userImage);

        if(mThisUser.isHasPet()) {
            havePet.check(R.id.user_setting_pet_yes);
        } else {
            havePet.check(R.id.user_setting_pet_no);
        }
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
}
