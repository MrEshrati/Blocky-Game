package com.poulstar.blocky.core.metrics;

import com.poulstar.blocky.R;
import com.poulstar.blocky.core.AbstractMetric;

public class FinancialMetric extends AbstractMetric {

    @Override
    public int getNameResourceId() {
        return R.string.metric_financial;
    }

    @Override
    public int getIconResourceId() {
        return R.drawable.wealthmetric;
    }
}
