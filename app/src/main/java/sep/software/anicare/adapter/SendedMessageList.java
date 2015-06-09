package sep.software.anicare.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.prototypes.CardWithList;
import it.gmariotti.cardslib.library.prototypes.LinearListView;
import sep.software.anicare.AniCareApp;
import sep.software.anicare.R;
import sep.software.anicare.model.AniCareMessage;
import sep.software.anicare.service.AniCareService;
import sep.software.anicare.view.MessageDialog;

/**
 * Created by Jeffrey on 2015. 6. 5..
 */
public class SendedMessageList extends CardWithList {

    List<AniCareMessage> msgList;
    AniCareService mAniCareService;

    public SendedMessageList(Context context, List<AniCareMessage> sendedMsgList) {
        super(context);
        this.msgList = sendedMsgList;
        mAniCareService = AniCareApp.getAppContext().getAniCareService();
    }

    @Override
    protected CardHeader initCardHeader() {

        //Add Header
        CardHeader header = new CardHeader(getContext(), R.layout.carddemo_googleknow_withlist_inner_header) {

            @Override
            public void setupInnerViewElements(ViewGroup parent, View view) {
                super.setupInnerViewElements(parent, view);
                TextView subTitle = (TextView) view.findViewById(R.id.carddemo_googleknow_sub_title);
                if (subTitle != null) {
                    subTitle.setText("다시 메세지를 보내고자 할 경우 Send 버튼을 눌러주세요.");  //Should use strings.xml
                }
            }
        };
        header.setTitle("보낸 메세지"); //should use strings.xml
        return header;
    }

    @Override
    protected void initCard() {

    }

    @Override
    protected List<ListObject> initChildren() {

        List<ListObject> mObjects = new ArrayList<ListObject>(msgList.size());
        //MayKnowObject tempObject = new MayKnowObject(this);

        for(AniCareMessage msg: msgList) {
            MayKnowObject tempObject = new MayKnowObject(this, msg);
            tempObject.name = msg.getReceiver();
            tempObject.common = msg.getContent();
            String receiverId = msg.getReceiverId();
            if (msg.getReceiverId().equals("9715C50C-FD9C-4FA8-B557-9A2E8654DB43") && !msg.getReceiver().equals("HongKun"))
                msg.setReceiverId("dummy");
            tempObject.url = mAniCareService.getUserImageUrl(msg.getReceiverId());
            mObjects.add(tempObject);
        }

//        //Add an object to the list
//        MayKnowObject s1 = new MayKnowObject(this);
//        s1.name = "정지현";
//        s1.common = "안녕하세요? 반갑습니다. 맡기고 싶은데요...";
//        s1.url = "https://lh5.googleusercontent.com/-squZd7FxR8Q/UyN5UrsfkqI/AAAAAAAAbAo/VoDHSYAhC_E/s54/new%2520profile%2520%25282%2529.jpg";
//        mObjects.add(s1);
//
//        //Add an object to the list
//        MayKnowObject s2 = new MayKnowObject(this);
//        s2.name = "유홍근";
//        s2.common = "유홍근님이 당신에게 펫을 맡기고 싶어 합니다.";
//        s2.url = "https://lh5.googleusercontent.com/-squZd7FxR8Q/UyN5UrsfkqI/AAAAAAAAbAo/VoDHSYAhC_E/s54/new%2520profile%2520%25282%2529.jpg";
//        mObjects.add(s2);


        return mObjects;
    }

    @Override
    public View setupChildView(int childPosition, ListObject object, View convertView, ViewGroup parent) {

        //Setup the ui elements inside the item
        TextView textViewName = (TextView) convertView.findViewById(R.id.carddemo_know_name);
        TextView textViewPeople = (TextView) convertView.findViewById(R.id.carddemo_know_common);
        ImageView imagePeople = (ImageView) convertView.findViewById(R.id.carddemo_know_image);
        TextView addText = (TextView) convertView.findViewById(R.id.carddemo_know_add);

        final AniCareMessage msg = msgList.get(childPosition);
        //Retrieve the values from the object
        MayKnowObject stockObject = (MayKnowObject) object;
        textViewName.setText(stockObject.name);
        textViewPeople.setText(""+stockObject.common);

        Picasso.with(getContext()).setIndicatorsEnabled(false);
        Picasso.with(getContext()).load(stockObject.url)
                .into(imagePeople);

        addText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageDialog msgDial = new MessageDialog(getContext(), msg.getReceiver(), msg.getReceiverId());
                msgDial.show();
            }
        });

        return convertView;
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.sended_message_withlist_inner_main;
    }

    public class MayKnowObject extends DefaultListObject {

        public String name;
        public String common;
        public String url;
        public AniCareMessage msg;
        private MayKnowObject _this;

        public MayKnowObject(Card parentCard, AniCareMessage msg) {
            super(parentCard);
            init();
            this.msg = msg;
            _this = this;
        }

        private void init() {
            //OnClick Listener
            setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(LinearListView parent, View view, int position, ListObject object) {
//                    Toast.makeText(getContext(), "Click on " + getObjectId(), Toast.LENGTH_SHORT).show();
                }
            });

            //OnItemSwipeListener
            setOnItemSwipeListener(new OnItemSwipeListener() {

                @Override
                public void onItemSwipe(ListObject object, boolean dismissRight) {
                    final AniCareMessage msg = new Gson().fromJson(object.getObjectId(), AniCareMessage.class);
                    msg.resolved();
                    mAniCareService.updateMessageDB(msg.getId(), msg);
//                    Toast.makeText(getContext(), "Swipe on " + object.getObjectId(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public String getObjectId() {
            return msg.toString();
        }
    }
}