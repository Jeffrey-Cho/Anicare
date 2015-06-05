package sep.software.anicare.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.view.CardView;
import sep.software.anicare.R;
import sep.software.anicare.adapter.MessageListAdapter;
import sep.software.anicare.model.AniCareMessage;

/**
 * Created by Jeffrey on 2015. 6. 5..
 */
public class CardTestFragment extends AniCareFragment {

    private final static String TAG = CardTestFragment.class.getCanonicalName();

    private MessageListAdapter msgAdapter;
    private List<AniCareMessage> msgList = new ArrayList<AniCareMessage>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        msgAdapter = new MessageListAdapter(msgList);
//
//        // for testing
//        AniCareMessage msg1 = new AniCareMessage();
//        msg1.setContent("Test Purpose");
//        msg1.setRawDateTime("2015-06-03");
//        msg1.setRawType(0);
//        msg1.setSender("Me");
//        msg1.setReceiver("Tom");
//        msgList.add(msg1);
//
//        AniCareMessage msg2 = new AniCareMessage();
//        msg2.setContent("Test Purpose2");
//        msg2.setRawDateTime("2015-06-04");
//        msg2.setRawType(1);
//        msg2.setSender("Jane");
//        msg2.setReceiver("Tom");
//        msgList.add(msg2);
//
//        AniCareMessage msg3 = new AniCareMessage();
//        msg3.setContent("Test Purpose2");
//        msg3.setRawDateTime("2015-06-04");
//        msg2.setRawType(1);
//        msg3.setSender("Sam");
//        msg3.setReceiver("Jeff");
//        msgList.add(msg3);
//
//        //msgList = mAniCareService.listMessage();
//        msgAdapter.notifyDataSetChanged();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_test_card, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initCard();
    }

    private void initCard() {

        GoogleKnowwithList systemCard= new GoogleKnowwithList(getActivity());
        systemCard.init();

        CardView systemCardview = (CardView) getActivity().findViewById(R.id.system_message_cardview);
        systemCardview.setCard(systemCard);


        GoogleKnowwithList receivedCard = new GoogleKnowwithList(getActivity());
        receivedCard.init();

        //Set card in the cardView
        CardView receivedCardview = (CardView) getActivity().findViewById(R.id.received_message_cardview);
        receivedCardview.setCard(receivedCard);

        GoogleKnowwithList sendCard= new GoogleKnowwithList(getActivity());
        sendCard.init();

        //Set card in the cardView
        CardView sendCardview = (CardView) getActivity().findViewById(R.id.send_message_cardview);
        sendCardview.setCard(sendCard);
    }
}
