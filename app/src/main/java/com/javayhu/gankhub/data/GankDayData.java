package com.javayhu.gankhub.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * gank data in some day
 * <p>
 * Created by javayhu on 1/21/17.
 */
public class GankDayData {

    public boolean error;
    public List<String> category;
    public Results results;

    class Results {
        @SerializedName("Android")
        public List<GankData> androidList;
        @SerializedName("休息视频")
        public List<GankData> videoList;
        @SerializedName("iOS")
        public List<GankData> iosList;
        @SerializedName("福利")
        public List<GankData> girlList;
        @SerializedName("拓展资源")
        public List<GankData> resourceList;
        @SerializedName("瞎推荐")
        public List<GankData> recommendList;
        @SerializedName("App")
        public List<GankData> appList;
    }

}
