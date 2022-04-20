package com.poulstar.blocky;

import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.poulstar.blocky.core.AbstractBlock;
import com.poulstar.blocky.core.AbstractMetric;
import com.poulstar.blocky.core.BlockTypes;
import com.poulstar.blocky.core.CityBoard;
import com.poulstar.blocky.core.GameEngine;
import com.poulstar.blocky.core.GameEngineListener;
import com.poulstar.blocky.core.MetricTypes;
import com.poulstar.blocky.core.blocks.ApartmentBlock;
import com.poulstar.blocky.core.blocks.EmergencyBlock;
import com.poulstar.blocky.core.blocks.FactoryBlock;
import com.poulstar.blocky.core.blocks.FireStationBlock;
import com.poulstar.blocky.core.blocks.HospitalBlock;
import com.poulstar.blocky.core.blocks.HouseBlock;
import com.poulstar.blocky.core.blocks.ITCompanyBlock;
import com.poulstar.blocky.core.blocks.MallBlock;
import com.poulstar.blocky.core.blocks.PoliceStationBlock;
import com.poulstar.blocky.core.blocks.SchoolBlock;
import com.poulstar.blocky.core.blocks.UniversityBlock;
import com.poulstar.blocky.core.components.StartDialog;
import com.poulstar.blocky.core.components.StartDialogListener;
import com.poulstar.blocky.core.metrics.FinancialMetric;
import com.poulstar.blocky.core.metrics.HealthMetric;
import com.poulstar.blocky.core.metrics.KnowledgeMetric;
import com.poulstar.blocky.core.metrics.PopulationMetric;
import com.poulstar.blocky.core.metrics.SafetyMetric;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements GameEngineListener, StartDialogListener {

    protected GameEngine game;
    protected View layRoot;
    protected ViewGroup layMetricContainer;
    protected TableLayout layGameTable;
    protected TextView lblTime, lblRound, lblMoney;
    protected ImageButton btnInstructions;
    private StartDialog startDialog;
    protected PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);
        final PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "blocky:playing");

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle(R.string.app_name);
        this.startDialog = new StartDialog(MainActivity.this);
        this.startDialog.setDialogListener(this);
        this.startDialog.show();

        this.layRoot = findViewById(R.id.layRoot);
        this.layMetricContainer = findViewById(R.id.layMetricContainer);
        this.layGameTable = findViewById(R.id.layGameTable);
        this.lblTime = findViewById(R.id.lblTime);
        this.lblMoney = findViewById(R.id.lblMoney);
        this.lblRound = findViewById(R.id.lblRound);
        this.btnInstructions = findViewById(R.id.btnInstructions);
        this.btnInstructions.setOnClickListener(v -> {
            startDialog.show();
        });
        createGame();
        createMetrics();
        this.game.setListener(this);
    }

    protected void createGame() {
        CityBoard cityBoard = new CityBoard(4, 4);
        // Initialize metrics
        HashMap<MetricTypes, AbstractMetric> metrics = new HashMap<>();
        metrics.put(MetricTypes.POPULATION_METRIC, new PopulationMetric());
        metrics.put(MetricTypes.HEALTH_METRIC, new HealthMetric());
        metrics.put(MetricTypes.KNOWLEDGE_METRIC, new KnowledgeMetric());
        metrics.put(MetricTypes.FINANCIAL_METRIC, new FinancialMetric());
        metrics.put(MetricTypes.SAFETY_METRIC, new SafetyMetric());

        // Initialize blocks
        HashMap<BlockTypes, AbstractBlock> blocks = new HashMap<>();
        blocks.put(BlockTypes.HOUSE_BLOCK, new HouseBlock());
        blocks.put(BlockTypes.APARTMENT_BLOCK, new ApartmentBlock());
        blocks.put(BlockTypes.EMERGENCY_BLOCK, new EmergencyBlock());
        blocks.put(BlockTypes.HOSPITAL_BLOCK, new HospitalBlock());
        blocks.put(BlockTypes.MALL_BLOCK, new MallBlock());
        blocks.put(BlockTypes.FACTORY_BLOCK, new FactoryBlock());
        blocks.put(BlockTypes.IT_COMPANY_BLOCK, new ITCompanyBlock());
        blocks.put(BlockTypes.POLICE_STATION_BLOCK, new PoliceStationBlock());
        blocks.put(BlockTypes.FIRE_STATION_BLOCK, new FireStationBlock());
        blocks.put(BlockTypes.SCHOOL_BLOCK, new SchoolBlock());
        blocks.put(BlockTypes.UNIVERSITY_BLOCK, new UniversityBlock());

        this.game = new GameEngine(MainActivity.this, layRoot, metrics, blocks, cityBoard);
        TableRow[] rows = this.game.createGameBoard(getApplicationContext());
        TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        rowParams.weight = 1;
        for(TableRow row : rows) {
            this.layGameTable.addView(row, rowParams);
        }
    }

    protected void createMetrics(){
        this.layMetricContainer.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = ((float) 100) / this.game.getMetrics().size();
        for(AbstractMetric metric : this.game.getMetrics().values()) {
            View metricView = metric.createLayout(getApplicationContext());
            this.layMetricContainer.addView(metricView, params);
        }
    }

    public void resetTime() {
        runOnUiThread(() -> {
            lblTime.setText("00 : 00");
        });
    }
    public void updateTexts() {
        runOnUiThread(() -> {
            lblTime.setText(game.getRemainingRoundTimeString());
            lblRound.setText(String.valueOf(game.getGameDay()));
        });
    }
    public void updateMoney() {
        runOnUiThread(() -> {
            lblMoney.setText(String.valueOf(game.getMoney()));
        });
    }

    @Override
    public void gameTick(GameEngine gameEngine) {
        this.updateTexts();
    }

    @Override
    public void moneyUpdate(GameEngine gameEngine) {
        this.updateMoney();
    }

    @Override
    public void gameEnded(GameEngine gameEngine) {
        this.wakeLock.release();
        this.startDialog.hide();
        this.resetTime();
        if(gameEngine.isWinner()) {
            runOnUiThread(() -> {
                startDialog.setWinFace();
                startDialog.show();
            });
        }else {
            runOnUiThread(() -> {
                startDialog.setFailFace();
                startDialog.show();
            });
        }
    }

    @Override
    public void start() {
        this.startDialog.hide();
        this.game.setMoney(5000);
        this.game.setRoundDuration(2000);
        this.game.setDayDuration(30000);
        this.game.setLimitDays(8);
        this.game.setLimitMoney(-5000);
        this.game.setRequiredMoney(10000);
        this.wakeLock.acquire(this.game.getLimitDays() * this.game.getDayDuration());
        this.game.start();
    }

    @Override
    public void back() {
        this.startDialog.hide();
    }
}