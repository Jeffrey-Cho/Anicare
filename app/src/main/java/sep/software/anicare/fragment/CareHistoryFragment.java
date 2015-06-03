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
import sep.software.anicare.adapter.HistoryListAdapter;
import sep.software.anicare.model.CareHistory;

public class CareHistoryFragment extends AniCareFragment implements RecyclerItemClickListener.OnItemClickListener{

    private final static String TAG = CareHistoryFragment.class.getCanonicalName();

    private HistoryListAdapter historyAdapter;
    private List<CareHistory> historyList = new ArrayList<CareHistory>();

    @Override
    public void onItemClick(View view, int position) {
        // do nothing for now
    }

    @Override
    public void onItemLongClick(View view, int position) {
        // do nothing for now
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        historyAdapter = new HistoryListAdapter(historyList);


        CareHistory history1 = new CareHistory();
        history1.setRawDateTime("2015-06-04");
        historyList.add(history1);

        CareHistory history2 = new CareHistory();
        history2.setRawDateTime("2015-06-07");
        historyList.add(history2);

        //historyList = mAniCareService.listHistory();
        historyAdapter.notifyDataSetChanged();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        RecyclerView recList = (RecyclerView) rootView.findViewById(R.id.history_list);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mThisActivity);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        recList.setAdapter(historyAdapter);

        recList.addOnItemTouchListener(new RecyclerItemClickListener(this.getActivity(), recList, this));


        return rootView;
    }


}
