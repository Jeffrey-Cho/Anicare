package sep.software.anicare.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import sep.software.anicare.R;
import sep.software.anicare.callback.DialogCallback;
import sep.software.anicare.event.AniCareMessage;
import sep.software.anicare.model.AniCarePet;
import sep.software.anicare.model.AniCareUser;
import sep.software.anicare.service.AniCareDBService;
import sep.software.anicare.service.AniCareDBServicePreference;
import sep.software.anicare.util.AniCareLogger;
import sep.software.anicare.view.AniCareAlertDialog;

/**
 * Created by hongkunyoo on 15. 5. 3..
 */
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

        adapter.add("Remove User Setting");
        adapter.add("Remove Pet Setting");
        adapter.add("Remove AniCareMessage DB");

        adapter.add("Read User Setting");
        adapter.add("Read Pet Setting");
        adapter.add("Read AniCareMessage DB");

        adapter.add("Logout");


        final AniCareMessage msg = AniCareMessage.rand();

        dbService.addMessage(msg);
        List<AniCareMessage> list = dbService.listMessage();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str;
                switch (position) {
                    case 0:
                        AniCareUser user = mObjectPreference.get("user", AniCareUser.class);
                        user.setLocation(null);
                        mObjectPreference.put("user", user);
                        str = "Done";
                        new AlertDialog.Builder(mThisActivity)
                                .setMessage(str)
                                .setPositiveButton("ok", null)
                                .show();
                        break;

                    case 1:
                        mObjectPreference.remove("user");
//                        textView.setText("Remove Pet Setting");
                        break;

                    case 2:
                        mObjectPreference.remove("messageDB");
                        break;

                    case 3:
                        str = mObjectPreference.get("user", AniCareUser.class).toString();

                        new AlertDialog.Builder(mThisActivity)
                        .setMessage(str)
                        .setPositiveButton("ok", null)
                        .show();

                        break;

                    case 4:
//                        textView.setText(mObjectPreference.get("pet", AniCarePet.class).toString());
                        break;

                    case 5:
                        List<AniCareMessage> list = dbService.listMessage();
//                        textView.setText(list.toString());
                        break;

                    case 6:
                        mAniCareService.logout();
                        Intent intent = new Intent();
                        intent.setClass(mThisActivity, SplashActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }

            }
        });

    }
}
