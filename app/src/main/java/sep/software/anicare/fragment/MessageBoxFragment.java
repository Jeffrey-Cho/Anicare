package sep.software.anicare.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardListView;
import sep.software.anicare.R;
import sep.software.anicare.adapter.MessageListAdapter;
import sep.software.anicare.adapter.PetListAdapter;
import sep.software.anicare.interfaces.ListCallback;
import sep.software.anicare.model.AniCareMessage;
import sep.software.anicare.model.AniCarePet;
import sep.software.anicare.view.MessageDialog;

public class MessageBoxFragment extends AniCareFragment {

    private final static String TAG = MessageBoxFragment.class.getCanonicalName();

    Button testBtn;

    private MessageListAdapter msgListAdapter;
    private List<AniCareMessage> msgList = new ArrayList<AniCareMessage>();
    private List<AniCarePet> petList = new ArrayList<AniCarePet>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        msgList = mAniCareService.listMessage();
        msgListAdapter = new MessageListAdapter(msgList);

        // for testing
        mAniCareService.listPet(0, mThisUser.getId(), new ListCallback<AniCarePet>() {
            @Override
            public void onCompleted(List<AniCarePet> list, int count) {
                petList.addAll(list);
            }
        });

        Log.d(TAG, "petList size: " + petList.size());

//     listMessage();

    }

    private void listMessage() {
//        mAppContext.showProgressDialog(mThisActivity);
//        msgList = mAniCareService.listMessage();
////        mAniCareService.listMessage(mThisUser.getId(), new ListCallback<AniCarePet>() {
////            @Override
////            public void onCompleted(List<AniCarePet> list, int count) {
////                msgList.addAll(list);
////                msgListAdapter.notifyDataSetChanged();
////                mAppContext.dismissProgressDialog();
////            }
////        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_message_box, container, false);

        ArrayList<Card> cards = new ArrayList<Card>();

        //Create a Card
        Card card = new Card(mAppContext);
        //Create a CardHeader
        CardHeader header = new CardHeader(mAppContext);
        //Add Header to card
        card.addCardHeader(header);

        for (int i = 0; i< 100;i++ ) {
            card.setTitle("Test+"+i);
            cards.add(card);
        }

        Log.d(TAG, "Card list size: " + cards.size());

        for (int i = 0; i< 100;i++ ) {
            Log.d(TAG, cards.get(i).getTitle());
        }

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(),cards);

        CardListView listView = (CardListView) getActivity().findViewById(R.id.message_list);

        if (listView!=null){
            listView.setAdapter(mCardArrayAdapter);
        }

//        testBtn = (Button)rootView.findViewById(R.id.fragment_message_box_btn);
//
//        testBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String receiver = "To myself";
//                String receiverId = mThisUser.getId();
//                MessageDialog dialog = new MessageDialog(mThisActivity, receiver, receiverId);
//                dialog.show();
//            }
//        });


        return rootView;
    }
}
