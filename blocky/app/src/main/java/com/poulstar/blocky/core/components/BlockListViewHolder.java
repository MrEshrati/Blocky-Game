package com.poulstar.blocky.core.components;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.poulstar.blocky.R;

public class BlockListViewHolder extends RecyclerView.ViewHolder {
    private final ImageView imgIcon;
    private final TextView lblTitle,lblPositive,lblNegative,lblPrice;
    private ViewGroup container;

    public BlockListViewHolder(@NonNull View itemView) {
        super(itemView);
        this.container = itemView.findViewById(R.id.layContainer);
        this.imgIcon = itemView.findViewById(R.id.imgIcon);
        this.lblTitle = itemView.findViewById(R.id.lblTitle);
        this.lblPositive = itemView.findViewById(R.id.lblPositive);
        this.lblNegative = itemView.findViewById(R.id.lblNegative);
        this.lblPrice = itemView.findViewById(R.id.lblPrice);
    }

    public ViewGroup getContainer() {
        return container;
    }

    public ImageView getImgIcon() {
        return imgIcon;
    }

    public TextView getLblTitle() {
        return lblTitle;
    }

    public TextView getLblPositive() {
        return lblPositive;
    }

    public TextView getLblNegative() {
        return lblNegative;
    }

    public TextView getLblPrice() {
        return lblPrice;
    }
}
