package com.poulstar.blocky.core.blocks;

import com.poulstar.blocky.R;
import com.poulstar.blocky.core.AbstractBlock;
import com.poulstar.blocky.core.AbstractMetric;
import com.poulstar.blocky.core.GameEngine;
import com.poulstar.blocky.core.MetricTypes;

import java.util.HashMap;

public class EmptyBlock extends AbstractBlock {

    @Override
    public int getImageResourceId() {
        return R.drawable.empty_block;
    }

    @Override
    public int getNameResourceId() {
        return R.string.block_empty;
    }

    @Override
    public int getPrice() {
        return 0;
    }

    @Override
    public void calculateBlockEffects(GameEngine engine) {
        this.effects =  new HashMap<AbstractMetric, Float>(){};
    }
}
