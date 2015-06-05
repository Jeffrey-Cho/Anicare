package sep.software.anicare.fragment;

import android.app.Activity;
import android.content.Context;
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
import sep.software.anicare.R;

/**
 * Created by apple on 2015. 6. 5..
 */
public class GoogleKnowwithList extends CardWithList {

    public GoogleKnowwithList(Context context) {
        super(context);
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
                    subTitle.setText("To see posts from friends and family,add them.");  //Should use strings.xml
                }
            }
        };
        header.setTitle("You may know"); //should use strings.xml
        return header;
    }

    @Override
    protected void initCard() {

    }

    @Override
    protected List<ListObject> initChildren() {
        //Init the list
        List<ListObject> mObjects = new ArrayList<ListObject>();

        //Add an object to the list
        MayKnowObject s1 = new MayKnowObject(this);
        s1.name = "정지현";
        s1.common = "안녕하세요? 반갑습니다. 맡기고 싶은데요...";
        s1.url = "https://lh5.googleusercontent.com/-squZd7FxR8Q/UyN5UrsfkqI/AAAAAAAAbAo/VoDHSYAhC_E/s54/new%2520profile%2520%25282%2529.jpg";
        mObjects.add(s1);

        //Add an object to the list
        MayKnowObject s2 = new MayKnowObject(this);
        s2.name = "유홍근";
        s2.common = "유홍근님이 당신에게 펫을 맡기고 싶어 합니다.";
        s2.url = "https://lh5.googleusercontent.com/-squZd7FxR8Q/UyN5UrsfkqI/AAAAAAAAbAo/VoDHSYAhC_E/s54/new%2520profile%2520%25282%2529.jpg";
        mObjects.add(s2);

        return mObjects;
    }

    @Override
    public View setupChildView(int childPosition, ListObject object, View convertView, ViewGroup parent) {

        //Setup the ui elements inside the item
        TextView textViewName = (TextView) convertView.findViewById(R.id.carddemo_know_name);
        TextView textViewPeople = (TextView) convertView.findViewById(R.id.carddemo_know_common);
        ImageView imagePeople = (ImageView) convertView.findViewById(R.id.carddemo_know_image);
        TextView addText = (TextView) convertView.findViewById(R.id.carddemo_know_add);


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
                Toast.makeText(getContext(), "Add Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.carddemo_googleknow_withlist_inner_main;
    }

    public class MayKnowObject extends DefaultListObject {

        public String name;
        public String common;
        public String url;

        public MayKnowObject(Card parentCard) {
            super(parentCard);
            init();
        }

        private void init() {
            //OnClick Listener
            setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(LinearListView parent, View view, int position, ListObject object) {
                    Toast.makeText(getContext(), "Click on " + getObjectId(), Toast.LENGTH_SHORT).show();
                }
            });

            //OnItemSwipeListener
            setOnItemSwipeListener(new OnItemSwipeListener() {
                @Override
                public void onItemSwipe(ListObject object, boolean dismissRight) {
                    Toast.makeText(getContext(), "Swipe on " + object.getObjectId(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public String getObjectId() {
            return name;
        }
    }
}
