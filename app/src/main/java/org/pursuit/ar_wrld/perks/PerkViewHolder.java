package org.pursuit.ar_wrld.perks;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.pursuit.ar_wrld.R;

public class PerkViewHolder extends RecyclerView.ViewHolder {

    private TextView perkNameTv;
    private ImageView perkImage;
    private TextView perkDescriptionTv;

    public PerkViewHolder(@NonNull View itemView) {
        super(itemView);
        findViews();
    }

    public void onBind(PerkInfoModel perkInfoModel, PerkListener perkListener){
        perkNameTv.setText(perkInfoModel.getPerkName());
        perkImage.setImageDrawable(perkInfoModel.getPerkImage());
        perkDescriptionTv.setText(perkInfoModel.getPerkDescription());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perkListener.savePerkToSharedPref(perkInfoModel.getWhichPerkFromInterface());
            }
        });
    }

    private void findViews() {
        perkNameTv = itemView.findViewById(R.id.perk_name);
        perkImage = itemView.findViewById(R.id.perk_image);
        perkDescriptionTv = itemView.findViewById(R.id.perk_description);
    }

}
