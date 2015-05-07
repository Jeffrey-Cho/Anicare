package sep.software.anicare.fragment;

import sep.software.anicare.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ListFriendFragment extends AniCareFragment {

    TextView textView;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
		View rootView = inflater.inflate(R.layout.fragment_list_friend, container, false);
        textView = (TextView) rootView.findViewById(R.id.hello_world);

//        mAniCareService.listPet(0, mThisUser.getId(), new ListCallback<AniCarePet>() {
//            @Override
//            public void onCompleted(List<AniCarePet> list, int count) {
//                textView.setText(list.toString());
//            }
//        });




        return rootView;
    }

}
