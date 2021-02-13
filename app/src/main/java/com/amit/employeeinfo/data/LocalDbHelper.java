package com.amit.employeeinfo.data;

import android.content.Context;

import com.amit.db.DBHelper;
import com.amit.employeeinfo.models.Employee;
import com.amit.employeeinfo.utils.Constants;
import com.amit.employeeinfo.views.activities.BaseActivity;

import java.util.ArrayList;

// Created by AMIT JANGID on 13/02/21.
public class LocalDbHelper
{
    private static final String TAG = LocalDbHelper.class.getSimpleName();

    private final DBHelper dbHelper;

    public LocalDbHelper(Context context)
    {
        this.dbHelper = new DBHelper(context);
    }

    public ArrayList<Employee> getAllEmployees()
    {
        try
        {
            return dbHelper.getAllRecords(Constants.TABLE_EMPLOYEE_INFO,
                    true, null, Employee.class);
        }
        catch (Exception e)
        {
            BaseActivity.handleException(TAG, "exception while getting all employees from db", e);

            return null;
        }
    }
}
