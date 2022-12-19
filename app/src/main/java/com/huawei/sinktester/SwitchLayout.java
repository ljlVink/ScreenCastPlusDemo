package com.huawei.sinktester;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

public class SwitchLayout extends ConstraintLayout {
    private final Switch sw;
    private final TextView pwd,title;

    public SwitchLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwitchLayout);
        int anInt = typedArray.getInt(R.styleable.SwitchLayout_mode, 0);
        String string = typedArray.getString(R.styleable.SwitchLayout_mytext);

        View view = LayoutInflater.from(context).inflate(R.layout.layout_switch, this, true);
        sw =view.findViewById(R.id.sw);
        pwd = view.findViewById(R.id.pwd);
        title =view.findViewById(R.id.title);
        title.setText(null != string ? string : "");
        if (anInt == 0) {
            sw.setVisibility(VISIBLE);
            pwd.setVisibility(GONE);
        } else {
            pwd.setVisibility(VISIBLE);
            sw.setVisibility(GONE);
        }

        sw.setClickable(false);
        typedArray.recycle();

    }


    public void setCheck(boolean check) {
        sw.setChecked(check);
    }

    public void setPwd(String pwd) {
        this.pwd.setText(pwd);
    }

    public boolean getCheck() {
        return sw.isChecked();
    }

}
