package com.javayhu.gankhub.module.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.javayhu.gankhub.R;
import com.javayhu.gankhub.data.GankDayData;
import com.javayhu.gankhub.data.GankRetrofit;
import com.javayhu.gankhub.data.GankService;

import java.util.Calendar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 某日的干货的数据
 * <p>
 * Created by hujiawei on 2017/1/22.
 */
public class GankDayActivity extends AppCompatActivity {

    private GankService mGankService;

    private int mYear;
    private int mMonth;
    private int mDay;

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gank_day);

        mGankService = GankRetrofit.getGankService();
        mTextView = (TextView) findViewById(R.id.content);

        Calendar calendar = Calendar.getInstance();
        Intent intent = getIntent();
        mYear = intent.getIntExtra("year", calendar.get(Calendar.YEAR));
        mMonth = intent.getIntExtra("month", calendar.get(Calendar.MONTH) + 1);
        mDay = intent.getIntExtra("day", calendar.get(Calendar.DAY_OF_MONTH));

        mGankService.getSomeDayData(mYear, mMonth, mDay)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GankDayData>() {
                    @Override
                    public void accept(GankDayData gankDayData) throws Exception {
                        mTextView.setText(gankDayData.category.get(0) + " " + gankDayData.category.get(1));
                    }
                });
    }


    public static void startActivity(Context context, int year, int month, int day) {
        Intent intent = new Intent(context, GankDayActivity.class);
        intent.putExtra("year", year);
        intent.putExtra("month", month);
        intent.putExtra("day", day);
        context.startActivity(intent);
    }
}
