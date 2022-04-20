package com.poulstar.blocky.core;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractBlock implements Cloneable {
    protected int x,y;
    protected String positiveEffects, negativeEffects;
    protected HashMap<AbstractMetric, Float> effects;

    public abstract int getImageResourceId();
    public abstract int getNameResourceId();
    public abstract int getPrice();
    protected abstract void calculateBlockEffects(GameEngine engine);

    public void calculateEffects(GameEngine engine){
        this.calculateBlockEffects(engine);
        this.positiveEffects = "";
        this.negativeEffects = "";
        for(Map.Entry<AbstractMetric, Float> entry : this.effects.entrySet()) {
            if(entry.getValue() >= 0) {
                this.positiveEffects = "+"+entry.getValue()+" "+entry.getKey().getName(engine.getContext())+" ";
            } else {
                this.negativeEffects = "-"+entry.getValue()+" "+entry.getKey().getName(engine.getContext())+" ";
            }
        }
    }


    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int[] getPosition() {
        return new int[]{this.x, this.y};
    }

    public HashMap<AbstractMetric, Float> getEffects() {
        return effects;
    }

    public String getPositiveEffects() {
        return positiveEffects;
    }

    public String getNegativeEffects() {
        return negativeEffects;
    }

    @Override
    public AbstractBlock clone() {
        try {
            return (AbstractBlock) super.clone();
        }catch (Exception e) {
            return null;
        }
    }
}
