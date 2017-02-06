package com.javayhu.gankhub.module.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.javayhu.gankhub.R;
import com.javayhu.gankhub.data.GankRetrofit;
import com.javayhu.gankhub.data.GankService;
import com.javayhu.gankhub.module.about.AboutActivity;
import com.javayhu.gankhub.module.detail.GankDayActivity;
import com.javayhu.gankhub.view.PullToRefreshView;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 主页
 * <p>
 * Created hujiawei on 2017/1/21.
 */
public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();

    private GankService mGankService;

    private TextView mTextView;
    private PullToRefreshView mPullToRefreshView;

    public static final int REFRESH_DELAY = 2000;

    public static final String KEY_ICON = "icon";
    public static final String KEY_COLOR = "color";

    protected List<Map<String, Integer>> mSampleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        CrashReport.setUserSceneTag(this, 36107);

        mGankService = GankRetrofit.getGankService();

        Map<String, Integer> map;
        mSampleList = new ArrayList<>();

        int[] icons = {
                R.drawable.ic_next_month,
                R.drawable.ic_previous_month,
                R.drawable.ic_next_month};

        int[] colors = {
                R.color.calendar_foreground_today,
                R.color.calendar_foreground_semi,
                R.color.calendar_foreground_today};

        for (int i = 0; i < icons.length; i++) {
            map = new HashMap<>();
            map.put(KEY_ICON, icons[i]);
            map.put(KEY_COLOR, colors[i]);
            mSampleList.add(map);
        }

        mTextView = (TextView) findViewById(R.id.textview);
        mTextView.setClickable(true);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoGankDay();
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(new SampleAdapter());

        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, REFRESH_DELAY);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_search:
                return true;
            case R.id.menu_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            /*decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);*/

            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
    }



    private class SampleAdapter extends RecyclerView.Adapter<SampleHolder> {

        @Override
        public SampleHolder onCreateViewHolder(ViewGroup parent, int pos) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_rview_ganklist, parent, false);
            return new SampleHolder(view);
        }

        @Override
        public void onBindViewHolder(SampleHolder holder, int pos) {
            Map<String, Integer> data = mSampleList.get(pos);
            holder.bindData(data);
        }

        @Override
        public int getItemCount() {
            return mSampleList.size();
        }
    }

    private class SampleHolder extends RecyclerView.ViewHolder {

        private View mRootView;
        private ImageView mImageViewIcon;

        private Map<String, Integer> mData;

        public SampleHolder(View itemView) {
            super(itemView);

            mRootView = itemView;
            mImageViewIcon = (ImageView) itemView.findViewById(R.id.image_view_icon);
        }

        public void bindData(Map<String, Integer> data) {
            mData = data;

            mRootView.setBackgroundResource(mData.get(KEY_COLOR));
            mImageViewIcon.setImageResource(mData.get(KEY_ICON));
        }
    }

    private void gotoGankDay() {
        Intent intent = new Intent(this, GankDayActivity.class);
        intent.putExtra("year", 2017);
        intent.putExtra("month", 1);
        intent.putExtra("day", 20);
        startActivity(intent);

        /*ARouter.getInstance().build("/day")
                .withInt("year", 2017)
                .withInt("month", 1)
                .withInt("day", 20).navigation();*/
    }
}
