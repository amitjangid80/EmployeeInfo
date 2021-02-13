package com.amit.employeeinfo.views.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.amit.employeeinfo.R;
import com.amit.employeeinfo.models.Employee;
import com.amit.employeeinfo.utils.Constants;
import com.amit.img_picker.ImagePicker;
import com.amit.views.ASpinner;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class BankInfoActivity extends BaseActivity
{
    private static final String TAG = BankInfoActivity.class.getSimpleName();

    @BindView(R.id.edtBankName)
    public TextInputEditText edtBankName;

    @BindView(R.id.edtAccountNo)
    public TextInputEditText edtAccountNo;

    @BindView(R.id.edtIFSCCode)
    public TextInputEditText edtIFSCCode;

    @BindView(R.id.spinnerBranchName)
    public ASpinner spinnerBranchName;

    @BindView(R.id.ivCaptureEmpImage)
    public AppCompatImageView ivCaptureEmpImage;

    private int code;
    private Employee mEmployee;
    private String selectedBranchName, selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_info);

        ButterKnife.bind(this);
        Bundle myExtras = getIntent().getExtras();

        if (myExtras != null && myExtras.size() > 0)
        {
            code = myExtras.getInt(Constants.COLUMN_CODE);
        }

        setToolbar(BankInfoActivity.this, getString(R.string.bank_info));

        // calling init activity method
        initActivity();
    }

    private void initActivity()
    {
        try
        {
            spinnerBranchName.setItems(getResources().getStringArray(R.array.branch_name_array));

            spinnerBranchName.setOnItemClickListener((selectedItem, position) ->
            {
                selectedBranchName = selectedItem;
                spinnerBranchName.setText(selectedItem);
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

                    selectedBranchName = mEmployee.getBranchName();
                    spinnerBranchName.setText(mEmployee.getBranchName());

                    edtBankName.setText(mEmployee.getBankName());
                    edtIFSCCode.setText(mEmployee.getIfscCode());
                    edtAccountNo.setText(mEmployee.getAccountNo());
                }
            }
        }
        catch (Exception e)
        {
            handleException(TAG, "exception while getting data from db", e);
        }
    }

    public void captureEmpImage(View view)
    {
        ImagePicker.Companion.with(BankInfoActivity.this).cameraOnly().start();
    }

    public void saveBankInfo(View view)
    {
        try
        {
            if (mEmployee != null)
            {
                mEmployee.setEmpImage(selectedImage);
                mEmployee.setBranchName(selectedBranchName);
                mEmployee.setBankName(edtBankName.getText().toString().trim());
                mEmployee.setIfscCode(edtIFSCCode.getText().toString().trim());
                mEmployee.setAccountNo(edtAccountNo.getText().toString().trim());

                int code = localDbHelper.addEmployeeData(mEmployee, true);
                Log.e(TAG, "savePersonalInfo: inserted employee code is: " + code);

                if (code > 0)
                {
                    showSuccessDialogWithExit(BankInfoActivity.this, getResources().getString(R.string.saved_successfully));
                }
                else
                {
                    showErrorDialog(BankInfoActivity.this, getResources().getString(R.string.saved_failed));
                }
            }
        }
        catch (Exception e)
        {
            handleException(TAG, "exception while saving employee info", e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        try
        {
            if (resultCode == Activity.RESULT_OK)
            {
                if (data != null && data.getData() != null)
                {
                    Uri uri = data.getData();
                    ivCaptureEmpImage.setImageURI(uri);

                    int length, bufferSize = 1024;
                    byte[] buffer = new byte[bufferSize];

                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                    while ((length = inputStream.read(buffer)) != -1)
                    {
                        byteArrayOutputStream.write(buffer, 0, length);
                    }

                    byte[] imageArray = byteArrayOutputStream.toByteArray();
                    selectedImage = new String(imageArray, StandardCharsets.UTF_8);
                }
            }
        }
        catch (Exception e)
        {
            handleException(TAG, "exception while getting image in on activity result", e);
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

        Intent intent = new Intent(BankInfoActivity.this, EmployeeInfoActivity.class);
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
