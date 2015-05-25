package sep.software.anicare.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
    EditText content;
    Button cancelBtn;
    Button sendBtn;

    AniCareApp mApp;
    AniCareService mAniCareService;

    String receiver;
    String receiverId;


    public MessageDialog(Context context, String receiver, String receiverId) {
        super(context);
        this.context = context;
        this.mApp = AniCareApp.getAppContext();
        mAniCareService = mApp.getAniCareService();
        this.receiver = receiver;
        this.receiverId = receiverId;
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
        content = (EditText)findViewById(R.id.custom_message_dialog_content);
        cancelBtn = (Button)findViewById(R.id.custom_message_dialog_cancel_btn);
        sendBtn =   (Button)findViewById(R.id.custom_message_dialog_send_btn);

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
                msg.setRawType(1);
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
                        mApp.dismissProgressDialog();
                        dismiss();
                    }
                });
            }
        });
    }
}
