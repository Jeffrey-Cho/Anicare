package sep.software.anicare.adapter;

import android.app.Activity;
import android.content.Context;
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
import sep.software.anicare.model.AniCareMessage;
import sep.software.anicare.model.AniCareUser;
import sep.software.anicare.service.AniCareService;
import sep.software.anicare.view.MessageDialog;

/**
 * Created by Jeffrey on 2015. 6. 5..
 */
public class ReceivedMessageList extends CardWithList {

    private final List<AniCareMessage> msgList;
    AniCareApp mAppContext;
    AniCareService mAniCareService;

    public ReceivedMessageList(Context context, List<AniCareMessage> receivedMsg) {
        super(context);
        this.msgList = receivedMsg;
        mAppContext = AniCareApp.getAppContext();
        mAniCareService = mAppContext.getAniCareService();
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
                    subTitle.setText("답장을 원할 경우 Replay 버튼을 눌러주세요.");  //Should use strings.xml
                }
            }
        };
        header.setTitle("받은 메세지"); //should use strings.xml
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
            tempObject.name = msg.getSender();
            tempObject.common = msg.getContent();
            tempObject.url = mAniCareService.getUserImageUrl(msg.getSenderId());
            mObjects.add(tempObject);
        }

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
                MessageDialog msgDial = new MessageDialog(getContext(), msg.getSender(), msg.getSenderId());
                msgDial.show();
            }
        });

        return convertView;
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.received_message_withlist_inner_main;
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
                    Toast.makeText(getContext(), "Swipe on " + object.getObjectId(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public String getObjectId() {
            return msg.toString();
        }
    }
}