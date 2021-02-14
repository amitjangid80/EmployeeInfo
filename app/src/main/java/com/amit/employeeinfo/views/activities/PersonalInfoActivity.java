package com.amit.employeeinfo.views.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.amit.employeeinfo.R;
import com.amit.employeeinfo.models.Employee;
import com.amit.employeeinfo.utils.Constants;
import com.amit.utilities.DateTimeUtils;
import com.amit.views.DatePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class PersonalInfoActivity extends BaseActivity
{
    private static final String TAG = PersonalInfoActivity.class.getSimpleName();

    @BindView(R.id.edtFirstName)
    public TextInputEditText edtFirstName;

    @BindView(R.id.edtLastName)
    public TextInputEditText edtLastName;

    @BindView(R.id.edtPhoneNo)
    public TextInputEditText edtPhoneNo;

    @BindView(R.id.rbMale)
    public AppCompatRadioButton rbMale;

    @BindView(R.id.rbOther)
    public AppCompatRadioButton rbOther;

    @BindView(R.id.rbFemale)
    public AppCompatRadioButton rbFemale;

    @BindView(R.id.tvSelectDOB)
    public TextView tvSelectDOB;

    private int code;
    private boolean isUpdate;
    private String selectedDOB, displayDOB;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        ButterKnife.bind(this);
        Bundle myExtras = getIntent().getExtras();

        if (myExtras != null && myExtras.size() > 0)
        {
            code = myExtras.getInt(Constants.COLUMN_CODE);
            isUpdate = myExtras.getBoolean(Constants.IS_UPDATE_EXTRA);
        }

        setToolbar(PersonalInfoActivity.this, getString(R.string.personal_info));
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
            if (isUpdate && code > 0)
            {
                // calling get employee details method
                ArrayList<Employee> employeesArrayList = localDbHelper.getEmployeeDetails(code);

                if (employeesArrayList != null && employeesArrayList.size() > 0)
                {
                    Employee employee = employeesArrayList.get(0);

                    selectedDOB = employee.getDob();
                    displayDOB = DateTimeUtils.formatDateTime(employee.getDob(), Constants.DISPLAY_DATE_FORMAT, Constants.DATE_TIME_FORMAT);

                    tvSelectDOB.setText(displayDOB);

                    edtPhoneNo.setText(employee.getPhoneNo());
                    edtLastName.setText(employee.getLastName());
                    edtFirstName.setText(employee.getFirstName());

                    switch (employee.getGender())
                    {
                        case 1:
                            rbMale.setChecked(true);
                            break;

                        case 2:
                            rbFemale.setChecked(true);
                            break;

                        case 3:
                            rbOther.setChecked(true);
                            break;
                    }
                }
            }
        }
        catch (Exception e)
        {
            handleException(TAG, "exception while getting data from db", e);
        }
    }

    public void selectDob(View view)
    {
        new DatePicker(PersonalInfoActivity.this)
                .setDateFormat(Constants.DATE_TIME_FORMAT)
                .setCurrentDateMin(false)
                .setOnDateSelectedListener(selectedDate ->
                {
                    try
                    {
                        selectedDOB = selectedDate;
                        displayDOB = DateTimeUtils.formatDateTime(selectedDate, Constants.DISPLAY_DATE_FORMAT, Constants.DATE_TIME_FORMAT);

                        tvSelectDOB.setText(displayDOB);
                    }
                    catch (ParseException e)
                    {
                        handleException(TAG, "exception while selecting and setting dob", e);
                    }
                })
                .showDatePicker();
    }

    public void savePersonalInfo(View view)
    {
        try
        {
            if (!validate())
            {
                return;
            }

            int gender;

            if (rbMale.isChecked())
            {
                gender = 1;
            }
            else if (rbFemale.isChecked())
            {
                gender = 2;
            }
            else
            {
                gender = 3;
            }

            Employee employee = new Employee();

            employee.setGender(gender);
            employee.setDob(selectedDOB);
            employee.setPhoneNo(edtPhoneNo.getText().toString().trim());
            employee.setLastName(edtLastName.getText().toString().trim());
            employee.setFirstName(edtFirstName.getText().toString().trim());

            int code = localDbHelper.addEmployeeData(employee, isUpdate);
            Log.e(TAG, "savePersonalInfo: inserted employee code is: " + code);

            if (code > 0)
            {
                Intent intent = new Intent(PersonalInfoActivity.this, EmployeeInfoActivity.class);
                intent.putExtra(Constants.COLUMN_CODE, code);
                startActivity(intent);
                finish();
            }
            else
            {
                showErrorDialog(PersonalInfoActivity.this, getResources().getString(R.string.saved_failed));
            }
        }
        catch (Exception e)
        {
            handleException(TAG, "exception while saving personal info in database", e);
        }
    }

    private boolean validate()
    {
        try
        {
            if (edtFirstName.getText() == null || edtFirstName.getText().toString().trim().length() < 2)
            {
                showErrorDialog(PersonalInfoActivity.this, getResources().getString(R.string.enter_first_name));

                return false;
            }

            if (edtLastName.getText() == null || edtLastName.getText().toString().trim().length() < 2)
            {
                showErrorDialog(PersonalInfoActivity.this, getResources().getString(R.string.enter_last_name));
                return false;
            }

            if (edtPhoneNo.getText() == null || edtPhoneNo.getText().toString().trim().length() < 2)
            {
                showErrorDialog(PersonalInfoActivity.this, getResources().getString(R.string.enter_phone_no));
                return false;
            }

            if (!rbMale.isChecked() && !rbFemale.isChecked() && !rbOther.isChecked())
            {
                showErrorDialog(PersonalInfoActivity.this, getResources().getString(R.string.select_gender));
                return false;
            }

            if (selectedDOB == null || selectedDOB.length() <= 10)
            {
                showErrorDialog(PersonalInfoActivity.this, getResources().getString(R.string.select_dob));
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
    protected void onDestroy()
    {
        super.onDestroy();
        System.gc();
    }
}
