package org.pursuit.ar_wrld.perks;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.pursuit.ar_wrld.GameInformation;
import org.pursuit.ar_wrld.R;

public class PerkViewHolder extends RecyclerView.ViewHolder {

    private TextView perkNameTv;
    private ImageView perkImage;
    private TextView perkDescriptionTv;

    public PerkViewHolder(@NonNull View itemView) {
        super(itemView);
        findViews();
    }

    public void onBind(PerkInfo perkInfo, PerkListener perkListener){
        perkNameTv.setText(perkInfo.getPerkName());
        perkImage.setImageDrawable(perkInfo.getPerkImage());
        perkDescriptionTv.setText(perkInfo.getPerkDescription());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perkListener.savePerkToSharedPref(perkInfo.getWhichPerkFromInterface());
            }
        });
    }

    private void findViews() {
        perkNameTv = itemView.findViewById(R.id.perk_name);
        perkImage = itemView.findViewById(R.id.perk_image);
        perkDescriptionTv = itemView.findViewById(R.id.perk_description);
    }

}
