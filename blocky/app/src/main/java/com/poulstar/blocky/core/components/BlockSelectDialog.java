package com.poulstar.blocky.core.components;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.poulstar.blocky.R;
import com.poulstar.blocky.core.AbstractBlock;

public class BlockSelectDialog extends Dialog implements View.OnClickListener {

    protected ScrollView scrollView;
    protected RecyclerView lstContainer;
    protected BlockListAdapter blocksAdapter;
    protected View.OnClickListener selectListener;

    public BlockSelectDialog(@NonNull Activity activity, AbstractBlock[] blocks) {
        super(activity);
        this.blocksAdapter = new BlockListAdapter(blocks, this);
    }

    public void setOnSelectListener(View.OnClickListener selectListener) {
        this.selectListener = selectListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.block_select);
        this.scrollView = findViewById(R.id.scrollView);
        this.lstContainer = findViewById(R.id.lstContainer);
        this.lstContainer.setLayoutManager(new LinearLayoutManager(getContext()));
        this.lstContainer.setAdapter(this.blocksAdapter);
//        for(AbstractBlock block : this.blocks) {
//            View view = getLayoutInflater().inflate(R.layout.block, this.lstContainer);
//            ImageView imgIcon = view.findViewById(R.id.imgIcon);
//            TextView lblTitle = view.findViewById(R.id.lblTitle);
//            TextView lblPositive = view.findViewById(R.id.lblPositive);
//            TextView lblNegative = view.findViewById(R.id.lblNegative);
//            TextView lblPrice = view.findViewById(R.id.lblPrice);
//            imgIcon.setImageResource(block.getImageResourceId());
//            lblTitle.setText(getContext().getString(block.getNameResourceId()));
//            lblPrice.setText(String.valueOf(block.getPrice()));
//        }
    }

    public AbstractBlock getSelectedBlock(View v){
        return this.blocksAdapter.getBlockByView(v);
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        this.hide();
        if(this.selectListener != null) {
            this.selectListener.onClick(v);
        }
    }
}
