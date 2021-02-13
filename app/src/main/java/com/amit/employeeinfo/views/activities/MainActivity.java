package com.amit.employeeinfo.views.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amit.employeeinfo.R;
import com.amit.employeeinfo.interfaces.OnEmployeeClickListener;
import com.amit.employeeinfo.models.Employee;
import com.amit.employeeinfo.utils.Constants;
import com.amit.employeeinfo.views.adapters.EmployeeListAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class MainActivity extends BaseActivity implements OnEmployeeClickListener
{
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.rvEmployeesList)
    public RecyclerView rvEmployeesList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // calling set toolbar method
        setToolbar(MainActivity.this, getString(R.string.employees));
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        // calling get employee list method
        getEmployeeList();
    }

    private void getEmployeeList()
    {
        try
        {
            // calling get all employees method
            ArrayList<Employee> employeesArrayList = localDbHelper.getEmployeeDetails(0);

            if (employeesArrayList != null && employeesArrayList.size() > 0)
            {
                EmployeeListAdapter employeeListAdapter = new EmployeeListAdapter(
                        MainActivity.this, employeesArrayList,
                        MainActivity.this);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);

                rvEmployeesList.setLayoutManager(linearLayoutManager);
                rvEmployeesList.setAdapter(employeeListAdapter);
            }
        }
        catch (Exception e)
        {
            handleException(TAG, "exception while getting data from db and setting to adapter", e);
        }
    }

    public void addNewEmployee(View view)
    {
        // calling navigate to personal info activity method
        navigateToPersonalInfoActivity(0, false);
    }

    private void navigateToPersonalInfoActivity(int code, boolean isUpdate)
    {
        Intent intent = new Intent(MainActivity.this, PersonalInfoActivity.class);
        intent.putExtra(Constants.COLUMN_CODE, code);
        intent.putExtra(Constants.IS_UPDATE_EXTRA, isUpdate);
        startActivity(intent);
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

    @Override
    public void onEmployeeClick(Employee employee)
    {
        // calling navigate to personal info activity method
        navigateToPersonalInfoActivity(employee.getCode(), true);
    }
}
