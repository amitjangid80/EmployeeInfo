package com.amit.employeeinfo.views.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.amit.employeeinfo.R;
import com.amit.employeeinfo.interfaces.OnEmployeeClickListener;
import com.amit.employeeinfo.models.Employee;
import com.amit.iv.CircularImageView;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

// Created by AMIT JANGID on 13/02/21.
public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.EmployeeListViewHolder>
{
    private final Context mContext;
    private final ArrayList<Employee> mEmployeesArrayList;
    private final OnEmployeeClickListener mOnEmployeeClickListener;

    public EmployeeListAdapter(Context context, ArrayList<Employee> employeesArrayList, OnEmployeeClickListener onEmployeeClickListener)
    {
        this.mContext = context;
        this.mEmployeesArrayList = employeesArrayList;
        mOnEmployeeClickListener = onEmployeeClickListener;
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
        if (mEmployeesArrayList != null && mEmployeesArrayList.size() > 0)
        {
            Employee employee = mEmployeesArrayList.get(position);
            String workExperience = mContext.getResources().getString(R.string.work_experience) + ": <b>" + employee.getWorkExperience() + "</b>";

            holder.tvDesignation.setText(employee.getDesignation());
            holder.tvEmployeeName.setText(employee.getEmployeeName());
            holder.tvWorkExperience.setText(Html.fromHtml(workExperience));

            if (employee.getEmpImage() != null)
            {
                byte[] image = employee.getEmpImage().getBytes(StandardCharsets.UTF_8);
                holder.ivEmpImage.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
            }
            else
            {
                holder.ivEmpImage.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.no_img, null));
            }

            holder.csEmployee.setOnClickListener(view -> mOnEmployeeClickListener.onEmployeeClick(employee));
        }
    }

    @Override
    public int getItemCount()
    {
        return mEmployeesArrayList != null ? mEmployeesArrayList.size() : 0;
    }

    public static class EmployeeListViewHolder extends RecyclerView.ViewHolder
    {
        private final ConstraintLayout csEmployee;
        private final CircularImageView ivEmpImage;
        private final TextView tvEmployeeName, tvDesignation, tvWorkExperience;

        public EmployeeListViewHolder(@NonNull View itemView)
        {
            super(itemView);

            csEmployee = itemView.findViewById(R.id.csEmployee);
            ivEmpImage = itemView.findViewById(R.id.ivEmpImage);
            tvDesignation = itemView.findViewById(R.id.tvDesignation);
            tvEmployeeName = itemView.findViewById(R.id.tvEmployeeName);
            tvWorkExperience = itemView.findViewById(R.id.tvWorkExperience);
        }
    }
}
