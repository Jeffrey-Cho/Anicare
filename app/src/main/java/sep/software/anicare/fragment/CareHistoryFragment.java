package sep.software.anicare.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.view.CardView;
import sep.software.anicare.R;
import sep.software.anicare.adapter.HistoryCardList;
import sep.software.anicare.adapter.HistoryListAdapter;
import sep.software.anicare.adapter.MyPointCard;
import sep.software.anicare.model.AniCareMessage;
import sep.software.anicare.model.CareHistory;

public class CareHistoryFragment extends AniCareFragment {

    private final static String TAG = CareHistoryFragment.class.getCanonicalName();

    private HistoryCardList historyCard;
    //private List<CareHistory> historyList;
    private List<AniCareMessage> messageList;
    private int point;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //historyList = mAniCareService.listHistory();
        //historyList = fakeHistoryGenerator();
        messageList = mAniCareService.listMessage();
//        calculatePoint(messageList);
        point = mThisUser.getPoint();

        //setHasOptionsMenu(true);
    }

    private void calculatePoint(List<AniCareMessage> messageList) {
        List<AniCareMessage> list = messageList;

        for (AniCareMessage item:list) {
            Log.d(TAG, item.getSender()+ " " + item.getType().getValue() + " " + item.getCommType().getValue() +" " + item.getContent());
            if (item.getType().equals(AniCareMessage.Type.SYSTEM)) {
                if (item.isMine() && item.getCommType().equals(AniCareMessage.CommType.ACCEPT)) {
                    //mAniCareService.plsPoint(100);
                    point = point + 100;
                } else if (!item.isMine() && item.getCommType().equals(AniCareMessage.CommType.ACCEPT)) {
                    //mAniCareService.minPoint(100);
                    point = point - 100;
                }
            }

        }
    }

//    private List<CareHistory> fakeHistoryGenerator() {
//        List<CareHistory> fakeHistoryList = new ArrayList<CareHistory>(9);
//
//        CareHistory item1 = new CareHistory();
//        item1.setRawDateTime("2015/06/01");
//
//        CareHistory item2 = new CareHistory();
//        item2.setRawDateTime("2015/06/02");
//
//        CareHistory item3 = new CareHistory();
//        item3.setRawDateTime("2015/06/03");
//
//        CareHistory item4 = new CareHistory();
//        item4.setRawDateTime("2015/06/04");
//
//        CareHistory item5 = new CareHistory();
//        item5.setRawDateTime("2015/06/05");
//
//        CareHistory item6 = new CareHistory();
//        item6.setRawDateTime("2015/06/06");
//
//        CareHistory item7 = new CareHistory();
//        item7.setRawDateTime("2015/06/07");
//
//        CareHistory item8 = new CareHistory();
//        item8.setRawDateTime("2015/06/08");
//
//        CareHistory item9 = new CareHistory();
//        item9.setRawDateTime("2015/06/09");
//
//        fakeHistoryList.add(item1);
//        fakeHistoryList.add(item2);
//        fakeHistoryList.add(item3);
//        fakeHistoryList.add(item4);
//        fakeHistoryList.add(item5);
//        fakeHistoryList.add(item6);
//        fakeHistoryList.add(item7);
//        fakeHistoryList.add(item8);
//        fakeHistoryList.add(item9);
//
//
//        return fakeHistoryList;
//    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initCard();
        historyCard.updateHistory(messageList);
        //historyCard.updateProgressBar(false, false);
    }

    /**
     * This method builds a simple card
     */
    private void initCard() {

        //Point Card
        MyPointCard pointCard = new MyPointCard(getActivity(), point);
        pointCard.init();

        //Set card in the cardView
        CardView pointCardView = (CardView) getActivity().findViewById(R.id.pointcard);
        pointCardView.setCard(pointCard);

        //History Card
        historyCard= new HistoryCardList(getActivity());
        historyCard.init();

        //Set card in the cardView
        CardView historyCardView = (CardView) getActivity().findViewById(R.id.historycard);
        historyCardView.setCard(historyCard);
    }

}
