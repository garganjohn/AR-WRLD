package org.pursuit.ar_wrld.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.pursuit.ar_wrld.R;

public class TopScoreViewHolder extends RecyclerView.ViewHolder {

    public TextView selectedUsername;
    public TextView selectedUserScore;
    public TextView selectedUserTitle;

    public TopScoreViewHolder(@NonNull View itemView) {
        super(itemView);

        selectedUsername = itemView.findViewById(R.id.selected_username);
        selectedUserTitle = itemView.findViewById(R.id.selected_usertitle);
        selectedUserScore = itemView.findViewById(R.id.select_userscore);
    }
}

