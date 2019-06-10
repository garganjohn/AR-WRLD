package org.pursuit.ar_wrld.perks;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.pursuit.ar_wrld.R;

import java.util.List;

public class PerkAdapter extends RecyclerView.Adapter<PerkViewHolder> {
    private List<PerkInfo> perkInfoList;
    private PerkListener perkListener;

    public PerkAdapter(List<PerkInfo> perkInfoList, PerkListener perkListener){
        this.perkInfoList = perkInfoList;
        this.perkListener = perkListener;
    }

    @NonNull
    @Override
    public PerkViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.perk_viewholder, viewGroup, false);
        return new  PerkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PerkViewHolder viewPagerForPerk, int i) {
        viewPagerForPerk.onBind(perkInfoList.get(i), perkListener);
    }

    @Override
    public int getItemCount() {
        return perkInfoList.size();
    }
}
