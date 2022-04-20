package com.poulstar.blocky.core.metrics;

import com.poulstar.blocky.R;
import com.poulstar.blocky.core.AbstractMetric;

public class HealthMetric extends AbstractMetric {

    @Override
    public int getNameResourceId() {
        return R.string.metric_health;
    }

    @Override
    public int getIconResourceId() {
        return R.drawable.healthmetric;
    }
}
