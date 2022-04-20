package com.poulstar.blocky.core.metrics;

import com.poulstar.blocky.R;
import com.poulstar.blocky.core.AbstractMetric;

public class PopulationMetric extends AbstractMetric {
    @Override
    public int getNameResourceId() {
        return R.string.metric_population;
    }

    @Override
    public int getIconResourceId() {
        return R.drawable.baseline_groups_white_36;
    }
}
