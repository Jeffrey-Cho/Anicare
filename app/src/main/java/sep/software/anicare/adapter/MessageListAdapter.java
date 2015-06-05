package sep.software.anicare.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import sep.software.anicare.AniCareApp;
import sep.software.anicare.R;
import sep.software.anicare.model.AniCareMessage;
import sep.software.anicare.service.AniCareService;

/**
 * Created by Jeffrey on 2015. 6. 3..
 */
public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHolder> {

    private List<AniCareMessage> msgList;
    private AniCareService mAniCareService; // For Using AniCareService.setPetImageIntro (Picasso library)
    private AniCareApp mAppContext;

    public MessageListAdapter(List<AniCareMessage> msgList) {
        this.msgList = msgList;
        mAppContext = AniCareApp.getAppContext();
        mAniCareService = mAppContext.getAniCareService();
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

    public AniCareMessage getItem(int position) {
        return msgList.get(position);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder MessageViewHolder, int i) {
        AniCareMessage msgItem = msgList.get(i);

        if(msgItem.getRawType() == 0) {
            MessageViewHolder.name.setText(msgItem.getReceiver());
        } else {
            MessageViewHolder.name.setText(msgItem.getSender());
        }

        MessageViewHolder.msgContent.setText(msgItem.getContent());
        MessageViewHolder.date.setText(msgItem.getRawDateTime());
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        AniCareMessage msgItem = msgList.get(i);
        View itemView;

        if (msgItem.getRawType() == 0) { // Send Msg
            itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.send_message_card_view, viewGroup, false);

        } else  { // Receive Msg
            itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.receive_message_card_view, viewGroup, false);
        }

        return new MessageViewHolder(itemView);
    }


    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        protected TextView name;
        protected TextView msgContent;
        protected TextView date;

        public MessageViewHolder(View v) {
            super(v);

            name = (TextView) v.findViewById(R.id.name);
            msgContent = (TextView) v.findViewById(R.id.msg_content);
            date = (TextView) v.findViewById(R.id.msg_date);
        }
    }
}
