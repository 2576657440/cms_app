package org.com.gannan.farminginfoplatform.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import org.com.gannan.farminginfoplatform.R;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class ChooseOneDateDialog extends Dialog implements View.OnClickListener {

    private PriorityListener listener;
    private boolean scrolling = false;
    private NumericWheelAdapter yearAdapter = null;
    private NumericWheelAdapter monthAdapter = null;
    private NumericWheelAdapter dayAdapter = null;
    private int curYear = 0;
    private int curMonth = 0;
    private int curDay = 0;
    private Button btnSubmit;
    private WheelView yearview;
    private WheelView monthview;
    private WheelView dayview;

    private int flag = 0;

    public interface PriorityListener {
        public void refreshPriorityUI(String year, String month, String day, int flag);
    }


    public ChooseOneDateDialog(final Context context, final PriorityListener listener,
                               int currentyear, int currentmonth,
                               int currentday) {
        super(context, android.R.style.Theme_Holo_Dialog_NoActionBar);
        this.listener = listener;
        this.curYear = currentyear;
        this.curMonth = currentmonth;
        this.curDay = currentday;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_select_one_wheel);
        btnSubmit = (Button) findViewById(R.id.submitButton);
        LinearLayout dateLayout = (LinearLayout) findViewById(R.id.date_selelct_layout);
        yearview = (WheelView) findViewById(R.id.year);
        monthview = (WheelView) findViewById(R.id.month);
        dayview = (WheelView) findViewById(R.id.day);

        Display display = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int dip = (int) (20 * metrics.density);
        LayoutParams lparamsHours = new LayoutParams(metrics.widthPixels - dip, metrics.heightPixels / 3);
        dateLayout.setLayoutParams(lparamsHours);

        addListener();
        setWheelView();

    }

    private void setWheelView() {
        Calendar calendar = Calendar.getInstance();
        if (this.curYear == 0 || this.curMonth == 0) {
            curYear = calendar.get(Calendar.YEAR);
            curMonth = calendar.get(Calendar.MONTH) + 1;
            curDay = calendar.get(Calendar.DAY_OF_MONTH);
        }

        yearAdapter = new NumericWheelAdapter(2016, curYear+50);
        yearview.setAdapter(yearAdapter);
        int cc = curYear - 2016;
        yearview.setCurrentItem(cc);
        yearview.setVisibleItems(5);
        monthAdapter = new NumericWheelAdapter(1, 12, "%02d");
        monthview.setAdapter(monthAdapter);
        monthview.setCurrentItem(curMonth - 1);
        monthview.setVisibleItems(5);
        updateDays(yearview, monthview, dayview);
//        dayview.setCyclic(true);
        dayview.setVisibleItems(5);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submitButton://确定
                dismiss();
                listener.refreshPriorityUI(yearAdapter.getValues(),
                        monthAdapter.getValues(), dayAdapter.getValues(), flag);
                break;

        }
    }

    private void addListener() {
        OnWheelChangedListener listener = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (!scrolling) {
                    updateDays(yearview, monthview, dayview);
                }
            }
        };
        monthview.addChangingListener(listener);
        yearview.addChangingListener(listener);
        btnSubmit.setOnClickListener(this);
    }

    /**
     * 根据年份和月份来更新日期
     */
    private void updateDays(WheelView year, WheelView month, WheelView day) {
        String[] monthsBig = {"1", "3", "5", "7", "8", "10", "12"};
        String[] monthsLittle = {"4", "6", "9", "11"};

        final List<String> listBig = Arrays.asList(monthsBig);
        final List<String> listLittle = Arrays.asList(monthsLittle);
        int yearNum = year.getCurrentItem() + 2001;
        if (listBig.contains(String.valueOf(month.getCurrentItem() + 1))) {
            dayAdapter = new NumericWheelAdapter(1, 31, "%02d");
        } else if (listLittle.contains(String.valueOf(month.getCurrentItem() + 1))) {
            dayAdapter = new NumericWheelAdapter(1, 30, "%02d");
        } else {//二月
            //闰年
            if ((yearNum % 4 == 0 && yearNum % 100 != 0) || yearNum % 400 == 0) {
                dayAdapter = new NumericWheelAdapter(1, 29, "%02d");
            } else {//平常年
                dayAdapter = new NumericWheelAdapter(1, 28, "%02d");
            }
        }
        dayview.setAdapter(dayAdapter);
        dayview.setCurrentItem(curDay - 1);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

}
