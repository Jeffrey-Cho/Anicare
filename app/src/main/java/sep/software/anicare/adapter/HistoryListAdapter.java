package sep.software.anicare.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import sep.software.anicare.AniCareApp;
import sep.software.anicare.R;
import sep.software.anicare.model.CareHistory;
import sep.software.anicare.service.AniCareService;

/**
 * Created by Jeffrey on 2015. 6. 3..
 */
public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.HistoryViewHolder> {

    private List<CareHistory> historyList;
    private AniCareService mAniCareService; // For Using AniCareService.setPetImageIntro (Picasso library)
    private AniCareApp mAppContext;

    public HistoryListAdapter(List<CareHistory> historyList) {
        this.historyList = historyList;
        mAppContext = AniCareApp.getAppContext();
        mAniCareService = mAppContext.getAniCareService();
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public CareHistory getItem(int position) {
        return historyList.get(position);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder historyViewHolder, int i) {
        CareHistory historyItem = historyList.get(i);
        historyViewHolder.date.setText(historyItem.getRawDateTime());
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.history_card_view, viewGroup, false);

        return new HistoryViewHolder(itemView);
    }


    public static class HistoryViewHolder extends RecyclerView.ViewHolder {

        protected TextView date;

        public HistoryViewHolder(View v) {
            super(v);
            date = (TextView) v.findViewById(R.id.histroy_content);
        }
    }
}
