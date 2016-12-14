package com.example.yunyi.projecthydra;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.ViewportChangeListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PreviewLineChartView;

/**
 * Created by Yun YI on 23/11/2016.
 */

public class DetailsActivity extends AppCompatActivity {

    LineChartView chart;
    PreviewLineChartView previewChart;
    LineChartData data;
    LineChartData previewData;
    String id;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intentt = getIntent();
        Bundle bundle = intentt.getExtras();

        if (bundle != null) {
            id = (String) bundle.get("idNum");
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        chart = (LineChartView)findViewById(R.id.chart);
        previewChart = (PreviewLineChartView) findViewById(R.id.preview_chart);

        generateDefaultData();

        chart.setLineChartData(data);
        chart.setZoomEnabled(false);
        chart.setScrollEnabled(false);
        chart.setOnValueTouchListener(new ValueTouchListener());

        previewChart.setLineChartData(previewData);
        previewChart.setViewportChangeListener(new ViewportListener());

        previewX(true);
    }

    private void generateDefaultData() {
        int numValues = 50;

        List<PointValue> values = new ArrayList<PointValue>();
        for (int i = 0; i < numValues; ++i) {
            values.add(new PointValue(i, (int) (Math.random() * 100)));
        }

        Line line = new Line(values);
        line.setColor(ChartUtils.COLOR_BLUE);

        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        values = new ArrayList<PointValue>();
        for (int i = 0; i < numValues; ++i) {
            values.add(new PointValue(i, (int) (Math.random() * 100)));
        }

        line = new Line(values);
        line.setColor(ChartUtils.COLOR_VIOLET);
        lines.add(line);

        data = new LineChartData(lines);
        previewData = new LineChartData(data);

        data.setAxisXBottom(new Axis().setName("Time").setTypeface(Typeface.DEFAULT_BOLD).setTextColor(Color.rgb(120,120,120)));
        data.setAxisYLeft(new Axis().setHasLines(true).setTypeface(Typeface.DEFAULT_BOLD).setTextColor(Color.rgb(120,120,120)));

        previewData.getLines().get(0).setColor(ChartUtils.DEFAULT_COLOR);
        previewData.getLines().get(1).setColor(ChartUtils.DEFAULT_COLOR);
        previewData.getLines().get(0).setHasPoints(false);
        previewData.getLines().get(1).setHasPoints(false);



    }

    private class ViewportListener implements ViewportChangeListener {

        @Override
        public void onViewportChanged(Viewport newViewport) {
            // don't use animation, it is unnecessary when using preview chart.
            chart.setCurrentViewport(newViewport);
        }

    }

    private void previewY() {
        Viewport tempViewport = new Viewport(chart.getMaximumViewport());
        float dy = tempViewport.height() / 4;
        tempViewport.inset(0, dy);
        previewChart.setCurrentViewportWithAnimation(tempViewport);
        previewChart.setZoomType(ZoomType.VERTICAL);
    }

    private void previewX(boolean animate) {
        Viewport tempViewport = new Viewport(chart.getMaximumViewport());
        float dx = tempViewport.width() / 3;
        tempViewport.inset(dx, 0);
        if (animate) {
            previewChart.setCurrentViewportWithAnimation(tempViewport);
        } else {
            previewChart.setCurrentViewport(tempViewport);
        }
        previewChart.setZoomType(ZoomType.HORIZONTAL);
    }

    private void previewXY() {
        // Better to not modify viewport of any chart directly so create a copy.
        Viewport tempViewport = new Viewport(chart.getMaximumViewport());
        // Make temp viewport smaller.
        float dx = tempViewport.width() / 4;
        float dy = tempViewport.height() / 4;
        tempViewport.inset(dx, dy);
        previewChart.setCurrentViewportWithAnimation(tempViewport);
    }

    private class ValueTouchListener implements LineChartOnValueSelectListener {

        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            Toast.makeText(getApplicationContext(), "Selected: " + value, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signout:
                signOut();
                return true;
            case R.id.settings:
                Intent intent = new Intent(DetailsActivity.this, SettingsActivity.class);
                intent.putExtra("idNum", id);
                startActivity(intent);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signOut() {

        mAuth.signOut();
        Intent intent = new Intent(DetailsActivity.this, AuthActivity.class);
        startActivity(intent);
    }


}