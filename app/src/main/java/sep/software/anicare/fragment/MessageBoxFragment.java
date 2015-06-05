package sep.software.anicare.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import sep.software.anicare.R;
import sep.software.anicare.adapter.MessageListAdapter;
import sep.software.anicare.model.AniCareMessage;

public class MessageBoxFragment extends AniCareFragment implements RecyclerItemClickListener.OnItemClickListener {

    private final static String TAG = MessageBoxFragment.class.getCanonicalName();

    private MessageListAdapter msgAdapter;
    private List<AniCareMessage> msgList = new ArrayList<AniCareMessage>();

    @Override
    public void onItemClick(View view, int position)
    {
        //do nothing for now
    }


    @Override
    public void onItemLongClick(View view, int position)
    {
        //do nothing for now
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        msgAdapter = new MessageListAdapter(msgList);

        // for testing
        AniCareMessage msg1 = new AniCareMessage();
        msg1.setContent("Test Purpose");
        msg1.setRawDateTime("2015-06-03");
        msg1.setRawType(0);
        msg1.setSender("Me");
        msg1.setReceiver("Tom");
        msgList.add(msg1);

        AniCareMessage msg2 = new AniCareMessage();
        msg2.setContent("Test Purpose2");
        msg2.setRawDateTime("2015-06-04");
        msg2.setRawType(1);
        msg2.setSender("Jane");
        msg2.setReceiver("Tom");
        msgList.add(msg2);

        //msgList = mAniCareService.listMessage();
        msgAdapter.notifyDataSetChanged();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_message_box, container, false);

        RecyclerView recList = (RecyclerView) rootView.findViewById(R.id.msg_List);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mThisActivity);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        recList.setAdapter(msgAdapter);

        recList.addOnItemTouchListener(new RecyclerItemClickListener(this.getActivity(), recList, this));

        //rootView.setBackgroundColor(getResources().getColor(R.color.anicare_hint));

        return rootView;
    }
}
