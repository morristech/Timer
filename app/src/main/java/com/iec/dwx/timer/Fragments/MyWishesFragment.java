package com.iec.dwx.timer.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.iec.dwx.timer.Activities.PublicWishes;
import com.iec.dwx.timer.R;
import com.iec.dwx.timer.Utils.EricViewHolder.FragmentMyWishesViewHolder;

public class MyWishesFragment extends Fragment {

    private FragmentMyWishesViewHolder myViewHolder=null;
    private float clickDownY;
    private float clickUpY;
    private boolean buttonEnable=false;

    //设定最小滑动距离
    private  float Y_DISTANCE_SLIDE=30f;

    //测试数据
    private int currentPosition=0;
    private int imges[]=new int[]{R.drawable.mywishes1,R.drawable.mywishes2,
                                  R.drawable.mywishes3,R.drawable.mywishes4};
    private int txts[]=new int[]{R.string.fragment_my_wishes_test_content1,R.string.fragment_my_wishes_test_content2,
            R.string.fragment_my_wishes_test_content3,R.string.fragment_my_wishes_test_content4};
    //测试数据结束

    public static MyWishesFragment newInstance() {
        return new MyWishesFragment();
    }

    public MyWishesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myViewHolder=new FragmentMyWishesViewHolder();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_wishes, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //初始化myViewHolder
        if(myViewHolder.getFlag()){

            FragmentActivity fragmentActivity=getActivity();

            //主界面
            myViewHolder.setmTv((TextView) fragmentActivity.findViewById(R.id.fragment_my_wishes_tv));
            myViewHolder.setmImg((ImageView) fragmentActivity.findViewById(R.id.fragment_my_wishes_iv));

            //点击侧边键初始化
            myViewHolder.setIntoOthersBtn((ImageButton) fragmentActivity.findViewById(R.id.fragment_my_wishes_Into_others_button));
            myViewHolder.setShareBtn((ImageButton) fragmentActivity.findViewById(R.id.fragment_my_wishes_share_button));
            myViewHolder.setAddImgBtn((ImageButton) fragmentActivity.findViewById(R.id.fragment_my_wishes_add_button));
            myViewHolder.setDeleteImgBtn((ImageButton) fragmentActivity.findViewById(R.id.fragment_my_wishes_delete_button));

            //为右边键添加响应
            componentAddOnclickRegister();
            myViewHolder.changeFlag(false);
        }

        //监听点击事件,按道理说不需要，但是不写这个划动也会失效
        getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("fragment_my_wishes", "该onclik不需要使用");
            }
        });

        //监听划动与点击事件
        getView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //利用落点和起点之间的Y距离来判断竖划
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        clickDownY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        clickUpY = event.getY();
                        MovingMethod(clickUpY - clickDownY);
                        onClickMethod(clickUpY - clickDownY);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });


    }


    //根据屏幕初大小始化一个部件的位置
    private void  componentPositionInit(){
        //记得在这里控制textview的位置或者用其他方式
    }

    //为侧边键添加点击响应
    private void componentAddOnclickRegister(){
        myViewHolder.getIntoOthersBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), PublicWishes.class);
                startActivity(intent);
            }
        });
    }

    //点击响应事件
    private void onClickMethod(float y){
        if(y==0f){
            Log.e("fragment_my_wishes", "真正点击应处理的事件");

            if(buttonEnable=(!buttonEnable)){
                //按钮进入
                myAnimationButtonInDelayStart(myViewHolder.getDeleteImgBtn(),0);
                myAnimationButtonInDelayStart(myViewHolder.getAddImgBtn(),
                        getContext().getResources().getInteger(R.integer.fragment_my_wishes_buttons_animation_gap_time));
                myAnimationButtonInDelayStart(myViewHolder.getShareBtn(),
                        2*getContext().getResources().getInteger(R.integer.fragment_my_wishes_buttons_animation_gap_time));
                myAnimationButtonInDelayStart(myViewHolder.getIntoOthersBtn(),
                        3*getContext().getResources().getInteger(R.integer.fragment_my_wishes_buttons_animation_gap_time));
            }else{
                //按钮消失
                myAnimationButtonOutDelayStart(myViewHolder.getDeleteImgBtn(),0);
                myAnimationButtonOutDelayStart(myViewHolder.getAddImgBtn(),
                        getContext().getResources().getInteger(R.integer.fragment_my_wishes_buttons_animation_gap_time));
                myAnimationButtonOutDelayStart(myViewHolder.getShareBtn(),
                        2 * getContext().getResources().getInteger(R.integer.fragment_my_wishes_buttons_animation_gap_time));
                myAnimationButtonOutDelayStart(myViewHolder.getIntoOthersBtn(),
                        3 * getContext().getResources().getInteger(R.integer.fragment_my_wishes_buttons_animation_gap_time));
            }

        }

    }

    //划动响应事件
    private  void MovingMethod(float y){
        if(y<-Y_DISTANCE_SLIDE){
            upMovingMehtod();
            Log.e("fragment_my_wishes", y + "上划");
        }else if(y>Y_DISTANCE_SLIDE){
            downMovingMethod();
            Log.e("fragment_my_wishes", y + "下划");
        }
    }


    //上划响应事件
    private void upMovingMehtod(){
        //测试数据
        if(currentPosition<3){
            currentPosition++;
            myViewHolder.getmImg().setImageResource(imges[currentPosition]);
            myViewHolder.getmTv().setText(txts[currentPosition]);
        }

    }

    //下划响应事件
    private void downMovingMethod(){
       //测试数据
        if(currentPosition>0){
            currentPosition--;
            myViewHolder.getmImg().setImageResource(imges[currentPosition]);
            myViewHolder.getmTv().setText(txts[currentPosition]);
        }
    }

    private void myAnimationButtonInDelayStart(ImageButton imageButton,long delayMilliSecond){
         Animation buttonIn=AnimationUtils.loadAnimation(getContext(),R.anim.fragment_my_wishes_buttons_in);
        buttonIn.setStartOffset(delayMilliSecond);
        imageButton.setVisibility(View.VISIBLE);
        imageButton.setClickable(true);
        imageButton.startAnimation(buttonIn);
    }

    private void myAnimationButtonOutDelayStart(ImageButton imageButton,long delayMilliSecond){
        Animation buttonIn=AnimationUtils.loadAnimation(getContext(),R.anim.fragment_my_wishes_buttons_out);
        buttonIn.setStartOffset(delayMilliSecond);
        imageButton.startAnimation(buttonIn);
        imageButton.setClickable(false);
        imageButton.setVisibility(View.INVISIBLE);
    }
}
