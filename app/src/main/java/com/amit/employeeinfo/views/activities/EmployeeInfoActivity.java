package com.amit.employeeinfo.views.activities;

import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.amit.employeeinfo.R;
import com.amit.employeeinfo.models.Employee;
import com.amit.employeeinfo.utils.Constants;
import com.amit.views.ASpinner;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class EmployeeInfoActivity extends BaseActivity
{
    private static final String TAG = EmployeeInfoActivity.class.getSimpleName();

    @BindView(R.id.edtEmployeeNo)
    public TextInputEditText edtEmployeeNo;

    @BindView(R.id.edtEmployeeName)
    public TextInputEditText edtEmployeeName;

    @BindView(R.id.edtDesignation)
    public TextInputEditText edtDesignation;

    @BindView(R.id.spinnerAccountType)
    public ASpinner spinnerAccountType;

    @BindView(R.id.spinnerWorkExperience)
    public ASpinner spinnerWorkExperience;

    private int code;
    private Employee mEmployee;
    private String selectedAccountType, selectedWorkExperience;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_info);

        ButterKnife.bind(this);
        Bundle myExtras = getIntent().getExtras();

        if (myExtras != null && myExtras.size() > 0)
        {
            code = myExtras.getInt(Constants.COLUMN_CODE);
        }

        setToolbar(EmployeeInfoActivity.this, getString(R.string.employee_info));

        // calling init activity method
        initActivity();
    }

    private void initActivity()
    {
        try
        {
            spinnerAccountType.setItems(getResources().getStringArray(R.array.account_type_array));
            spinnerWorkExperience.setItems(getResources().getStringArray(R.array.work_experience_array));

            spinnerAccountType.setOnItemClickListener((selectedItem, position) ->
            {
                selectedAccountType = selectedItem;
                spinnerAccountType.setText(selectedItem);
            });

            spinnerWorkExperience.setOnItemClickListener((selectedItem, position) ->
            {
                selectedWorkExperience = selectedItem;
                spinnerWorkExperience.setText(selectedItem);
            });
        }
        catch (Exception e)
        {
            handleException(TAG, "exception while initializing activity", e);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        // calling get employee details method
        getEmployeeDetails();
    }

    private void getEmployeeDetails()
    {
        try
        {
            if (code > 0)
            {
                // calling get employee details method
                ArrayList<Employee> employeesArrayList = localDbHelper.getEmployeeDetails(code);

                if (employeesArrayList != null && employeesArrayList.size() > 0)
                {
                    mEmployee = employeesArrayList.get(0);

                    selectedAccountType = mEmployee.getAccountType();
                    selectedWorkExperience = mEmployee.getWorkExperience();

                    spinnerAccountType.setText(mEmployee.getAccountType());
                    spinnerWorkExperience.setText(mEmployee.getWorkExperience());

                    edtEmployeeNo.setText(mEmployee.getEmployeeNo());
                    edtDesignation.setText(mEmployee.getDesignation());
                    edtEmployeeName.setText(mEmployee.getEmployeeName());
                }
            }
        }
        catch (Exception e)
        {
            handleException(TAG, "exception while getting data from db", e);
        }
    }

    public void saveEmployeeInfo(View view)
    {
        try
        {
            if (mEmployee != null)
            {
                if (!validate())
                {
                    return;
                }

                mEmployee.setAccountType(selectedAccountType);
                mEmployee.setWorkExperience(selectedWorkExperience);
                mEmployee.setEmployeeNo(edtEmployeeNo.getText().toString().trim());
                mEmployee.setDesignation(edtDesignation.getText().toString().trim());
                mEmployee.setEmployeeName(edtEmployeeName.getText().toString().trim());

                int code = localDbHelper.addEmployeeData(mEmployee, true);
                Log.e(TAG, "savePersonalInfo: inserted employee code is: " + code);

                if (code > 0)
                {
                    Intent intent = new Intent(EmployeeInfoActivity.this, BankInfoActivity.class);
                    intent.putExtra(Constants.COLUMN_CODE, code);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    showErrorDialog(EmployeeInfoActivity.this, getResources().getString(R.string.saved_failed));
                }
            }
        }
        catch (Exception e)
        {
            handleException(TAG, "exception while saving employee info", e);
        }
    }

    private boolean validate()
    {
        try
        {
            if (edtEmployeeNo.getText() == null || edtEmployeeNo.getText().toString().trim().length() < 2)
            {
                showErrorDialog(EmployeeInfoActivity.this, getResources().getString(R.string.enter_emp_no));

                return false;
            }

            if (edtEmployeeName.getText() == null || edtEmployeeName.getText().toString().trim().length() < 2)
            {
                showErrorDialog(EmployeeInfoActivity.this, getResources().getString(R.string.enter_emp_name));
                return false;
            }

            if (edtDesignation.getText() == null || edtDesignation.getText().toString().trim().length() < 2)
            {
                showErrorDialog(EmployeeInfoActivity.this, getResources().getString(R.string.enter_designation));
                return false;
            }

            if (selectedAccountType == null || selectedAccountType.isEmpty())
            {
                showErrorDialog(EmployeeInfoActivity.this, getResources().getString(R.string.select_account_type));
                return false;
            }

            if (selectedWorkExperience == null || selectedWorkExperience.length() <= 2)
            {
                showErrorDialog(EmployeeInfoActivity.this, getResources().getString(R.string.select_work_exp));
                return false;
            }

            return true;
        }
        catch (Exception e)
        {
            handleException(TAG, "exception while validating fields", e);
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        Intent intent = new Intent(EmployeeInfoActivity.this, PersonalInfoActivity.class);
        intent.putExtra(Constants.COLUMN_CODE, code);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        System.gc();
    }
}
