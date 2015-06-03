package sep.software.anicare.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import sep.software.anicare.R;
import sep.software.anicare.interfaces.EntityCallback;
import sep.software.anicare.model.AniCareMessage;
import sep.software.anicare.model.AniCarePet;
import sep.software.anicare.service.AniCareDBService;
import sep.software.anicare.service.AniCareDBServicePreference;
import sep.software.anicare.service.AniCareDBServiceSQLite;
import sep.software.anicare.service.BlobStorageService;
import sep.software.anicare.util.AniCareLogger;

public class TestActivity extends AniCareActivity {

    ListView listView;

    ArrayAdapter<String> adapter = null;
    AniCareDBService dbService;

    ArrayList<DescriptionInterface> descriptions; // = new ArrayList<DescriptionInterface>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        dbService = new AniCareDBServiceSQLite(mThisActivity);
        adapter = new ArrayAdapter<String>(mThisActivity, android.R.layout.simple_list_item_1);
        listView = (ListView) findViewById(R.id.test_activity_list_view);


        listView.setAdapter(adapter);

//        adapter.add("Logout");
//        adapter.add("Remove Pet Setting");
//        adapter.add("Remove AniCareMessage DB");
//        adapter.add("Remove History DB");
//
//        adapter.add("Read User Setting");
//        adapter.add("Read Pet Setting");
//        adapter.add("Read AniCareMessage DB");
//        adapter.add("Read History DB");

