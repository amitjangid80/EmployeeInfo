package com.amit.employeeinfo.data;

import android.content.Context;
import android.database.Cursor;

import com.amit.db.DBHelper;
import com.amit.db.DbData;
import com.amit.employeeinfo.models.Employee;
import com.amit.employeeinfo.utils.Constants;
import com.amit.employeeinfo.views.activities.BaseActivity;
import com.amit.utilities.DateTimeUtils;
import com.amit.utilities.TextUtils;

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

    public ArrayList<Employee> getEmployeeDetails(int code)
    {
        try
        {
            boolean hasConditions = false;
            StringBuilder conditionalValues = null;
            ArrayList<Employee> employeesArrayList = new ArrayList<>();

            if (code > 0)
            {
                hasConditions = true;
                conditionalValues = new StringBuilder();
                conditionalValues.append(Constants.COLUMN_CODE).append(" = ").append(code);
            }

            Cursor cursor = dbHelper.executeSelectQuery(Constants.TABLE_EMPLOYEE_INFO,
                    "*", hasConditions, conditionalValues);

            if (cursor != null && cursor.moveToFirst())
            {
                for (int i = 0; i < cursor.getCount(); i++)
                {
                    Employee employee = new Employee();
                    employee.setCode(cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_CODE)));
                    employee.setGender(cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_GENDER)));
                    employee.setDob(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_DOB)));
                    employee.setPhoneNo(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_PHONE_NO)));
                    employee.setLastName(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_LAST_NAME)));
                    employee.setFirstName(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_FIRST_NAME)));
                    employee.setAccountType(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_ACCOUNT_TYPE)));
                    employee.setWorkExperience(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_WORK_EXPERIENCE)));
                    employee.setEmployeeNo(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_EMPLOYEE_NO)));
                    employee.setDesignation(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_DESIGNATION)));
                    employee.setEmployeeName(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_EMPLOYEE_NAME)));
                    employee.setBranchName(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_BRANCH_NAME)));
                    employee.setBankName(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_BANK_NAME)));
                    employee.setIfscCode(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_IFSC_CODE)));
                    employee.setAccountNo(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_ACCOUNT_NO)));
                    employee.setEmpImage(cursor.getBlob(cursor.getColumnIndex(Constants.COLUMN_EMP_IMAGE)));

                    employeesArrayList.add(employee);
                    cursor.moveToNext();
                }

                cursor.close();
            }

            return employeesArrayList;
        }
        catch (Exception e)
        {
            BaseActivity.handleException(TAG, "exception while getting all employees from db", e);

            return null;
        }
    }

    public int addEmployeeData(Employee employee, boolean isUpdate)
    {
        try
        {
            dbHelper.addDataForTable(new DbData(Constants.COLUMN_FIRST_NAME, TextUtils.replaceNullWithEmpty(employee.getFirstName())))
                    .addDataForTable(new DbData(Constants.COLUMN_LAST_NAME, TextUtils.replaceNullWithEmpty(employee.getLastName())))
                    .addDataForTable(new DbData(Constants.COLUMN_PHONE_NO, TextUtils.replaceNullWithEmpty(employee.getPhoneNo())))
                    .addDataForTable(new DbData(Constants.COLUMN_GENDER, TextUtils.replaceNullWithZero(String.valueOf(employee.getGender()))))
                    .addDataForTable(new DbData(Constants.COLUMN_DOB, TextUtils.replaceNullWithEmpty(employee.getDob())))
                    .addDataForTable(new DbData(Constants.COLUMN_EMPLOYEE_NO, TextUtils.replaceNullWithEmpty(employee.getEmployeeNo())))
                    .addDataForTable(new DbData(Constants.COLUMN_EMPLOYEE_NAME, TextUtils.replaceNullWithEmpty(employee.getEmployeeName())))
                    .addDataForTable(new DbData(Constants.COLUMN_DESIGNATION, TextUtils.replaceNullWithEmpty(employee.getDesignation())))
                    .addDataForTable(new DbData(Constants.COLUMN_ACCOUNT_TYPE, TextUtils.replaceNullWithEmpty(employee.getAccountType())))
                    .addDataForTable(new DbData(Constants.COLUMN_WORK_EXPERIENCE, TextUtils.replaceNullWithEmpty(employee.getWorkExperience())))
                    .addDataForTable(new DbData(Constants.COLUMN_BANK_NAME, TextUtils.replaceNullWithEmpty(employee.getBankName())))
                    .addDataForTable(new DbData(Constants.COLUMN_BRANCH_NAME, TextUtils.replaceNullWithEmpty(employee.getBranchName())))
                    .addDataForTable(new DbData(Constants.COLUMN_ACCOUNT_NO, TextUtils.replaceNullWithEmpty(employee.getAccountNo())))
                    .addDataForTable(new DbData(Constants.COLUMN_IFSC_CODE, TextUtils.replaceNullWithEmpty(employee.getIfscCode())))
                    .addDataForTable(new DbData(Constants.COLUMN_EMP_IMAGE, employee.getEmpImage()));

            if (isUpdate)
            {
                String conditionalValues = Constants.COLUMN_CODE + " = " + employee.getCode();

                dbHelper.addDataForTable(new DbData(Constants.COLUMN_UPDATE_DATE, DateTimeUtils.getCurrentDateTime(Constants.DATE_TIME_FORMAT)))
                        .updateData(Constants.TABLE_EMPLOYEE_INFO, conditionalValues);
            }
            else
            {
                dbHelper.addDataForTable(new DbData(Constants.COLUMN_ENTRY_DATE, DateTimeUtils.getCurrentDateTime(Constants.DATE_TIME_FORMAT)))
                        .insertData(Constants.TABLE_EMPLOYEE_INFO);
            }

            return dbHelper.getMaxId(Constants.COLUMN_CODE, Constants.TABLE_EMPLOYEE_INFO);
        }
        catch (Exception e)
        {
            BaseActivity.handleException(TAG, "exception while adding personal info in database", e);
            return -1;
        }
    }
}
