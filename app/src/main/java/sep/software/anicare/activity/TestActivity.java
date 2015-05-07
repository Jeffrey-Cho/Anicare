package sep.software.anicare.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import sep.software.anicare.R;
import sep.software.anicare.model.AniCareMessage;
import sep.software.anicare.service.AniCareDBService;
import sep.software.anicare.service.AniCareDBServicePreference;

public class TestActivity extends AniCareActivity {

    ListView listView;

    ArrayAdapter<String> adapter = null;
    AniCareDBService dbService = new AniCareDBServicePreference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        adapter = new ArrayAdapter<String>(mThisActivity, android.R.layout.simple_list_item_1);
        listView = (ListView) findViewById(R.id.test_activity_list_view);


        listView.setAdapter(adapter);

        adapter.add("Logout");
        adapter.add("Remove Pet Setting");
        adapter.add("Remove AniCareMessage DB");
        adapter.add("Remove History DB");

        adapter.add("Read User Setting");
        adapter.add("Read Pet Setting");
        adapter.add("Read AniCareMessage DB");
        adapter.add("Read History DB");




        final AniCareMessage msg = AniCareMessage.rand();

        dbService.addMessage(msg);
        List<AniCareMessage> list = dbService.listMessage();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str;
                switch (position) {
                    case 0:
                        mAniCareService.logout();
                        Intent intent = new Intent();
                        intent.setClass(mThisActivity, SplashActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                    case 1:
                        mAniCareService.removePetSetting();
                        str = "Removed Pet Setting";
                        new AlertDialog.Builder(mThisActivity)
                                .setMessage(str)
                                .setPositiveButton("ok", null)
                                .show();
                        break;

                    case 2:
                        dbService.deleteMessageAll();
                        str = "Removed AniCareMessage DB";
                        new AlertDialog.Builder(mThisActivity)
                                .setMessage(str)
                                .setPositiveButton("ok", null)
                                .show();
                        break;

                    case 3:
                        dbService.deleteHistoryAll();
                        str = "Removed CareHistory DB";
                        new AlertDialog.Builder(mThisActivity)
                                .setMessage(str)
                                .setPositiveButton("ok", null)
                                .show();
                        break;

                    case 4:
                        str = mAniCareService.getCurrentUser().toString();

                        new AlertDialog.Builder(mThisActivity)
                        .setMessage(str)
                        .setPositiveButton("ok", null)
                        .show();

                        break;

                    case 5:
                        str = mAniCareService.getCurrentPet().toString();

                        new AlertDialog.Builder(mThisActivity)
                                .setMessage(str)
                                .setPositiveButton("ok", null)
                                .show();

                        break;

                    case 6:
                        str = dbService.listMessage().toString();
                        new AlertDialog.Builder(mThisActivity)
                                .setMessage(str)
                                .setPositiveButton("ok", null)
                                .show();
                        break;

                    case 7:
                        str = dbService.listHistory().toString();
                        new AlertDialog.Builder(mThisActivity)
                                .setMessage(str)
                                .setPositiveButton("ok", null)
                                .show();
                        break;

                }

            }
        });

    }
}
