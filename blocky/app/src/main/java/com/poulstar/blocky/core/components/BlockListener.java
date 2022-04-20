package com.poulstar.blocky.core.components;

import android.view.View;

import com.poulstar.blocky.core.AbstractBlock;

public interface BlockListener {
    public void selected(AbstractBlock block);
    public void updated(AbstractBlock block);
}
