package sep.software.anicare.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import de.greenrobot.event.EventBus;
import sep.software.anicare.AniCareException;
import sep.software.anicare.R;
import sep.software.anicare.interfaces.EntityCallback;
import sep.software.anicare.model.AniCareMessage;
import sep.software.anicare.model.AniCarePet;
import sep.software.anicare.service.AniCareAsyncTask;
import sep.software.anicare.util.AniCareLogger;
import sep.software.anicare.util.AsyncChainer;
import sep.software.anicare.util.FileUtil;
import sep.software.anicare.util.ImageUtil;
import sep.software.anicare.view.MessageDialog;

/**
 * Created by apple on 2015. 6. 1..
 */
public class PetDetailActivity extends AniCareActivity implements View.OnClickListener {

    private static final String TAG = PetDetailActivity.class.getSimpleName();

    private ImageView petImage;
    private Uri mProfileImageUri;
    private TextView petName;
    private TextView petLocation;
    private TextView petCategory;
    private TextView petDetailLivingType;
    private TextView petSize;
    private TextView petSex;
    private TextView petPersonality;
    private TextView petNeutralized;
    private TextView petFeed;
    private Button friendRequestBtn;
    private Button sendMsgBtn;

    private AniCarePet.Category Category;
    private Bitmap petImageBitmap;

    private AniCarePet selectedPet;

    private String male = "수컷"; //getResources().getString(R.string.item_male);
    private String female = "암컷"; //getResources().getString(R.string.item_female);
    private String yesStr = "유"; //getResources().getString(R.string.yes);
    private String noStr = "무"; //getResources().getString(R.string.no);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_detail);

        Bundle bundle = getIntent().getExtras();
        selectedPet = bundle.getParcelable("petInfo"); // get selected pet information

        //getActionBar().hide();

        petImage = (ImageView) findViewById(R.id.petProfileImage);
        petName = (TextView) findViewById(R.id.pet_detail_name);
        petName.setText(selectedPet.getName());
        petName.setSelected(false);
        petLocation = (TextView) findViewById(R.id.pet_detail_location);
        //petLocation.setText(selectedPet.getLocation());
        petLocation.setSelected(false);

        petCategory = (TextView)findViewById(R.id.pet_detail_category);

        switch(selectedPet.getRawCategory()) {
            case 0:
                petCategory.setText(getResources().getString(R.string.item_dog));
                break;
            case 1:
                petCategory.setText(getResources().getString(R.string.item_cat));
                break;
            case 2:
                petCategory.setText(getResources().getString(R.string.item_bird));
                break;
            default:
                petCategory.setText(getResources().getString(R.string.item_default));
                break;
        }

        petCategory.setSelected(false);

        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.pet_category,android.R.layout.simple_spinner_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //petCategory.setAdapter(adapter);
        //petCategory.setOnItemSelectedListener(this);

        petDetailLivingType = (TextView) findViewById(R.id.pet_detail_living_type);
        //petDetailLivingType.setText(selectedPet.getLivingType());
        petDetailLivingType.setSelected(false);

        petSize = (TextView) findViewById(R.id.pet_size);

        switch (selectedPet.getRawSize()) {
            case 0:
                petSize.setText(getResources().getString(R.string.item_large));
                break;
            case 1:
                petSize.setText(getResources().getString(R.string.item_medium));
                break;
            case 2:
                petSize.setText(getResources().getString(R.string.item_small));
                break;
        }

        petSize.setSelected(false);

        petSex = (TextView) findViewById(R.id.pet_sexuality);
        petSex.setText(selectedPet.isMale() ? male : female);
        petSex.setSelected(false);

        petPersonality = (TextView) findViewById(R.id.pet_talenet);