        descriptions = new ArrayList<DescriptionInterface>(){
            {
                add(new DescriptionInterface() {

                    @Override
                    public String title() {
                        return "Logout";
                    }

                    @Override
                    public void describe() {
                        mAniCareService.logout();
                        Intent intent = new Intent();
                        intent.setClass(mThisActivity, SplashActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

                add(new DescriptionInterface() {

                    @Override
                    public String title() {
                        return "Remove Pet Setting";
                    }

                    @Override
                    public void describe() {
                        mAniCareService.removePetSetting();
                        String str = "Removed Pet Setting";
                        new AlertDialog.Builder(mThisActivity)
                                .setMessage(str)
                                .setPositiveButton("ok", null)
                                .show();
                    }
                });

                add(new DescriptionInterface() {

                    @Override
                    public String title() {
                        return "Remove AniCareMessage DB";
                    }

                    @Override
                    public void describe() {
                        String str = "Removed AniCareMessage DB";
                        new AlertDialog.Builder(mThisActivity)
                                .setMessage(str)
                                .setPositiveButton("ok", null)
                                .show();


                    }
                });

                add(new DescriptionInterface() {

                    @Override
                    public String title() {
                        return "Remove History DB";
                    }

                    @Override
                    public void describe() {
                        dbService.deleteHistoryAll();
                        String str = "Removed CareHistory DB";
                        new AlertDialog.Builder(mThisActivity)
                                .setMessage(str)
                                .setPositiveButton("ok", null)
                                .show();


                    }
                });

                add(new DescriptionInterface() {

                    @Override
                    public String title() {
                        return "Read User Setting";
                    }

                    @Override
                    public void describe() {
                        String str = mAniCareService.getCurrentUser().toString();

                        new AlertDialog.Builder(mThisActivity)
                                .setMessage(str)
                                .setPositiveButton("ok", null)
                                .show();


                    }
                });

                add(new DescriptionInterface() {

                    @Override
                    public String title() {
                        return "Read Pet Setting";
                    }

                    @Override
                    public void describe() {
                        String str = mAniCareService.getCurrentPet().toString();

                        new AlertDialog.Builder(mThisActivity)
                                .setMessage(str)
                                .setPositiveButton("ok", null)
                                .show();


                    }
                });

                add(new DescriptionInterface() {

                    @Override
                    public String title() {
                        return "Read AniCareMessage DB";
                    }

                    @Override
                    public void describe() {
                        String str = dbService.listMessage().toString();
                        new AlertDialog.Builder(mThisActivity)
                                .setMessage(str)
                                .setPositiveButton("ok", null)
                                .show();


                    }
                });

                add(new DescriptionInterface() {

                    @Override
                    public String title() {
                        return "Read History DB";
                    }

                    @Override
                    public void describe() {
                        String str = dbService.listHistory().toString();
                        new AlertDialog.Builder(mThisActivity)
                                .setMessage(str)
                                .setPositiveButton("ok", null)
                                .show();


                    }
                });

                add(new DescriptionInterface() {

                    @Override
                    public String title() {
                        return "Go to Map Activity";
                    }

                    @Override
                    public void describe() {

                        Intent intent = new Intent();
                        intent.setClass(mThisActivity, MapActivity.class);
                        startActivity(intent);
                    }
                });

                add(new DescriptionInterface() {

                    @Override
                    public String title() {
                        return "Add random message to DB";
                    }

                    @Override
                    public void describe() {
                        AniCareMessage msg = AniCareMessage.rand();
                        String str = msg.toString();
                        new AlertDialog.Builder(mThisActivity)
                                .setMessage(str)
                                .setPositiveButton("ok", null)
                                .show();
                        dbService.addMessage(msg);

                    }
                });

                add(new DescriptionInterface() {

                    @Override
                    public String title() {
                        return "Get every message list from DB";
                    }

                    @Override
                    public void describe() {
                        List<AniCareMessage> list = dbService.listMessage();
                        if (list == null) return;
                        new AlertDialog.Builder(mThisActivity)
                                .setMessage(list.toString())
                                .setPositiveButton("ok", null)
                                .show();

                    }
                });

                add(new DescriptionInterface() {

                    @Override
                    public String title() {
                        return "Set Blob storage CORS";
                    }

                    @Override
                    public void describe() {
                        final BlobStorageService blobStorageService = new BlobStorageService();
                        new AsyncTask<Void, Void, Boolean>(){


                            @Override
                            protected Boolean doInBackground(Void... params) {

                                return blobStorageService.setHeader();
                            }

                            @Override
                            protected void onPostExecute(Boolean aVoid) {
                                super.onPostExecute(aVoid);
                                AniCareLogger.log(aVoid);
                            }
                        }.execute();


                    }
                });
            }
        };


        for (DescriptionInterface des : descriptions) {
            adapter.add(des.title());
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str;
                DescriptionInterface des = descriptions.get(position);
                des.describe();

//                switch (position) {
//                    case 0:
//                        mAniCareService.logout();
//                        Intent intent = new Intent();
//                        intent.setClass(mThisActivity, SplashActivity.class);
//                        startActivity(intent);
//                        finish();
//                        break;
//
//                    case 1:
//                        mAniCareService.removePetSetting();
//                        str = "Removed Pet Setting";
//                        new AlertDialog.Builder(mThisActivity)
//                                .setMessage(str)
//                                .setPositiveButton("ok", null)
//                                .show();
//                        break;
//
//                    case 2:
//                        dbService.deleteMessageAll();
//                        str = "Removed AniCareMessage DB";
//                        new AlertDialog.Builder(mThisActivity)
//                                .setMessage(str)
//                                .setPositiveButton("ok", null)
//                                .show();
//                        break;
//
//                    case 3:
//                        dbService.deleteHistoryAll();
//                        str = "Removed CareHistory DB";
//                        new AlertDialog.Builder(mThisActivity)
//                                .setMessage(str)
//                                .setPositiveButton("ok", null)
//                                .show();
//                        break;
//
//                    case 4:
//                        str = mAniCareService.getCurrentUser().toString();
//
//                        new AlertDialog.Builder(mThisActivity)
//                        .setMessage(str)
//                        .setPositiveButton("ok", null)
//                        .show();
//
//                        break;
//
//                    case 5:
//                        str = mAniCareService.getCurrentPet().toString();
//
//                        new AlertDialog.Builder(mThisActivity)
//                                .setMessage(str)
//                                .setPositiveButton("ok", null)
//                                .show();
//
//                        break;
//
//                    case 6:
//                        str = dbService.listMessage().toString();
//                        new AlertDialog.Builder(mThisActivity)
//                                .setMessage(str)
//                                .setPositiveButton("ok", null)
//                                .show();
//                        break;
//
//                    case 7:
//                        str = dbService.listHistory().toString();
//                        new AlertDialog.Builder(mThisActivity)
//                                .setMessage(str)
//                                .setPositiveButton("ok", null)
//                                .show();
//                        break;
//
//                }

            }
        });

    }

    interface DescriptionInterface {
        public String title();
        public void describe();
    }
}
