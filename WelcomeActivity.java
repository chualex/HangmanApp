package com.csci448.alchu.alexchu_a2;

import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

public class WelcomeActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new WelcomeFragment();
    }

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_welcome;
    }


}
