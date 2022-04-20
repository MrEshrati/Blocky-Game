package com.poulstar.blocky.core.components;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.poulstar.blocky.R;
import com.poulstar.blocky.core.AbstractBlock;
import com.poulstar.blocky.core.AbstractMetric;

public class BlockListAdapter extends RecyclerView.Adapter<BlockListViewHolder> {

    private AbstractBlock[] blocks;
    private final View.OnClickListener clickListener;

    public BlockListAdapter(AbstractBlock[] blocks, View.OnClickListener listener) {
        this.blocks = blocks;
        this.clickListener = listener;
    }

    public AbstractBlock getBlockByView(View view) {
        final int id = view.getId();
        return this.blocks[id];
    }

    @NonNull
    @Override
    public BlockListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.block, parent, false);
        return new BlockListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlockListViewHolder holder, int position) {
        holder.getContainer().setId(position);
        holder.getContainer().setOnClickListener(this.clickListener);
        holder.getImgIcon().setImageResource(this.blocks[position].getImageResourceId());
        holder.getLblTitle().setText(holder.getImgIcon().getContext().getString(this.blocks[position].getNameResourceId()));
        holder.getLblPrice().setText(String.valueOf(this.blocks[position].getPrice()));
        holder.getLblPositive().setText(blocks[position].getPositiveEffects());
        holder.getLblNegative().setText(blocks[position].getNegativeEffects());
    }

    @Override
    public int getItemCount() {
        return this.blocks.length;
    }
}
