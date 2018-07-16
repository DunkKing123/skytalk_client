package com.thxy.skytalk_client.widget;

import android.text.Editable;
import android.text.TextWatcher;

/**
 *  一个实现TextWatcher的抽象类，继承该类的类可以只实现onTextChanged方法，用于简化代码
 */
public abstract class MyTextWatcher implements TextWatcher {
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
