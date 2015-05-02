package sep.software.anicare.fragment;

import sep.software.anicare.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ListFriendFragment extends AniCareFragment {
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
		View rootView = inflater.inflate(R.layout.fragment_list_friend, container, false);

        return rootView;
    }

}
