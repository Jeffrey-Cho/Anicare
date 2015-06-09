package sep.software.anicare.adapter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
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
import sep.software.anicare.util.AniCareLogger;

/**
 * Created by Jeffrey on 2015. 6. 5..
 */
public class SystemMessageList extends CardWithList {

    List<AniCareMessage> msgList;
    AniCareService mAniCareService;
    AniCareApp mAppContext;

    public SystemMessageList(Context context, List<AniCareMessage> systemMsglist) {
        super(context);
        this.msgList = systemMsglist;
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
                    subTitle.setText("돌봄 서비스를 수락할 경우 Accept 버튼을 눌러주세요.");  //Should use strings.xml
                }
            }
        };
        header.setTitle("시스템 메세지"); //should use strings.xml
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
            mObjects.add(new MayKnowObject(this, msg));
        }

        return mObjects;
    }

    @Override
    public View setupChildView(final int childPosition, ListObject object, View convertView, ViewGroup parent) {

        //Setup the ui elements inside the item
        TextView textViewName = (TextView) convertView.findViewById(R.id.carddemo_know_name);
        TextView textViewPeople = (TextView) convertView.findViewById(R.id.carddemo_know_common);
        ImageView imagePeople = (ImageView) convertView.findViewById(R.id.carddemo_know_image);
        TextView addText = (TextView) convertView.findViewById(R.id.carddemo_know_add);

        final AniCareMessage msg = msgList.get(childPosition);

        //Retrieve the values from the object
        final MayKnowObject stockObject = (MayKnowObject) object;
        textViewName.setText(stockObject.name);
        textViewPeople.setText(""+stockObject.common);

        Picasso.with(getContext()).setIndicatorsEnabled(false);
        Picasso.with(getContext()).load(stockObject.url).into(imagePeople);
        if (msg.getCommType().getValue() != AniCareMessage.CommType.REQUEST.getValue()) {
            addText.setVisibility(View.GONE);
        }
        addText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(), "Add Clicked", Toast.LENGTH_SHORT).show();
            AniCareUser me = mAniCareService.getCurrentUser();
            final AniCareMessage responseMsg = new AniCareMessage();
            responseMsg.setType(AniCareMessage.Type.SYSTEM);
            responseMsg.setCommType(AniCareMessage.CommType.ACCEPT);
            responseMsg.setReceiver(msg.getSender());
            responseMsg.setReceiverId(msg.getSenderId());
            responseMsg.setSender(me.getName());
            responseMsg.setSenderId(me.getId());
            responseMsg.setContent(me.getName() + " has accepted your request.");
            responseMsg.setDateTime(AniCareDateTime.now());
            mAppContext.showProgressDialog(getContext());
            //Log.d(TAG, responseMsg.toString());
            mAniCareService.sendMessage(responseMsg ,new EntityCallback<AniCareMessage>() {
                @Override
                public void onCompleted(AniCareMessage entity) {
                    mAppContext.dismissProgressDialog();
                    msg.resolved();

                    mLinearListAdapter.remove(stockObject);
                    mLinearListAdapter.notifyDataSetChanged();

                    //Log.d(TAG, entity.toString());
                    mAniCareService.plsPoint(100); // When I accept other user request
                    //mAniCareService.updateMessageDB(msg.getId(), msg);
                    mAniCareService.addMessageDB(responseMsg); // buf fixed

                    Toast.makeText(getContext(),"Message sent", Toast.LENGTH_SHORT).show();
                }
            });
            }
        });

        return convertView;
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.system_message_withlist_inner_main;
    }

//    public void onEvent(AniCareMessage msg) {
//        AniCareLogger.log("onEvent! : " + msg);
//        mLinearListAdapter.add(new MayKnowObject(this, msg));
//    }

    public class MayKnowObject extends DefaultListObject {

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
            setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(LinearListView parent, View view, int position, ListObject object) {
//                    Toast.makeText(getContext(), "Click on " + msg.getSender(), Toast.LENGTH_SHORT).show();
                    AniCareMessage msg = new Gson().fromJson(object.getObjectId(), AniCareMessage.class);

                    if (msg.getType().getValue() == AniCareMessage.Type.SYSTEM.getValue()
                            && msg.getCommType().getValue() == AniCareMessage.CommType.ACCEPT.getValue()) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", msg.getUserPhone(), null));
                        getContext().startActivity(intent);
                    }
                }
            });

            //OnItemSwipeListener
            setOnItemSwipeListener(new OnItemSwipeListener() {
                @Override
                public void onItemSwipe(ListObject object, boolean dismissRight) {
                    final AniCareMessage msg = new Gson().fromJson(object.getObjectId(), AniCareMessage.class);

                    if (msg.getCommType().getValue() == AniCareMessage.CommType.REQUEST.getValue()) {
                        new AlertDialog.Builder(getContext())
                                .setMessage("Do you really want to reject the request?")
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        AniCareMessage responseMsg = new AniCareMessage();
                                        AniCareUser me = mAniCareService.getCurrentUser();
                                        responseMsg.setType(AniCareMessage.Type.SYSTEM);
                                        responseMsg.setCommType(AniCareMessage.CommType.REJECT);
                                        responseMsg.setReceiver(msg.getSender());
                                        responseMsg.setReceiverId(msg.getSenderId());
                                        responseMsg.setSender(me.getName());
                                        responseMsg.setSenderId(me.getId());
                                        responseMsg.setContent(me.getName() + " has rejected your request.");

                                        mAniCareService.sendMessage(responseMsg, new EntityCallback<AniCareMessage>() {
                                            @Override
                                            public void onCompleted(AniCareMessage entity) {
                                                msg.resolved();
                                                mAniCareService.updateMessageDB(msg.getId(), msg);
                                            }
                                        });
                                    }
                                })
                                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mLinearListAdapter.add(_this);
                                        mLinearListAdapter.notifyDataSetChanged();
                                    }
                                })
                                .show();
                    } else {
                        msg.resolved();
                        mAniCareService.updateMessageDB(msg.getId(), msg);
                    }

//                    Toast.makeText(getContext(), "Swipe on " + msg.getSender(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public String getObjectId() {
            return msg.toString();
        }
    }
}