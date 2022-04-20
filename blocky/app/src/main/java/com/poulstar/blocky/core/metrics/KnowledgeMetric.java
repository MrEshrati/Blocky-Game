package com.poulstar.blocky.core.metrics;

import com.poulstar.blocky.R;
import com.poulstar.blocky.core.AbstractMetric;

public class KnowledgeMetric extends AbstractMetric {
    @Override
    public int getNameResourceId() {
        return R.string.metric_knowledge;
    }

    @Override
    public int getIconResourceId() {
        return R.drawable.baseline_school_white_36;
    }
}
