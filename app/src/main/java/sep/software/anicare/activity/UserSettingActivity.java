package sep.software.anicare.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import sep.software.anicare.model.AniCareUser;
import sep.software.anicare.service.AniCareAsyncTask;
import sep.software.anicare.util.ImageUtil;

public class UserSettingActivity extends AniCareActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private static final String TAG = UserSettingActivity.class.getSimpleName();
    private String profileImageUrl;

    private ImageView userImage;
    private TextView userName;
    private TextView userLocation;
    private Spinner userLivingType;
    private RadioGroup havePet;
    private TextView selfIntro;
    private Button submitBtn;

    private AniCareUser.HouseType livingType;
    private int defaultPoint = 100;

    private ImageAsyncTask imageTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        getActionBar().hide();

        userImage = (ImageView) findViewById(R.id.profileImage);
        userName = (TextView) findViewById(R.id.user_setting_name);
        userLocation = (TextView) findViewById(R.id.user_setting_location);

        userLivingType = (Spinner) findViewById(R.id.user_setting_living_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.user_living_type,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userLivingType.setAdapter(adapter);
        userLivingType.setOnItemSelectedListener(this);

        havePet = (RadioGroup) findViewById(R.id.is_pet);
        selfIntro = (TextView) findViewById(R.id.user_setting_self_intro);
        submitBtn = (Button) findViewById(R.id.user_setting_submit);
        submitBtn.setOnClickListener(this);

        userName.setText(mThisUser.getName());
        profileImageUrl = mAniCareService.getUserImageUrl(mThisUser.getId());

//        imageTask = new ImageAsyncTask();
//        imageTask.execute(profileImageUrl);

        // 성래 형님, 이미지 로드하실 때, 다음과 같은 API도 사용 가능합니다. 참고바랍니다. (내부적으로는 마찬가지로 Picasso 이용함.)
        // Implementation : AniCareServiceTest class Line #177
        mAniCareService.setUserImageInto(mThisUser.getId(), userImage);

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

    @Override
    public void onClick(View v) {

        if (v.equals(submitBtn)) {

            if (checkContent()) {
                AniCareUser user = mAniCareService.getCurrentUser();

                user.setName(userName.getText().toString());
                user.setLocation(userLocation.getText().toString());
                if (selfIntro != null && !selfIntro.getText().toString().isEmpty()) {
                    user.setSelfIntro(selfIntro.getText().toString());
                } else {
                    user.setSelfIntro(null);
                }
                user.setPoint(defaultPoint); // temporary default point
                user.setHouseType(livingType);
                user.setHasPet(havePet.getCheckedRadioButtonId() == R.id.user_setting_pet_yes);

                mAniCareService.putUser(user, new EntityCallback<AniCareUser>() {
                    @Override
                    public void onCompleted(AniCareUser entity) {
                        Intent intent = new Intent();
                        intent.setClass(mThisActivity, PetSettingActivity.class);
                        startActivity(intent);
                        //finish();
                    }
                });

            } else {
                // alert window show
            }
        }
    }

    public class ImageAsyncTask extends AniCareAsyncTask <String, Integer, Bitmap> {

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
            userImage.setImageBitmap(profileImageBitmap);
        }
    }

}
