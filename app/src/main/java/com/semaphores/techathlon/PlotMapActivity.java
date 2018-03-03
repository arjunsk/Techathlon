package com.semaphores.techathlon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

public class PlotMapActivity extends AppCompatActivity
{
    private BottomSheetBehavior bottomSheetBehavior;
    private LinearLayout bottomSheetPlotMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot_map);

        initView();
        initBottomSheet();
    }

    public void initView()
    {
        bottomSheetPlotMap = findViewById(R.id.bottom_sheet_plot_map);
    }

    public void initBottomSheet()
    {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetPlotMap);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    public void onFabClick(View view)
    {
    }
}
