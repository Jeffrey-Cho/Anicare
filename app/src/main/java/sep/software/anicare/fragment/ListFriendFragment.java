package sep.software.anicare.fragment;

import sep.software.anicare.R;
import sep.software.anicare.adapter.PetListAdapter;
import sep.software.anicare.interfaces.ListCallback;
import sep.software.anicare.model.AniCarePet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class ListFriendFragment extends AniCareFragment {

    TextView textView;
    ListView petList;
    PetListAdapter petListAdapter;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
		View rootView = inflater.inflate(R.layout.fragment_list_friend, container, false);
        textView = (TextView) rootView.findViewById(R.id.hello_world);
        petList = (ListView) rootView.findViewById(R.id.list_friend_pet_list);

        petListAdapter = new PetListAdapter(mThisActivity);
        petList.setAdapter(petListAdapter);

        getListPet();


        return rootView;
    }

    private void getListPet() {
        mAniCareService.listPet(mThisUser.getId(), new ListCallback<AniCarePet>() {
            @Override
            public void onCompleted(List<AniCarePet> list, int count) {
                textView.setText(list.toString());


                petListAdapter.addAll(list);
            }
        });
    }

}
