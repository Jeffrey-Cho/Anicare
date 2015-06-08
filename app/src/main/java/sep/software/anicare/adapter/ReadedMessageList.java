package sep.software.anicare.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.prototypes.CardWithList;
import it.gmariotti.cardslib.library.prototypes.LinearListView;
import sep.software.anicare.AniCareApp;
import sep.software.anicare.R;
import sep.software.anicare.interfaces.EntityCallback;
import sep.software.anicare.model.AniCareDateTime;
import sep.software.anicare.model.AniCareMessage;
import sep.software.anicare.model.AniCareUser;
import sep.software.anicare.service.AniCareService;

/**
 * Created by apple on 2015. 6. 8..
 */
public class ReadedMessageList extends CardWithList {


    List<AniCareMessage> msgList;
    AniCareService mAniCareService;
    AniCareApp mAppContext;

    public ReadedMessageList(Context context, List<AniCareMessage> readedMsglist) {
        super(context);
        this.msgList = readedMsglist;
        mAppContext = AniCareApp.getAppContext();
        mAniCareService = mAppContext.getAniCareService();
//        EventBus.getDefault().register(this);
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
                    subTitle.setText("읽은 메세지입니다. 복구하고자 할 경우 다시 스와이프를 해주세요.");  //Should use strings.xml
                }
            }
        };
        header.setTitle("읽은 메세지"); //should use strings.xml
        return header;
    }

    @Override
    protected void initCard() {

    }

    @Override
    protected List<CardWithList.ListObject> initChildren() {

        List<CardWithList.ListObject> mObjects = new ArrayList<CardWithList.ListObject>(msgList.size());
        //MayKnowObject tempObject = new MayKnowObject(this);

        for(AniCareMessage msg: msgList) {
            mObjects.add(new MayKnowObject(this, msg));
        }

        return mObjects;
    }

    @Override
    public View setupChildView(final int childPosition, CardWithList.ListObject object, View convertView, ViewGroup parent) {

        //Setup the ui elements inside the item
        TextView textViewName = (TextView) convertView.findViewById(R.id.carddemo_know_name);
        TextView textViewPeople = (TextView) convertView.findViewById(R.id.carddemo_know_common);
        ImageView imagePeople = (ImageView) convertView.findViewById(R.id.carddemo_know_image);
        //TextView addText = (TextView) convertView.findViewById(R.id.carddemo_know_add);

        final AniCareMessage msg = msgList.get(childPosition);

        //Retrieve the values from the object
        final MayKnowObject stockObject = (MayKnowObject) object;
        textViewName.setText(stockObject.name);
        textViewPeople.setText(""+stockObject.common);

        Picasso.with(getContext()).setIndicatorsEnabled(false);
        Picasso.with(getContext()).load(stockObject.url).into(imagePeople);
//        if (msg.getCommType().getValue() != AniCareMessage.CommType.REQUEST.getValue()) {
//            addText.setVisibility(View.GONE);
//        }
//        addText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Toast.makeText(getContext(), "Add Clicked", Toast.LENGTH_SHORT).show();
//                AniCareUser me = mAniCareService.getCurrentUser();
//                final AniCareMessage responseMsg = new AniCareMessage();
//                responseMsg.setType(AniCareMessage.Type.SYSTEM);
//                responseMsg.setCommType(AniCareMessage.CommType.ACCEPT);
//                responseMsg.setReceiver(msg.getSender());
//                responseMsg.setReceiverId(msg.getSenderId());
//                responseMsg.setSender(me.getName());
//                responseMsg.setSenderId(me.getId());
//                responseMsg.setContent(me.getName() + " has accepted your request.");
//                responseMsg.setDateTime(AniCareDateTime.now());
//                mAppContext.showProgressDialog(getContext());
//                //Log.d(TAG, responseMsg.toString());
//                mAniCareService.sendMessage(responseMsg ,new EntityCallback<AniCareMessage>() {
//                    @Override
//                    public void onCompleted(AniCareMessage entity) {
//                        mAppContext.dismissProgressDialog();
//                        msg.resolved();
//
//                        mLinearListAdapter.remove(stockObject);
//                        mLinearListAdapter.notifyDataSetChanged();
//                        //Log.d(TAG, entity.toString());
//                        mAniCareService.plsPoint(100); // When I accept other user request
//                        //mAniCareService.updateMessageDB(msg.getId(), msg);
//                        mAniCareService.addMessageDB(responseMsg); // buf fixed
//
//
//                        Toast.makeText(getContext(), "Message sent", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });

        return convertView;
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.readed_message_withlist_inner_main;
    }

//    public void onEvent(AniCareMessage msg) {
//        AniCareLogger.log("onEvent! : " + msg);
//        mLinearListAdapter.add(new MayKnowObject(this, msg));
//    }

    public class MayKnowObject extends CardWithList.DefaultListObject {

        public String name;
        public String common;
        public String url;
        public AniCareMessage msg;
        private MayKnowObject _this;

        public MayKnowObject(Card parentCard, AniCareMessage msg) {
            super(parentCard);
            init();
            this.name = msg.getSender();
            this.common = msg.getContent();
            this.url = mAniCareService.getUserImageUrl(msg.getSenderId());
            this.msg = msg;
            _this = this;
        }

        private void init() {
            //OnClick Listener
            setOnItemClickListener(new CardWithList.OnItemClickListener() {
                @Override
                public void onItemClick(LinearListView parent, View view, int position, CardWithList.ListObject object) {
//                    Toast.makeText(getContext(), "Click on " + msg.getSender(), Toast.LENGTH_SHORT).show();
                }
            });

            //OnItemSwipeListener
            setOnItemSwipeListener(new CardWithList.OnItemSwipeListener() {
                @Override
                public void onItemSwipe(CardWithList.ListObject object, boolean dismissRight) {
                    final AniCareMessage msg = new Gson().fromJson(object.getObjectId(), AniCareMessage.class);
                        msg.unresolved();
                        mAniCareService.updateMessageDB(msg.getId(), msg);
                    }

//                    Toast.makeText(getContext(), "Swipe on " + msg.getSender(), Toast.LENGTH_SHORT).show();

            });
        }

        @Override
        public String getObjectId() {
            return msg.toString();
        }
    }
}
