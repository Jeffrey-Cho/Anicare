package sep.software.anicare.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import sep.software.anicare.AniCareApp;
import sep.software.anicare.R;
import sep.software.anicare.interfaces.EntityCallback;
import sep.software.anicare.model.AniCareDateTime;
import sep.software.anicare.model.AniCareMessage;
import sep.software.anicare.model.AniCareUser;
import sep.software.anicare.service.AniCareService;

/**
 * Created by hongkunyoo on 15. 5. 25..
 */
public class MessageDialog extends Dialog {

    Context context;
    TextView toWhom;
    TextView selfIntroView;
    EditText content;
    ImageButton cancelBtn;
    ImageButton sendBtn;
    CircleImageView receiverImage;

    AniCareApp mApp;
    AniCareService mAniCareService;

    String receiver;
    String receiverId;
    String selfIntro;


    public MessageDialog(Context context, String receiver, String receiverId, String selfIntro) {
        super(context);
        this.context = context;
        this.mApp = AniCareApp.getAppContext();
        mAniCareService = mApp.getAniCareService();
        this.receiver = receiver;
        this.receiverId = receiverId;
        this.selfIntro = selfIntro;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.custom_message_dialog);

        toWhom = (TextView) findViewById(R.id.custom_message_dialog_to_whom);
        selfIntroView = (TextView) findViewById(R.id.custom_message_dialog_intro);

        selfIntroView.setText("[자기소개]"+selfIntro);
        content = (EditText)findViewById(R.id.custom_message_dialog_content);
        cancelBtn = (ImageButton)findViewById(R.id.custom_message_dialog_cancel_btn);
        sendBtn =   (ImageButton)findViewById(R.id.custom_message_dialog_send_btn);
        receiverImage = (CircleImageView)findViewById(R.id.receiver_image);

        toWhom.setText(this.receiver);
        setTitle("Send Message");

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AniCareMessage msg = new AniCareMessage();

                AniCareUser me = mAniCareService.getCurrentUser();
                msg.setType(AniCareMessage.Type.MESSAGE);
                msg.setSender(me.getName());
                msg.setSenderId(me.getId());
                msg.setReceiver(receiver);
                msg.setReceiverId(receiverId);
                msg.setDateTime(AniCareDateTime.now());
                msg.makeRelation();
                msg.setContent(content.getText().toString());

                mApp.showProgressDialog(context);

                mAniCareService.sendMessage(msg, new EntityCallback<AniCareMessage>() {
                    @Override
                    public void onCompleted(AniCareMessage entity) {
                        mAniCareService.addMessageDB(entity);
                        mApp.dismissProgressDialog();
                        dismiss();
                    }
                });
            }
        });

        mAniCareService.setUserImageInto(receiverId,receiverImage);
    }
}
