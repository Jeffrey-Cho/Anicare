package sep.software.anicare.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.prototypes.CardWithList;
import it.gmariotti.cardslib.library.prototypes.LinearListView;
import sep.software.anicare.AniCareApp;
import sep.software.anicare.R;
import sep.software.anicare.model.AniCareMessage;
import sep.software.anicare.service.AniCareService;

/**
 * Created by jeffrey on 2015. 6. 6..
 */
public class MyPointCard extends CardWithList {

    private int point;
    AniCareService mAniCareService;
    AniCareApp mAppContext;

    public MyPointCard(Activity activity, int point) {
        super(activity);
        this.point = point;
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
                    subTitle.setText("포인트를 이용하여 추후 상용 서비스를 이용할 수 있습니다.");  //Should use strings.xml
                }
            }
        };
        header.setTitle("Points"); //should use strings.xml
        return header;
    }

    @Override
    protected void initCard() {

    }

    @Override
    protected List<ListObject> initChildren() {
        List<ListObject> mObjects = new ArrayList<ListObject>(1);

        MayKnowObject mayKnowObject = new MayKnowObject(this);

        mayKnowObject.url = mAniCareService.getCurrentUser().getImageUrl();
        mayKnowObject.name = mAniCareService.getCurrentUser().getName();
        mayKnowObject.point = mAniCareService.getCurrentUser().getPoint();
        mayKnowObject.common = "당신의 점수는 "+mayKnowObject.point+"점입니다.";

        mObjects.add(mayKnowObject);

        return mObjects;
    }

    @Override
    public View setupChildView(int childPosition, ListObject object, View convertView, ViewGroup parent) {
        //Setup the ui elements inside the item
        TextView textViewName = (TextView) convertView.findViewById(R.id.carddemo_know_name);
        TextView textViewPeople = (TextView) convertView.findViewById(R.id.carddemo_know_common);
        ImageView imagePeople = (ImageView) convertView.findViewById(R.id.carddemo_know_image);


        //Retrieve the values from the object
        MayKnowObject stockObject = (MayKnowObject) object;
        textViewName.setText(stockObject.name);
        textViewPeople.setText(""+stockObject.common);

        Picasso.with(getContext()).setIndicatorsEnabled(false);
        Picasso.with(getContext()).load(stockObject.url)
                .into(imagePeople);

        return convertView;
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.mypoint_inner_main;
    }

    public class MayKnowObject extends DefaultListObject {

        public String name;
        public String common;
        public String url;
        public int point;

        public MayKnowObject(Card parentCard) {
            super(parentCard);
            init();
        }

        private void init() {
            //OnClick Listener
            setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(LinearListView parent, View view, int position, ListObject object) {
                    //Toast.makeText(getContext(), "Click on " + getObjectId(), Toast.LENGTH_SHORT).show();
                }
            });

//            //OnItemSwipeListener
//            setOnItemSwipeListener(new OnItemSwipeListener() {
//                @Override
//                public void onItemSwipe(ListObject object, boolean dismissRight) {
//                    Toast.makeText(getContext(), "Swipe on " + object.getObjectId(), Toast.LENGTH_SHORT).show();
//                }
//            });
        }

        @Override
        public String getObjectId() {
            return name;
        }
    }
}
