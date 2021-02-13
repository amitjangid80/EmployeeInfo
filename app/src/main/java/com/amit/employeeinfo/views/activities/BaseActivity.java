package com.amit.employeeinfo.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.amit.dialog.PromptDialogBox;
import com.amit.employeeinfo.R;
import com.amit.employeeinfo.data.LocalDbHelper;

public abstract class BaseActivity extends AppCompatActivity
{
    private static final String TAG = BaseActivity.class.getSimpleName();

    public LocalDbHelper localDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        localDbHelper = new LocalDbHelper(BaseActivity.this);
    }

    public static void setToolbar(AppCompatActivity activity, String title)
    {
        try
        {
            Toolbar toolbar = activity.findViewById(R.id.toolbar);
            toolbar.setTitle(title);
            activity.setSupportActionBar(toolbar);

            if (activity.getSupportActionBar() != null)
            {
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
        }
        catch (Exception e)
        {
            handleException(TAG, "exception while setting toolbar for activity", e);
        }
    }

    public static void handleException(String TAG, String exceptionMsg, Exception e)
    {
        Log.e(TAG, "handleException: " + exceptionMsg + ":\n");
        e.printStackTrace();
    }

    public static void showSuccessDialog(Context context, String message)
    {
        try
        {
            new Handler(Looper.getMainLooper()).post(() ->
            {
                PromptDialogBox promptDialogBox = new PromptDialogBox(context)
                        .setDialogType(PromptDialogBox.DIALOG_TYPE_SUCCESS)
                        .setAnimationEnable(true)
                        .setTitleText(context.getResources().getString(R.string.success))
                        .setContentText(message)
                        .setPositiveListener(context.getResources().getString(R.string.ok), PromptDialogBox::dismiss);

                promptDialogBox.setCancelable(false);
                promptDialogBox.show();
            });
        }
        catch (Exception e)
        {
            handleException(TAG, "showErrorDialog: exception while showing error dialog", e);
        }
    }

    public static void showErrorDialog(Context context, String message)
    {
        try
        {
            new Handler(Looper.getMainLooper()).post(() ->
            {
                PromptDialogBox promptDialogBox = new PromptDialogBox(context)
                        .setDialogType(PromptDialogBox.DIALOG_TYPE_ERROR)
                        .setAnimationEnable(true)
                        .setTitleText(context.getResources().getString(R.string.error))
                        .setContentText(message)
                        .setPositiveListener(context.getResources().getString(R.string.ok), PromptDialogBox::dismiss);

                promptDialogBox.setCancelable(false);
                promptDialogBox.show();
            });
        }
        catch (Exception e)
        {
            handleException(TAG, "showErrorDialog: exception while showing error dialog", e);
        }
    }
}
