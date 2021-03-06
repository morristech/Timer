package com.iec.dwx.timer.Runnable;

import android.content.Context;

import com.iec.dwx.timer.Beans.CommonBean;
import com.iec.dwx.timer.Beans.SkillBean;
import com.iec.dwx.timer.Utils.DataBaseHelper.DBHelper;
import com.iec.dwx.timer.Utils.DataBaseHelper.DataBaseSkillHelper;

/**
 * Created by Administrator on 2015/10/23 0023.
 */
public class SkillUpdatePositionLeft implements Runnable {
    private final String TAG = SkillUpdatePositionLeft.class.getSimpleName();
    private Context context;
    private int id;
    private int left;

    public SkillUpdatePositionLeft(Context context, int id, int left) {
        this.context = context;
        this.id = id;
        this.left = left;
    }

    @Override
    public void run() {

        SkillBean skillBean= DataBaseSkillHelper.getInstance(context).getOneBean(id);
        if (skillBean == null) return;
        skillBean.setMarginLeft(left);
        DataBaseSkillHelper.getInstance(context).updateOneBean(skillBean.getmId(),skillBean);
    }
}
