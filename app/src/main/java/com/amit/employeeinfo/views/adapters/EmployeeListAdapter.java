package com.amit.employeeinfo.views.adapters;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amit.employeeinfo.R;
import com.amit.employeeinfo.models.Employee;
import com.amit.employeeinfo.views.activities.BaseActivity;
import com.amit.iv.CircularImageView;

import java.util.ArrayList;

// Created by AMIT JANGID on 13/02/21.
public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.EmployeeListViewHolder>
{
    private static final String TAG = EmployeeListAdapter.class.getSimpleName();

    private final ArrayList<Employee> mEmployeesArrayList;

    public EmployeeListAdapter(ArrayList<Employee> employeesArrayList)
    {
        this.mEmployeesArrayList = employeesArrayList;
    }

    @NonNull
    @Override
    public EmployeeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee_list, parent, false);
        return new EmployeeListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeListViewHolder holder, int position)
    {
        Employee employee = mEmployeesArrayList.get(position);

        holder.bind(employee);
    }

    @Override
    public int getItemCount()
    {
        return 0;
    }

    public static class EmployeeListViewHolder extends RecyclerView.ViewHolder
    {
        private final CircularImageView ivEmpImage;
        private final TextView tvEmployeeName, tvDesignation;

        public EmployeeListViewHolder(@NonNull View itemView)
        {
            super(itemView);

            ivEmpImage = itemView.findViewById(R.id.ivEmpImage);
            tvDesignation = itemView.findViewById(R.id.tvDesignation);
            tvEmployeeName = itemView.findViewById(R.id.tvEmployeeName);
        }

        private void bind(Employee employee)
        {
            try
            {
                tvDesignation.setText(employee.getDesignation());
                tvEmployeeName.setText(employee.getEmployeeName());
                ivEmpImage.setImageBitmap(BitmapFactory.decodeByteArray(employee.getEmpImage(), 0, employee.getEmpImage().length));
            }
            catch (Exception e)
            {
                BaseActivity.handleException(TAG, "exception while binding data to view", e);
            }
        }
    }
}
