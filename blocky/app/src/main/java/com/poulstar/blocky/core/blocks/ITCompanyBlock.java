package com.poulstar.blocky.core.blocks;

import com.poulstar.blocky.R;
import com.poulstar.blocky.core.AbstractBlock;
import com.poulstar.blocky.core.AbstractMetric;
import com.poulstar.blocky.core.GameEngine;
import com.poulstar.blocky.core.MetricTypes;

import java.util.HashMap;

public class ITCompanyBlock extends AbstractBlock {
    @Override
    public int getImageResourceId() {
        return R.drawable.it_company_block;
    }

    @Override
    public int getNameResourceId() {
        return R.string.block_it_company;
    }

    @Override
    public int getPrice() {
        return 2500;
    }

    @Override
    public void calculateBlockEffects(GameEngine engine) {
        float maxWeight = engine.getCityBoard().getMaxWeight();
        this.effects =  new HashMap<AbstractMetric, Float>(){
            {
                put(engine.getMetrics().get(MetricTypes.FINANCIAL_METRIC), 3 * (maxWeight / 2));
                put(engine.getMetrics().get(MetricTypes.SAFETY_METRIC), -2f);
            }
        };
    }
}
