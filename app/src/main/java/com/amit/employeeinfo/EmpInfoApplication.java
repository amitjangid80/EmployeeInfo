package com.amit.employeeinfo;

import android.app.Application;

import com.amit.db.DBHelper;
import com.amit.db.DbColumn;
import com.amit.employeeinfo.utils.Constants;
import com.amit.employeeinfo.views.activities.BaseActivity;
import com.amit.utilities.SharedPreferenceData;

// Created by AMIT JANGID on 13/02/21.
public class EmpInfoApplication extends Application
{
    private final String TAG = EmpInfoApplication.class.getSimpleName();

    private DBHelper dbHelper;
    private SharedPreferenceData sharedPreferenceData;

    @Override
    public void onCreate()
    {
        super.onCreate();

        new Thread(() ->
        {
            sharedPreferenceData = new SharedPreferenceData(EmpInfoApplication.this);
            sharedPreferenceData.setValue(Constants.DB_NAME_EXTRA, Constants.DATABASE_NAME);

            // initializing db helper class
            dbHelper = new DBHelper(EmpInfoApplication.this);

            // calling employee info table method
            createEmployeeInfoTable();
        }).start();
    }

    private void createEmployeeInfoTable()
    {
        try
        {
            if (dbHelper.isTableExists(Constants.TABLE_EMPLOYEE_INFO))
            {
                return;
            }

            dbHelper.addColumnForTable(new DbColumn(Constants.COLUMN_CODE, new String[]{"integer", "primary key", "autoincrement"}))
                    .addColumnForTable(new DbColumn(Constants.COLUMN_FIRST_NAME, "text"))
                    .addColumnForTable(new DbColumn(Constants.COLUMN_LAST_NAME, "text"))
                    .addColumnForTable(new DbColumn(Constants.COLUMN_PHONE_NO, "text"))
                    .addColumnForTable(new DbColumn(Constants.COLUMN_GENDER, "integer"))
                    .addColumnForTable(new DbColumn(Constants.COLUMN_DOB, "text"))
                    .addColumnForTable(new DbColumn(Constants.COLUMN_EMPLOYEE_NO, "text"))
                    .addColumnForTable(new DbColumn(Constants.COLUMN_EMPLOYEE_NAME, "text"))
                    .addColumnForTable(new DbColumn(Constants.COLUMN_DESIGNATION, "text"))
                    .addColumnForTable(new DbColumn(Constants.COLUMN_ACCOUNT_TYPE, "text"))
                    .addColumnForTable(new DbColumn(Constants.COLUMN_WORK_EXPERIENCE, "integer"))
                    .addColumnForTable(new DbColumn(Constants.COLUMN_BANK_NAME, "text"))
                    .addColumnForTable(new DbColumn(Constants.COLUMN_BRANCH_NAME, "text"))
                    .addColumnForTable(new DbColumn(Constants.COLUMN_ACCOUNT_NO, "text"))
                    .addColumnForTable(new DbColumn(Constants.COLUMN_IFSC_CODE, "text"))
                    .addColumnForTable(new DbColumn(Constants.COLUMN_EMP_IMAGE, "blob"))
                    .addColumnForTable(new DbColumn(Constants.COLUMN_ENTRY_DATE, "text"))
                    .addColumnForTable(new DbColumn(Constants.COLUMN_UPDATE_DATE, "text"))
                    .createTable(Constants.TABLE_EMPLOYEE_INFO);
        }
        catch (Exception e)
        {
            BaseActivity.handleException(TAG, "exception while creating employee info table", e);
        }
    }
}
