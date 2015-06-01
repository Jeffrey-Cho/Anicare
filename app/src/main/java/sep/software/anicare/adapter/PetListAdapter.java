package sep.software.anicare.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import sep.software.anicare.AniCareApp;
import sep.software.anicare.R;
import sep.software.anicare.model.AniCarePet;
import sep.software.anicare.service.AniCareService;
import sep.software.anicare.view.DynamicHeightImageView;

/**
 * Created by Jeffrey on 2015. 5. 22..
 */
public class PetListAdapter extends RecyclerView.Adapter<PetListAdapter.PetListViewHolder> {

    private List<AniCarePet> petList;
    private AniCareService mAniCareService; // For Using AniCareService.setPetImageIntro (Picasso library)
    private AniCareApp mAppContext;

    public PetListAdapter(List<AniCarePet> petList) {
        this.petList = petList;
        mAppContext = AniCareApp.getAppContext();
        mAniCareService = mAppContext.getAniCareService();
    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    public AniCarePet getItem(int position) {
        return petList.get(position);
    }

    @Override
    public void onBindViewHolder(PetListViewHolder petViewHolder, int i) {
        AniCarePet petItem = petList.get(i);

        mAniCareService.setPetImageInto(petItem.getId(),petViewHolder.cardImage);
        petViewHolder.petName.setText(petItem.getName());
        petViewHolder.petLocation.setText(petItem.getLocation());
    }

    @Override
    public PetListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.pet_card_view, viewGroup, false);

        return new PetListViewHolder(itemView);
    }


    public static class PetListViewHolder extends RecyclerView.ViewHolder {

//        protected ImageView cardImage;
        protected DynamicHeightImageView cardImage;
        protected TextView petName;
        protected TextView petLocation;

        public PetListViewHolder(View v) {
            super(v);
//            cardImage = (ImageView) v.findViewById(R.id.pet_image);
            cardImage = (DynamicHeightImageView) v.findViewById(R.id.pet_image);
            petName =  (TextView) v.findViewById(R.id.pet_name);
            petLocation = (TextView)  v.findViewById(R.id.pet_location);
        }
    }

}
