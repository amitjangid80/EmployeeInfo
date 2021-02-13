package com.amit.employeeinfo.views.activities;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amit.employeeinfo.R;
import com.amit.employeeinfo.databinding.ActivityMainBinding;
import com.amit.employeeinfo.models.Employee;
import com.amit.employeeinfo.views.adapters.EmployeeListAdapter;

import java.util.ArrayList;

// Created by AMIT JANGID on 13/02/21.
public class MainActivity extends BaseActivity
{
    private static final String TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // calling set toolbar method
        setToolbar(MainActivity.this, getString(R.string.app_name));
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    private void getEmployeeList()
    {
        try
        {
            // calling get all employees method
            ArrayList<Employee> employeesArrayList = localDbHelper.getAllEmployees();

            if (employeesArrayList != null && employeesArrayList.size() > 0)
            {
                EmployeeListAdapter employeeListAdapter = new EmployeeListAdapter(employeesArrayList);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

                binding.rvEmployeesList.setLayoutManager(linearLayoutManager);
                binding.rvEmployeesList.setAdapter(employeeListAdapter);
            }
        }
        catch (Exception e)
        {
            handleException(TAG, "exception while getting data from db and setting to adapter", e);
        }
    }

    public void addNewEmployee(View view)
    {

    }
}
