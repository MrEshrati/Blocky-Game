package com.poulstar.blocky.core;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poulstar.blocky.R;

import org.w3c.dom.Text;

public abstract class AbstractMetric {

    private TextView progressMetric;
    private LinearLayout.LayoutParams progressParams;
    protected float value = 1.0f;

    public abstract int getNameResourceId();
    public abstract int getIconResourceId();

    public AbstractMetric(){
        this.progressParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        this.progressParams.weight = 0;
    }

    public AbstractMetric(int value) {
        this.value = value;
    }

    public String getName(Context appContext) {
        return appContext.getString(this.getNameResourceId());
    }

    public void increaseValue(float value) {
        this.setValue(this.value + value);
    }

    public void decreaseValue(float value) {
        this.setValue(this.value - value);
    }

    public void setValue(float value) {
        if(value > 100) {
            value = 100;
        }else if(value < 0) {
            value = 0;
        }
        this.value = value;
        this.progressParams.weight = value;
        this.progressMetric.requestLayout();
    }

    public float getValue() {
        return this.value;
    }

    public View createLayout(Context appContext) {
        LayoutInflater inflater = (LayoutInflater) appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View metricLayout = inflater.inflate(R.layout.metric, null);
        TextView lblMetricName = metricLayout.findViewById(R.id.lblMetricName);
        ImageView imgMetricIcon = metricLayout.findViewById(R.id.imgMetricIcon);
        progressMetric = metricLayout.findViewById(R.id.progressMetric);
        progressMetric.setLayoutParams(this.progressParams);
        lblMetricName.setText(this.getName(appContext));
        imgMetricIcon.setImageResource(this.getIconResourceId());
        return metricLayout;
    }
}
