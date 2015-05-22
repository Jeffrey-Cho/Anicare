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

import java.util.List;

public class ListFriendFragment extends AniCareFragment {

    private static final String TAG = ListFriendFragment.class.getSimpleName();
    private PetListAdapter petList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAniCareService.listPet(0, mThisUser.getId(), new ListCallback<AniCarePet>() {
            @Override
            public void onCompleted(List<AniCarePet> list, int count) {
                petList = new PetListAdapter(list);
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
        recList.setAdapter(petList);

        return rootView;
    }



}
