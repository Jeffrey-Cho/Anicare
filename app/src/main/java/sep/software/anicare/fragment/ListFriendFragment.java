package sep.software.anicare.fragment;

import sep.software.anicare.R;
import sep.software.anicare.activity.PetDetailActivity;
import sep.software.anicare.adapter.PetListAdapter;
import sep.software.anicare.interfaces.ListCallback;
import sep.software.anicare.model.AniCarePet;
import sep.software.anicare.util.AniCareLogger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListFriendFragment extends AniCareFragment implements RecyclerItemClickListener.OnItemClickListener {

    private static final String TAG = ListFriendFragment.class.getSimpleName();

    private PetListAdapter petListAdapter;
    private List<AniCarePet> petList = new ArrayList<AniCarePet>();

    private int page = 0;
    private int mode = 3;
    private boolean isAdding = false;

    @Override
    public void onItemClick(View view, int position)
    {
        AniCarePet selectedPet;
        selectedPet = petListAdapter.getItem(position);

        Toast toast = Toast.makeText(mThisActivity, selectedPet.getName(), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        Intent intent = new Intent();
        intent.setClass(mThisActivity, PetDetailActivity.class);
        intent.putExtra("petInfo", selectedPet);
        startActivity(intent);
    }


    @Override
    public void onItemLongClick(View view, int position)
    {
        //do nothing for now
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        petListAdapter = new PetListAdapter(petList);
        refreshList(mode);
    }

    public void refreshList(int _mode) {
        this.mode = _mode;
        this.page = 0;
        petList.clear();
        petListAdapter.notifyDataSetChanged();
        mAppContext.showProgressDialog(mThisActivity);
        isAdding = true;
        mAniCareService.listPet(page, _mode, mThisUser.getId(), new ListCallback<AniCarePet>() {
            @Override
            public void onCompleted(List<AniCarePet> list, int count) {
                petList.addAll(list);
                petListAdapter.notifyDataSetChanged();
                mAppContext.dismissProgressDialog();
                isAdding = false;
            }
        });
    }


	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
		View rootView = inflater.inflate(R.layout.fragment_list_friend, container, false);

        RecyclerView recList = (RecyclerView) rootView.findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        final LinearLayoutManager llm = new LinearLayoutManager(mThisActivity);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        recList.setAdapter(petListAdapter);

        recList.addOnItemTouchListener(new RecyclerItemClickListener(this.getActivity(), recList, this));

        rootView.setBackgroundColor(getResources().getColor(R.color.anicare_hint));

        recList.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // Add more items when grid reaches bottom
                int position = llm.findLastVisibleItemPosition();
                int totalItemCount = llm.getItemCount();
                if (position >= totalItemCount-5 && !isAdding) {
                    addNextItem();
                }
            }
        });

        return rootView;
    }

    private void addNextItem() {
        mAppContext.showProgressDialog(mThisActivity);
        isAdding = true;
        mAniCareService.listPet(++page, mode, mThisUser.getId(), new ListCallback<AniCarePet>() {
            @Override
            public void onCompleted(List<AniCarePet> list, int count) {
                petList.addAll(list);
                petListAdapter.notifyDataSetChanged();
                mAppContext.dismissProgressDialog();
                isAdding = false;
            }
        });
    }

}
