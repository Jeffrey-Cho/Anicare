package sep.software.anicare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import sep.software.anicare.R;
import sep.software.anicare.model.AniCarePet;
import sep.software.anicare.model.AniCareUser;

/**
 * Created by hongkunyoo on 15. 5. 3..
 */
public class TestActivity extends AniCareActivity {

    TextView textView;
    Button removeUserSettingBtn;
    Button removePetSettingBtn;
    Button logoutBtn;

    Button seeCurrentUser;
    Button seeCurrentPet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        textView = (TextView) findViewById(R.id.test_activity_text_view);
        removeUserSettingBtn = (Button) findViewById(R.id.test_activity_remove_user_setting);
        removePetSettingBtn = (Button) findViewById(R.id.test_activity_remove_pet_setting);
        logoutBtn = (Button) findViewById(R.id.test_activity_logout);
        seeCurrentUser = (Button) findViewById(R.id.test_activity_see_user_setting);
        seeCurrentPet = (Button) findViewById(R.id.test_activity_see_pet_setting);

        removeUserSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mObjectPreference.remove("userSetting");
                textView.setText("Remove User Setting");
            }
        });

        removePetSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mObjectPreference.remove("petSetting");
                textView.setText("Remove Pet Setting");
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAniCareService.logout();
                textView.setText("Logout");
                Intent intent = new Intent();
                intent.setClass(mThisActivity, SplashActivity.class);
                startActivity(intent);
                finish();
            }
        });

        seeCurrentUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(mObjectPreference.get("userSetting", AniCareUser.class).toString());
            }
        });

        seeCurrentPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(mObjectPreference.get("petSetting", AniCarePet.class).toString());
            }
        });
    }
}
