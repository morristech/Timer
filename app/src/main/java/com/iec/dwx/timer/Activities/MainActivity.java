package com.iec.dwx.timer.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.iec.dwx.timer.Fragments.AchievementFragment;
import com.iec.dwx.timer.Fragments.MyWishesFragment;
import com.iec.dwx.timer.Fragments.SkillFragment;
import com.iec.dwx.timer.R;
import com.iec.dwx.timer.Utils.Utils;

import butterknife.Bind;
import me.imid.swipebacklayout.lib.SwipeBackLayout;

public class MainActivity extends BaseActivity implements SwipeBackLayout.SwipeListener {
    public static final String INTENT_KEY_PAGE = "page";

    @Bind(R.id.vp_main)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        choosePage();
    }

    private void choosePage() {
        int flag = getIntent().getIntExtra(INTENT_KEY_PAGE, 0);
        mViewPager.setCurrentItem(flag);
    }

    private void init() {
        mSwipeBackLayout.addSwipeListener(this);
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);

        PageAdapter adapter = new PageAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position==0){
                    getSwipeBackLayout().setEdgeSize(Utils.dp2px(200));
                }else {
                    getSwipeBackLayout().setEdgeSize(0);
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        mViewPager.setPageTransformer(false, new PageTransformer());
    }

    @Override
    protected int getEdgeSize() {
        return 0;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }


    @Override
    public void onScrollStateChange(int state, float scrollPercent) {
    }

    @Override
    public void onEdgeTouch(int edgeFlag) {
    }

    @Override
    public void onScrollOverThreshold() {
        Log.d(TAG, "onScrollOverThreshold");
    }

    private class PageAdapter extends FragmentStatePagerAdapter {
        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return MyWishesFragment.newInstance();
            } else if (position == 1) {
                return AchievementFragment.newInstance();
            } else {
                return SkillFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_time_enter, R.anim.activity_time_exit);
    }
}
