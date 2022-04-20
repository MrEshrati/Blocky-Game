package com.poulstar.blocky.core;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TableRow;

import com.google.android.material.snackbar.Snackbar;
import com.poulstar.blocky.R;
import com.poulstar.blocky.core.blocks.EmptyBlock;
import com.poulstar.blocky.core.components.BlockListener;
import com.poulstar.blocky.core.components.BlockSelectDialog;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GameEngine implements BlockListener, View.OnClickListener {
    protected final HashMap<MetricTypes, AbstractMetric> metrics;
    protected final HashMap<BlockTypes, AbstractBlock> blocks;
    protected CityBoard cityBoard;
    private final BlockSelectDialog dialog;
    private final Context context;
    private final View rootView;
    private AbstractBlock lastSelectedBlock;
    private int money;
    private Timer gameTimer;
    private int gameRound, gameDay;
    private long startTime, roundDuration, dayDuration, gameTime;
    private int limitDays, limitMoney, requiredMoney;
    private boolean winner = true;
    private boolean running = false;
    private GameEngineListener listener;

    public GameEngine(Activity activity, View rootView, HashMap<MetricTypes, AbstractMetric> metrics,
                      HashMap<BlockTypes, AbstractBlock> blocks, CityBoard cityBoard) {
        this.metrics = metrics;
        this.cityBoard = cityBoard;
        this.blocks = blocks;
        this.cityBoard.setBlockListener(this);
        this.context = activity.getApplicationContext();
        this.rootView = rootView;
        dialog = new BlockSelectDialog(activity, this.blocks.values().toArray(new AbstractBlock[0]));
        dialog.setOnSelectListener(this);
        this.roundDuration = 5000;
        this.dayDuration = 20000;
        this.limitDays = 10;
        this.limitMoney = -5000;
        this.requiredMoney = 100000;
        calculateBlockEffectsAsync();
    }

    private void calculateBlockEffectsAsync() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            Log.e("EFFECT", "CALCULATING BLOCK EFFECTS");
            for(AbstractBlock block : this.blocks.values()) {
                block.calculateEffects(this);
            }
        });
    }

    public HashMap<MetricTypes, AbstractMetric> getMetrics() {
        return this.metrics;
    }

    public HashMap<BlockTypes, AbstractBlock> getBlocks() {
        return this.blocks;
    }

    public TableRow[] createGameBoard(Context appContext) {
        return this.cityBoard.createGameBoard(appContext);
    }

    public void setListener(GameEngineListener listener) {
        this.listener = listener;
    }

    public void updateMetrics() {
        this.resetMetrics();
        for(AbstractBlock[] rows : this.cityBoard.getBlocks()) {
            for(AbstractBlock block : rows) {
                if(block.getEffects() == null) {
                    continue;
                }
                for(Map.Entry<AbstractMetric, Float> entry : block.getEffects().entrySet()) {
                    Log.e("METRIC UPDATE", entry.getKey().getName(this.getContext()));
                    entry.getKey().increaseValue(entry.getValue());
                }
            }
        }
    }

    public void resetMetrics() {
        for(AbstractMetric metric : this.metrics.values()) {
            metric.setValue(1.0f);
        }
    }

    public void start() {
        this.stop();
        this.resetMetrics();
        this.winner = true;
        this.cityBoard.clearBoard();
        this.startTime = System.currentTimeMillis();
        this.gameRound = 1;
        this.gameDay = 1;
        this.gameTime = 0;
        this.gameTimer = new Timer();
        this.running = true;
        this.gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
//                Log.i("Game Engine", String.format("Time: %d, Round: %d, Day: %d", gameTime, gameRound, gameDay));
                if(isLimitConditionSatisfied()) {
                    listener.gameEnded(GameEngine.this);
                    stop();
                } else {
                    gameTime += 1000;
                    if(gameTime % roundDuration == 0) {
                        addRound();
                    }
                    if(gameTime >= dayDuration) {
                        addDay();
                    }
                    if(listener != null) {
                        listener.gameTick(GameEngine.this);
                    }
                }
            }
        }, 0, 1000);
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.submit(() -> {
//            while (true) {
//                try {
//                    Thread.sleep(1000);
//                    Log.e("Game Engine", "Round pass");
//                    gameTime += 1000;
//                    if(gameTime % roundDuration == 0) {
//                        addRound();
//                    }
//                    if(gameTime >= dayDuration) {
//                        addDay();
//                    }
//                    updateTexts(txtTime, txtRound, txtMoney);
//                } catch (InterruptedException e) {
//                    Log.e("Game Engine", "Game cycle error.");
//                }
//            }
//        });
    }

    public void stop() {
        this.running = false;
        this.dialog.hide();
        if(this.gameTimer != null) {
            this.gameTimer.cancel();
            this.gameTimer.purge();
            this.gameTimer = null;
        }
    }

    private boolean isLimitConditionSatisfied() {
        if(this.gameDay > this.limitDays) {
            if(this.money < this.requiredMoney) {
                winner = false;
            }
            return true;
        }
        if(this.money >= this.requiredMoney) {
            winner = true;
            return true;
        }
        if(this.money < this.limitMoney) {
            winner = false;
            return true;
        }
        return false;
    }

    public void addRound() {
        this.gameRound++;
        this.addMoney(this.calculateIncome() / (int) (dayDuration / roundDuration));
    }
    public void addDay() {
        this.gameDay++;
        this.gameTime = 0;
        Log.e("DAY-PASS", String.format("income: %d - outcome: %d", this.calculateIncome(), this.calculateOutcome()));
        this.addMoney(-this.calculateOutcome());
    }

    public int calculateIncome() {
        double incomePerPerson = (this.metrics.get(MetricTypes.FINANCIAL_METRIC).value / 2) *
                (this.metrics.get(MetricTypes.KNOWLEDGE_METRIC).value / 5) *
                (this.metrics.get(MetricTypes.POPULATION_METRIC).value);
        double income = incomePerPerson * 100;
        return (int) income;
    }
    public int calculateOutcome() {
        float safetyLoss = Math.abs(this.metrics.get(MetricTypes.POPULATION_METRIC).value - this.metrics.get(MetricTypes.SAFETY_METRIC).value);
        float healthLoss = Math.abs(this.metrics.get(MetricTypes.POPULATION_METRIC).value - this.metrics.get(MetricTypes.HEALTH_METRIC).value);
        float knowledgeLoss = Math.abs(this.metrics.get(MetricTypes.POPULATION_METRIC).value - this.metrics.get(MetricTypes.KNOWLEDGE_METRIC).value);
        double outcome = healthLoss * 100;
        outcome += safetyLoss * 50;
        outcome += knowledgeLoss * 30;
        return (int) outcome;
    }

    @Override
    public void selected(AbstractBlock block) {
        if (block instanceof EmptyBlock && this.running) {
            this.lastSelectedBlock = block;
            this.dialog.show();
        }
    }

    @Override
    public void updated(AbstractBlock block) {
        if(!(block instanceof EmptyBlock)) {
            this.updateMetrics();
        }
    }

    @Override
    public void onClick(View v) {
        if (this.lastSelectedBlock != null) {
            AbstractBlock newBlock = this.dialog.getSelectedBlock(v).clone();
            if(this.money < newBlock.getPrice()) {
                Snackbar.make(this.rootView, R.string.message_not_enough_money, Snackbar.LENGTH_LONG).show();
                return;
            }
            this.addMoney(-newBlock.getPrice());
            this.cityBoard.replaceBlock(this.lastSelectedBlock, newBlock);
            this.lastSelectedBlock = null;
        }
    }

    public long getRemainingRoundTime() {
        return this.dayDuration - this.gameTime;

    }

    public String getRemainingRoundTimeString(){
        long remainingTime = this.getRemainingRoundTime();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(remainingTime);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(remainingTime);
        return String.format(Locale.US,"%02d : %02d", minutes, seconds);
    }

    public Context getContext() {
        return this.context;
    }

    public int getMoney() {
        return this.money;
    }

    public CityBoard getCityBoard() {
        return this.cityBoard;
    }

    public void setMoney(int money) {
        this.money = money;
        if(this.listener != null) {
            this.listener.moneyUpdate(this);
        }
    }

    public void addMoney(int money) {
        this.setMoney(this.money + money);
    }

    public int getGameRound() {
        return gameRound;
    }

    public int getGameDay() {
        return gameDay;
    }

    public long getGameTime() {
        return gameTime;
    }

    public void setRoundDuration(long roundDuration) {
        this.roundDuration = roundDuration;
    }

    public void setDayDuration(long dayDuration) {
        this.dayDuration = dayDuration;
    }

    public int getLimitDays() {
        return limitDays;
    }

    public void setLimitDays(int limitDays) {
        this.limitDays = limitDays;
    }

    public int getLimitMoney() {
        return limitMoney;
    }

    public void setLimitMoney(int limitMoney) {
        this.limitMoney = limitMoney;
    }

    public boolean isWinner() {
        return winner;
    }

    public boolean isRunning() {
        return running;
    }

    public int getRequiredMoney() {
        return requiredMoney;
    }

    public void setRequiredMoney(int requiredMoney) {
        this.requiredMoney = requiredMoney;
    }

    public long getDayDuration() {
        return dayDuration;
    }


}
