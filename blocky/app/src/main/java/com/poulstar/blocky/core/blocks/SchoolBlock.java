package com.poulstar.blocky.core.blocks;

import com.poulstar.blocky.R;
import com.poulstar.blocky.core.AbstractBlock;
import com.poulstar.blocky.core.AbstractMetric;
import com.poulstar.blocky.core.GameEngine;
import com.poulstar.blocky.core.MetricTypes;

import java.util.HashMap;

public class SchoolBlock extends AbstractBlock {
    @Override
    public int getImageResourceId() {
        return R.drawable.school_block;
    }

    @Override
    public int getNameResourceId() {
        return R.string.block_school;
    }

    @Override
    public int getPrice() {
        return 900;
    }

    @Override
    public void calculateBlockEffects(GameEngine engine) {
        float maxWeight = engine.getCityBoard().getMaxWeight();
        this.effects =  new HashMap<AbstractMetric, Float>(){
            {
                put(engine.getMetrics().get(MetricTypes.KNOWLEDGE_METRIC), maxWeight / 2);
            }
        };
    }
}
