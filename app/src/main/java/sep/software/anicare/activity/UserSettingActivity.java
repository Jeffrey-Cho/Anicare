package sep.software.anicare.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import sep.software.anicare.R;
import sep.software.anicare.callback.EntityCallback;
import sep.software.anicare.model.AniCareUser;

public class UserSettingActivity extends AniCareActivity {

    TextView textView;
    Button randSet;
    Button gotoPetSettingBtn;
    Button gotoMainBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        textView = (TextView) findViewById(R.id.user_setting_text_view);
        randSet = (Button) findViewById(R.id.user_setting_rand_set);
        gotoPetSettingBtn = (Button) findViewById(R.id.user_setting_go_to_pet_setting);
        gotoMainBtn = (Button) findViewById(R.id.user_setting_go_back_to_main);

        randSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AniCareUser user = AniCareUser.rand();
                mAniCareService.putUser(user, new EntityCallback<AniCareUser>() {
                    @Override
                    public void onCompleted(AniCareUser entity) {
                        textView.setText(entity.toString());
                    }
                });
            }
        });

        gotoPetSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mThisActivity, PetSettingActivity.class);
                startActivity(intent);
                finish();
            }
        });

        gotoMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