        switch(selectedPet.getRawPersonality()) {
            case 0: // bright
                petPersonality.setText(getResources().getString(R.string.item_bright));
                break;
            case 1: // shy
                petPersonality.setText(getResources().getString(R.string.item_shy));
                break;
            case 2: // normal
                petPersonality.setText(getResources().getString(R.string.item_normal));
                break;
        }

        petPersonality.setSelected(false);

        petNeutralized = (TextView) findViewById(R.id.pet_neutralized);
        petNeutralized.setText(selectedPet.isNeutralized() ? yesStr : noStr);
        petNeutralized.setSelected(false);

        petFeed = (TextView) findViewById(R.id.pet_feed);
        petFeed.setText(selectedPet.isPetFood() ? yesStr : noStr);
        petFeed.setSelected(false);

        friendRequestBtn = (Button) findViewById(R.id.pet_detail_submit);
        sendMsgBtn = (Button) findViewById(R.id.pet_detail_send_message);

        friendRequestBtn.setOnClickListener(this);
        sendMsgBtn.setOnClickListener(this);

        mAniCareService.setPetImageInto(selectedPet.getId(), petImage);
        //setImageButton();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.pet_detail_submit: // friend request
                matchWith(selectedPet);
                break;
            case R.id.pet_detail_send_message: // send messaage
                sendMessageTo(selectedPet);
                break;
            default:
                break;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void matchWith(AniCarePet pet) {
        AniCareMessage msg = new AniCareMessage();

//        msg.
        mAniCareService.sendMessage(msg, new EntityCallback<AniCareMessage>() {
            @Override
            public void onCompleted(AniCareMessage entity) {

            }
        });

    }

    private void sendMessageTo(AniCarePet pet) {
        String receiver = pet.getUserName();
        String receiverId = pet.getUserId();
        MessageDialog dialog = new MessageDialog(mThisActivity, receiver, receiverId);
        dialog.show();
        Toast.makeText(mThisActivity, "This message will be send to " + receiver+"("+receiverId+")", Toast.LENGTH_SHORT).show();
    }

//    private void setImageButton(){
//        petImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FileUtil.getMediaFromGallery(mThisActivity);
//            }
//        });
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK){
//            String imagePath = null;
//
//            switch(requestCode){
//                case FileUtil.GALLERY:
//                    mProfileImageUri = data.getData();
//                    imagePath = FileUtil.getMediaPathFromGalleryUri(mThisActivity, mProfileImageUri);
//                    break;
//                case FileUtil.CAMERA:
//                    mProfileImageUri = FileUtil.getMediaUriFromCamera(mThisActivity, data, mProfileImageUri);
//                    imagePath = mProfileImageUri.getPath();
//                    break;
//            }
//
//            updateProfileImage(imagePath);
//        }
//    }

//    private void updateProfileImage(String imagePath){
////        mAppContext.showProgressDialog(mThisActivity);
//        petImageBitmap = ImageUtil.refineSquareImage(imagePath, ImageUtil.PROFILE_IMAGE_SIZE);
//        Bitmap profileThumbnailImageBitmap = ImageUtil.refineSquareImage(imagePath, ImageUtil.PROFILE_THUMBNAIL_IMAGE_SIZE);
////        updateProfileImage(profileImageBitmap, profileThumbnailImageBitmap);
//        petImage.setImageBitmap(petImageBitmap);
//    }

//    public class ImageAsyncTask extends AniCareAsyncTask<String, Integer, Bitmap> {
//
//        @Override
//        protected Bitmap doInBackground(String... params) {
//            // TODO Auto-generated method stub
//            try {
//                return Picasso.with(mThisActivity).load(profileImageUrl).get();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//                EventBus.getDefault().post(new AniCareException(AniCareException.TYPE.NETWORK_UNAVAILABLE));
//                return null;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            Bitmap profileImageBitmap = ImageUtil.refineSquareImage(bitmap, ImageUtil.PROFILE_IMAGE_SIZE);
//            petImage.setImageBitmap(profileImageBitmap);
//        }
//    }

}