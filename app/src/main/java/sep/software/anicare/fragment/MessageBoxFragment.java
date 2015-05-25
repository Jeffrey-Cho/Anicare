package sep.software.anicare.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import sep.software.anicare.R;
import sep.software.anicare.adapter.PetListAdapter;
import sep.software.anicare.interfaces.ListCallback;
import sep.software.anicare.model.AniCarePet;
import sep.software.anicare.view.MessageDialog;

public class MessageBoxFragment extends AniCareFragment {

    Button testBtn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_message_box, container, false);

        testBtn = (Button)rootView.findViewById(R.id.fragment_message_box_btn);

        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String receiver = "To myself";
                String receiverId = mThisUser.getId();
                MessageDialog dialog = new MessageDialog(mThisActivity, receiver, receiverId);
                dialog.show();
            }
        });


        return rootView;
    }
}
