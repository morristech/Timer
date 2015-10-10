package com.iec.dwx.timer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.graphics.ColorUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.iec.dwx.timer.Activities.BaseActivity;
import com.iec.dwx.timer.Activities.MainActivity;
import com.iec.dwx.timer.Activities.TimeActivity;
import com.iec.dwx.timer.Beans.WishBean;
import com.iec.dwx.timer.Utils.CacheManager.SDiskCacheManager;
import com.iec.dwx.timer.Utils.CacheManager.SMemoryCacheManager;
import com.iec.dwx.timer.Utils.PaletteGetPictureColor;

import butterknife.Bind;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;

public class TestActivity extends BaseActivity {


    @Bind(R.id.btn_test_1)
    Button mBtnTest1;
    @Bind(R.id.btn_test_2)
    Button mBtnTest2;
    @Bind(R.id.iv_show)
    ImageView mIvShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bmob.initialize(this,"3a39e05d106b31b3f61a8ce842933a8a");
        mSwipeBackLayout.setEnableGesture(false);
        mBtnTest1.setOnClickListener(v -> linkTo());
        mBtnTest2.setOnClickListener(v -> PaletteTest());
    }

    private void get() {
        Bitmap bitmap = SDiskCacheManager.getInstance(this).getBitmap("http:\\sdfas");
        if (bitmap==null) System.out.println("get null");
        mIvShow.setImageBitmap(bitmap);
    }

    private void save() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.b);
        if (bitmap==null) System.out.println("save null");
        SDiskCacheManager.getInstance(this).putBitmap("http:\\sdfas", bitmap);
    }

    private void linkTo() {
//        startActivity(new Intent(this, TimeActivity.class));
        //进入viewpager测试使用，记得修改回去
        startActivity(new Intent(this, MainActivity.class));
    }

    private void Bombsave(){
        WishBean wishBean=new WishBean();
        wishBean.setWishTime("aaa");
        wishBean.setWishContent("bbb");
        wishBean.setPictureUrl("ccc");
        wishBean.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                System.out.println("成功上传");
            }

            @Override
            public void onFailure(int i, String s) {
                System.out.println("上传失败");
            }
        });
    }

    private void Bombget(){
        BmobQuery<WishBean> bmobQuery=new BmobQuery<WishBean>();
        bmobQuery.getObject(this, "7176a5396d", new GetListener<WishBean>() {
            @Override
            public void onSuccess(WishBean wishBean) {
                System.out.println(wishBean.getWishTime());
            }

            @Override
            public void onFailure(int i, String s) {
                System.out.println("获取失败");
            }
        });
    }

    private void PaletteTest(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mywishes2);
        if (bitmap==null) System.out.println("save null");
        int color=PaletteGetPictureColor.getColorFromBitmap(getResources(),bitmap);
        mBtnTest2.setBackgroundColor(color);
        System.out.println(color);

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_test;
    }

    @Override
    protected int getEdgeSize() {
        return 0;
    }


}
