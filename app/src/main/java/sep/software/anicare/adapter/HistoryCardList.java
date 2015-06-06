package sep.software.anicare.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.prototypes.CardWithList;
import it.gmariotti.cardslib.library.prototypes.LinearListView;
import sep.software.anicare.R;
import sep.software.anicare.model.CareHistory;

/**
 * Created by apple on 2015. 6. 6..
 */
public class HistoryCardList extends CardWithList {

    public HistoryCardList(Context context) {
        super(context);
    }

    @Override
    protected CardHeader initCardHeader() {

        //Add Header
        CardHeader header = new CardHeader(getContext());
        //Add a popup menu. This method set OverFlow button to visible
        header.setTitle("History"); //should use R.string.
        return header;
    }

    @Override
    protected void initCard() {

//        setSwipeable(true);
//        setOnSwipeListener(new OnSwipeListener() {
//            @Override
//            public void onSwipe(Card card) {
//                Toast.makeText(getContext(), "Swipe on " + card.getCardHeader().getTitle(), Toast.LENGTH_SHORT).show();
//            }
//        });

        //Provide a custom view for the ViewStud EmptyView
        setEmptyViewViewStubLayoutId(R.layout.carddemo_extras_base_withlist_empty);
        setUseProgressBar(true);
    }



    @Override
    protected List<ListObject> initChildren() {
        //The default list is empty
        return null;
    }

    @Override
    public View setupChildView(int childPosition, ListObject object, View convertView, ViewGroup parent) {

        //Setup the elements inside each row
//        TextView dayText = (TextView) convertView.findViewById(R.id.carddemo_weather_dayName);
        TextView dayDate = (TextView) convertView.findViewById(R.id.carddemo_weather_dayDate); // History date
//        ImageView icon = (ImageView) convertView.findViewById(R.id.carddemo_weather_dayIcon);
        TextView minTempText = (TextView) convertView.findViewById(R.id.carddemo_weather_dayTempMin); // plus point
        TextView maxTempText = (TextView) convertView.findViewById(R.id.carddemo_weather_dayTempMax); // minus point
        TextView dayDescr = (TextView) convertView.findViewById(R.id.carddemo_weather_dayDescr); // other description

        HistoryObject historyObject= (HistoryObject)object;

        dayDate.setText(historyObject.date);
        if (historyObject.plusPoint > 0) {
            minTempText.setText(""+historyObject.plusPoint);
        } else {
            maxTempText.setText(""+historyObject.minusPoint);
        }
        dayDescr.setText("bla bla bla~~~");

        return convertView;
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.history_card_inner_main;
    }


    public void updateHistory(List<CareHistory> historyList) {

        ArrayList<HistoryObject> objs = new ArrayList<HistoryObject>();

        for (CareHistory historyItem:historyList){
            HistoryObject historyObject = new HistoryObject(this);
            historyObject.date = historyItem.getRawDateTime();
            historyObject.plusPoint = 200; // need to modify
            objs.add(historyObject);
        }
        getLinearListAdapter().addAll(objs);
        //updateProgressBar(true,true);
    }


    // -------------------------------------------------------------
    // HistoryObject
    // -------------------------------------------------------------

    public class HistoryObject extends DefaultListObject {

//        public DayForecast mDayForecast;
        public String date;
        public int plusPoint = 0;
        public int minusPoint = 0;

        public HistoryObject(Card parentCard) {
            super(parentCard);
//            mDayForecast = forecast;
            init();
        }

        private void init() {
//            setOnItemClickListener(new OnItemClickListener() {
//                @Override
//                public void onItemClick(LinearListView parent, View view, int position, ListObject object) {
//                    Toast.makeText(getContext(), "Click on " + mDayForecast.getStringDate(), Toast.LENGTH_SHORT).show();
//                }
//            });
        }

    }
}
