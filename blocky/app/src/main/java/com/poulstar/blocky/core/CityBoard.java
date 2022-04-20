package com.poulstar.blocky.core;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;

import com.poulstar.blocky.core.blocks.EmptyBlock;
import com.poulstar.blocky.core.components.BlockListener;

import java.util.ArrayList;

public class CityBoard implements View.OnClickListener {
    protected int xSize;
    protected int ySize;
    protected AbstractBlock[][] blocks;
    protected ImageView[][] imageBlocks;
    protected BlockListener blockListener;
    private float maxWeight = 6.25f;

    public CityBoard(int xSize, int ySize) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.blocks = new AbstractBlock[ySize][xSize];
        this.imageBlocks = new ImageView[ySize][xSize];
        this.maxWeight = 100f / (xSize * ySize);
    }

    public void setBlockListener(BlockListener blockListener) {
        this.blockListener = blockListener;
    }

    public TableRow[] createGameBoard(Context appContext) {
        ArrayList<TableRow> rows = new ArrayList<>();
        TableRow.LayoutParams imageParams = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        for(int y = 0; y < this.ySize; y++) {
            TableRow row = new TableRow(appContext);
            for(int x = 0; x < this.xSize; x++) {
                ImageView imageView = new ImageView(appContext);
                imageView.setLayoutParams(imageParams);
                row.addView(imageView);
                this.imageBlocks[y][x] = imageView;
                this.emptyBlock(x, y);

                // Make one dimensional index from two dimensional array
                imageView.setId((y * this.ySize) + x);
                imageView.setOnClickListener(this);
            }
            rows.add(row);
        }
        return rows.toArray(new TableRow[0]);
    }

    public void clearBoard() {
        for(int y = 0; y < this.ySize; y++) {
            for(int x = 0; x < this.xSize; x++) {
                this.emptyBlock(x, y);
            }
        }
    }

    public void replaceBlock(AbstractBlock oldBlock, AbstractBlock newBlock) {
        int[] position = oldBlock.getPosition();
        this.setBlock(position[0], position[1], newBlock);
    }

    public void emptyBlock(int x, int y) {

        this.setBlock(x, y, new EmptyBlock());
    }

    public void setBlock(int x, int y, AbstractBlock block) {
        block.setPosition(x, y);
        this.blocks[y][x] = block;
        this.imageBlocks[y][x].setImageResource(block.getImageResourceId());
        if(this.blockListener != null) {
            this.blockListener.updated(this.blocks[y][x]);
        }
    }

    public int[] getSize() {
        return new int[]{this.xSize, this.ySize};
    }

    public AbstractBlock[][] getBlocks() {
        return blocks;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        // Convert one dimensional index to two dimensional indexes
        int y = id / this.ySize;
        int x = id - (y * this.ySize);
        if(this.blockListener != null) {
            this.blockListener.selected(this.blocks[y][x]);
        }
    }

    public float getMaxWeight() {
        return maxWeight;
    }
}
