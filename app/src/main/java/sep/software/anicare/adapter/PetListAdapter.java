package sep.software.anicare.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import sep.software.anicare.model.AniCarePet;

/**
 * Created by hongkunyoo on 15. 5. 22..
 */
public class PetListAdapter extends ArrayAdapter<AniCarePet> {
    public PetListAdapter(Context context) {
        super(context, 0);
    }


}
