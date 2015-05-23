package sep.software.anicare.fragment;

import sep.software.anicare.R;
import sep.software.anicare.adapter.PetListAdapter;
import sep.software.anicare.interfaces.ListCallback;
import sep.software.anicare.model.AniCarePet;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ListFriendFragment extends AniCareFragment {

    private static final String TAG = ListFriendFragment.class.getSimpleName();
    private PetListAdapter petListAdapter;
    private List<AniCarePet> petList = new ArrayList<AniCarePet>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        petListAdapter = new PetListAdapter(petList);
        mAppContext.showProgressDialog(mThisActivity);
        mAniCareService.listPet(mThisUser.getId(), new ListCallback<AniCarePet>() {
            @Override
            public void onCompleted(List<AniCarePet> list, int count) {
                petList.addAll(list);
                petListAdapter.notifyDataSetChanged();
                mAppContext.dismissProgressDialog();
            }
        });
    }

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
		View rootView = inflater.inflate(R.layout.fragment_list_friend, container, false);

        RecyclerView recList = (RecyclerView) rootView.findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mThisActivity);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        recList.setAdapter(petListAdapter);

        return rootView;
    }



}
