package com.guide.geek.ui;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * author : 桥下一粒砂 (chenyoca@gmail.com)
 * date   : 2013-07-03
 * ViewPager定时轮播
 */
public class ViewPagerScheduler {



        private ViewPager viewPager;
        private int count;

        static final int MESSAGE = 520131474;

        private Handler scheduleTurnHandler = new Handler(){
                @Override
                public void handleMessage (Message msg) {
                        // 如果没有两条以上数据，则不需要轮播
                        if(MESSAGE != msg.what || count < 2) return;
                        int index = (viewPager.getCurrentItem()+1)%count;
                        viewPager.setCurrentItem(index, true);
                }
        };

        private Timer scheduleTimer;

        public ViewPagerScheduler(ViewPager viewPager){
                this.viewPager = viewPager;
        }

        /**
         * 更新ViewPager包含的数据数量
         * @param count 数据量
         */
        public void updateCount(int count){
                this.count = count;
        }

        /**
         * 重新开启（首次开启）定时轮播。
         * @param period 轮播周期，单位毫秒。
         */
        public void restart (int period){
                scheduleTimer = new Timer();
                scheduleTimer.schedule(new TimerTask() {
                        @Override
                        public void run () {
                                scheduleTurnHandler.sendEmptyMessage(MESSAGE);
                        }
                },period,period);
        }

        /**
         * 停止。
         */
        public void stop(){
                if(scheduleTimer != null) scheduleTimer.cancel();
        }

}