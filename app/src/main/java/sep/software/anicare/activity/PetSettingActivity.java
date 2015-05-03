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
import sep.software.anicare.model.AniCarePet;
import sep.software.anicare.model.AniCareUser;

public class PetSettingActivity extends AniCareActivity {

    TextView textView;
    Button randSet;
    Button gotoMainBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_setting);


        textView = (TextView) findViewById(R.id.pet_setting_text_view);
        randSet = (Button) findViewById(R.id.pet_setting_rand_set);
        gotoMainBtn = (Button) findViewById(R.id.pet_setting_go_back_to_main);

        randSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AniCarePet pet = AniCarePet.rand();
                mAniCareService.putPet(pet, new EntityCallback<AniCarePet>() {
                    @Override
                    public void onCompleted(AniCarePet entity) {
                        textView.setText(entity.toString());
                    }
                });
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
