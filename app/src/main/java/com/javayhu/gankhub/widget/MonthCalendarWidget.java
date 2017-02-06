package com.javayhu.gankhub.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.javayhu.gankhub.R;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import static com.javayhu.gankhub.R.id.calendar;

/**
 * 桌面日历组件 (改进版本)
 * <p>
 * 组件源于 https://github.com/romannurik/Android-MonthCalendarWidget
 * <p>
 * Updated by hujiawei on 2017/2/4.
 */
public class MonthCalendarWidget extends AppWidgetProvider {

    private static final String ACTION_PREVIOUS_MONTH = "widget.canlendar.action.PREVIOUS_MONTH";
    private static final String ACTION_NEXT_MONTH = "widget.canlendar.action.NEXT_MONTH";
    private static final String ACTION_RESET = "widget.canlendar.action.RESET";
    private static final String ACTION_CLICK = "widget.canlendar.action.CLICK";

    private static final String PREF_MONTH = "month";
    private static final String PREF_YEAR = "year";
    private static final String PREF_DAY = "day";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int appWidgetId : appWidgetIds) {
            drawWidget(context, appWidgetId);
        }
    }

    private void redrawWidgets(Context context) {
        int[] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(
                new ComponentName(context, MonthCalendarWidget.class));
        for (int appWidgetId : appWidgetIds) {
            drawWidget(context, appWidgetId);
        }
    }

    /**
     * 接收桌面组件点击时发送的广播(处理来自组件的事件响应)
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();

        if (ACTION_PREVIOUS_MONTH.equals(action)) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            Calendar cal = Calendar.getInstance();
            int thisMonth = sp.getInt(PREF_MONTH, cal.get(Calendar.MONTH));
            int thisYear = sp.getInt(PREF_YEAR, cal.get(Calendar.YEAR));
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.MONTH, thisMonth);
            cal.set(Calendar.YEAR, thisYear);
            cal.add(Calendar.MONTH, -1);//减一个月
            sp.edit().putInt(PREF_MONTH, cal.get(Calendar.MONTH))
                    .putInt(PREF_YEAR, cal.get(Calendar.YEAR))
                    .apply();
            redrawWidgets(context);

        } else if (ACTION_NEXT_MONTH.equals(action)) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            Calendar cal = Calendar.getInstance();
            int thisMonth = sp.getInt(PREF_MONTH, cal.get(Calendar.MONTH));
            int thisYear = sp.getInt(PREF_YEAR, cal.get(Calendar.YEAR));
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.MONTH, thisMonth);
            cal.set(Calendar.YEAR, thisYear);
            cal.add(Calendar.MONTH, 1);//加一个月
            sp.edit().putInt(PREF_MONTH, cal.get(Calendar.MONTH))
                    .putInt(PREF_YEAR, cal.get(Calendar.YEAR))
                    .apply();
            redrawWidgets(context);

        } else if (ACTION_RESET.equals(action)) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            sp.edit().remove(PREF_MONTH).remove(PREF_YEAR).apply();
            redrawWidgets(context);

        } else if (ACTION_CLICK.equals(action)) {
            Calendar cal = Calendar.getInstance();
            int year = intent.getIntExtra(PREF_YEAR, cal.get(Calendar.YEAR));
            int month = intent.getIntExtra(PREF_MONTH, cal.get(Calendar.MONTH)) + 1;
            int day = intent.getIntExtra(PREF_DAY, cal.get(Calendar.DAY_OF_MONTH));
            Toast.makeText(context, "date:" + year + " " + month + " " + day, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        drawWidget(context, appWidgetId);
    }

    /**
     * 组件绘制
     * 1.删除原始组件的mini状态
     * 2.修改原始组件的最小宽高
     */
    private void drawWidget(Context context, int appWidgetId) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        Resources res = context.getResources();
        Bundle widgetOptions = appWidgetManager.getAppWidgetOptions(appWidgetId);
        boolean shortMonthName = false;
        int numWeeks = 6;//最多的时候有6行
        if (widgetOptions != null) {
            int minWidthDp = widgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
            //int minHeightDp = widgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
            //如果当前组件的宽度小于shortMonthLabel设置的最大宽度的时候，那么就显示shortMonth
            shortMonthName = minWidthDp <= res.getInteger(R.integer.max_width_short_month_label_dp);
        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_calendar);

        //设置组件顶部中间显示的内容
        Calendar cal = Calendar.getInstance();
        int today = cal.get(Calendar.DAY_OF_YEAR);
        int todayYear = cal.get(Calendar.YEAR);

        int thisMonth = sp.getInt(PREF_MONTH, cal.get(Calendar.MONTH));
        int thisYear = sp.getInt(PREF_YEAR, cal.get(Calendar.YEAR));

        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, thisMonth);
        cal.set(Calendar.YEAR, thisYear);
        //2月 17 或者 2月 2017
        rv.setTextViewText(R.id.month_label, DateFormat.format(shortMonthName ? "MMM yy" : "MMM yyyy", cal));

        //设置组件左上角的第一天
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int monthStartDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DAY_OF_MONTH, 1 - monthStartDayOfWeek);

        rv.removeAllViews(calendar);

        //添加一行表示周x
        RemoteViews headerRowRv = new RemoteViews(context.getPackageName(), R.layout.widget_calendar_row_header);
        DateFormatSymbols dfs = DateFormatSymbols.getInstance();
        String[] weekdays = dfs.getShortWeekdays();
        for (int day = Calendar.SUNDAY; day <= Calendar.SATURDAY; day++) {
            RemoteViews dayRv = new RemoteViews(context.getPackageName(), R.layout.widget_calendar_cell_header);
            dayRv.setTextViewText(android.R.id.text1, weekdays[day]);
            headerRowRv.addView(R.id.row_container, dayRv);
        }
        rv.addView(calendar, headerRowRv);

        //添加各周的日子
        for (int week = 0; week < numWeeks; week++) {
            RemoteViews rowRv = new RemoteViews(context.getPackageName(), R.layout.widget_calendar_row_week);
            for (int day = 0; day < 7; day++) {
                //noinspection WrongConstant
                boolean inMonth = cal.get(Calendar.MONTH) == thisMonth;
                boolean inYear = cal.get(Calendar.YEAR) == todayYear;
                boolean isToday = inYear && inMonth && (cal.get(Calendar.DAY_OF_YEAR) == today);//inMonth 多余?

                int cellLayoutResId = R.layout.widget_calendar_cell_day;
                if (isToday) {
                    cellLayoutResId = R.layout.widget_calendar_cell_today;
                } else if (inMonth) {
                    cellLayoutResId = R.layout.widget_calendar_cell_day_this_month;
                }

                RemoteViews cellRv = new RemoteViews(context.getPackageName(), cellLayoutResId);
                cellRv.setTextViewText(android.R.id.text1, Integer.toString(cal.get(Calendar.DAY_OF_MONTH)));

                Bundle bundle = new Bundle();
                bundle.putInt(PREF_YEAR, cal.get(Calendar.YEAR));
                bundle.putInt(PREF_MONTH, cal.get(Calendar.MONTH));
                bundle.putInt(PREF_DAY, cal.get(Calendar.DAY_OF_MONTH));

                Intent intent = new Intent(context, MonthCalendarWidget.class);
                intent.setAction(ACTION_CLICK);
                intent.putExtras(bundle);

                int requestCode = buildRequestCode(cal);//PendingIntent的requestCode不同就是不同的PendingIntent
                cellRv.setOnClickPendingIntent(android.R.id.text1, PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT));

                rowRv.addView(R.id.row_container, cellRv);
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
            rv.addView(calendar, rowRv);

            //noinspection WrongConstant
            boolean inMonth = cal.get(Calendar.MONTH) == thisMonth;
            if (!inMonth) {//下面一行的日子都不在当前月中的时候提前退出外层for循环
                break;
            }
        }

        //添加事件监听
        rv.setOnClickPendingIntent(R.id.prev_month_button,
                PendingIntent.getBroadcast(context, 0,
                        new Intent(context, MonthCalendarWidget.class).setAction(ACTION_PREVIOUS_MONTH),
                        PendingIntent.FLAG_UPDATE_CURRENT));

        rv.setOnClickPendingIntent(R.id.next_month_button,
                PendingIntent.getBroadcast(context, 0,
                        new Intent(context, MonthCalendarWidget.class).setAction(ACTION_NEXT_MONTH),
                        PendingIntent.FLAG_UPDATE_CURRENT));

        rv.setOnClickPendingIntent(R.id.month_label,
                PendingIntent.getBroadcast(context, 0,
                        new Intent(context, MonthCalendarWidget.class).setAction(ACTION_RESET),
                        PendingIntent.FLAG_UPDATE_CURRENT));

        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    /**
     * 只要保证不同的year、month、day得到的结果不同就行
     */
    private int buildRequestCode(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return year + month * 100 + day;
    }
}
