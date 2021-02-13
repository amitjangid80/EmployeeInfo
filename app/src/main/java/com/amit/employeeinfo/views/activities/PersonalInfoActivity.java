package com.amit.employeeinfo.views.activities;

import android.os.Bundle;

import com.amit.employeeinfo.R;

public class PersonalInfoActivity extends BaseActivity
{
    private static final String TAG = PersonalInfoActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
    }
}
