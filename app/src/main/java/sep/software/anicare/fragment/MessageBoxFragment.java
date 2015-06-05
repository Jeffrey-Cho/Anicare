package sep.software.anicare.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.view.CardView;
import sep.software.anicare.R;
import sep.software.anicare.adapter.ReceivedMessageList;
import sep.software.anicare.adapter.SendedMessageList;
import sep.software.anicare.adapter.SystemMessageList;
import sep.software.anicare.model.AniCareMessage;

/**
 * Created by Jeffrey on 2015. 6. 5..
 */
public class MessageBoxFragment extends AniCareFragment {

    private final static String TAG = CardTestFragment.class.getCanonicalName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_message_box, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initCard();
    }

    private void initCard() {

        //List<AniCareMessage> totalMsg = mAniCareService.listMessage(); // does not work properly now. we use fake function.

        List<AniCareMessage> totalMsg = generateTempMessages(); // dummpy fake function

        List<AniCareMessage> systemMsg = new ArrayList<AniCareMessage>();
        List<AniCareMessage> receivedMsg = new ArrayList<AniCareMessage>();
        List<AniCareMessage> sendedMsg = new ArrayList<AniCareMessage>();

        for (AniCareMessage msg: totalMsg) {
            if (msg.getRawType() == 0) {
                systemMsg.add(msg);
            } else {
                if (true) { // for temp
                    receivedMsg.add(msg);
                } else {
                    sendedMsg.add(msg);
                }
            }
        }


        // System Messages
        SystemMessageList systemCard= new SystemMessageList(getActivity(), systemMsg);
        systemCard.init();

        CardView systemCardview = (CardView) getActivity().findViewById(R.id.system_message_cardview);
        systemCardview.setCard(systemCard);

        // Received Messages
        ReceivedMessageList receivedCard = new ReceivedMessageList(getActivity(), receivedMsg);
        receivedCard.init();

        CardView receivedCardview = (CardView) getActivity().findViewById(R.id.received_message_cardview);
        receivedCardview.setCard(receivedCard);

        // Sended Messages
        SendedMessageList sendCard= new SendedMessageList(getActivity(), sendedMsg);
        sendCard.init();

        CardView sendCardview = (CardView) getActivity().findViewById(R.id.send_message_cardview);
        sendCardview.setCard(sendCard);
    }

    /*
    *  temp message generate function, this will remove when the integration is done.
    */
    private List<AniCareMessage> generateTempMessages() {

        List<AniCareMessage> templist = new ArrayList<AniCareMessage>(10);

        AniCareMessage list1 = new AniCareMessage();
        list1.setSender("System");
        list1.setContent("이종웅님이 요청을 보내셨습니다. 수락하시겠습니까?");
        list1.setRawType(0);

        AniCareMessage list2 = new AniCareMessage();
        list2.setSender("System");
        list2.setContent("이창훈님이 요청을 보내셨습니다. 수락하시겠습니까?");
        list2.setRawType(0);

        AniCareMessage list3 = new AniCareMessage();
        list3.setSender("System");
        list3.setContent("우충기님이 요청을 보내셨습니다. 수락하시겠습니까?");
        list3.setRawType(0);

        AniCareMessage list4 = new AniCareMessage();
        list4.setSender("정지현");
        list4.setContent("강아지도 맡아주나요?");
        list4.setRawType(1);

        AniCareMessage list5 = new AniCareMessage();
        list5.setSender("조성래");
        list5.setContent("메르스는 언제 끝나나요?");
        list5.setRawType(1);

        AniCareMessage list6 = new AniCareMessage();
        list6.setSender("성경일");
        list6.setContent("언제 가능하시나요?");
        list6.setRawType(1);

        AniCareMessage list7 = new AniCareMessage();
        list7.setSender("유홍근");
        list7.setContent("저희 동네 아시나요?");
        list7.setRawType(1);

        AniCareMessage list8 = new AniCareMessage();
        list8.setSender("백종문");
        list8.setContent("계획은 세우셨나요?");
        list8.setRawType(1);

        AniCareMessage list9 = new AniCareMessage();
        list9.setSender("카이스트");
        list9.setContent("졸업 요청 부탁드립니다.");
        list9.setRawType(1);

        AniCareMessage list10 = new AniCareMessage();
        list10.setSender("System");
        list10.setContent("xxx님이 요청을 보내셨습니다. 수락하시겠습니까?");
        list10.setRawType(0);

        templist.add(list1);
        templist.add(list2);
        templist.add(list3);
        templist.add(list4);
        templist.add(list5);
        templist.add(list6);
        templist.add(list7);
        templist.add(list8);
        templist.add(list9);
        templist.add(list10);

        return templist;
    }
}
