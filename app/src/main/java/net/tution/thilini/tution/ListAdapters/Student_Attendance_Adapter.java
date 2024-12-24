package net.tution.thilini.tution.ListAdapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import net.tution.thilini.tution.DataStructures.StudentAttendance;
import net.tution.thilini.tution.R;

import java.util.ArrayList;

public class Student_Attendance_Adapter extends ArrayAdapter<StudentAttendance> {

    private ArrayList<StudentAttendance> dataSet;

    private static class ViewHolder {
        TextView tvStudentName;
        FrameLayout flDay1, flDay2, flDay3, flDay4, flDay5;
    }

    public Student_Attendance_Adapter(ArrayList<StudentAttendance> dataSet, Context context) {
        super(context, R.layout.row_student_attendence, dataSet);
        this.dataSet = dataSet;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        StudentAttendance studentAttendance = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_student_attendence, parent, false);

            viewHolder.tvStudentName = convertView.findViewById(R.id.tvStudentName);
            viewHolder.flDay1 = convertView.findViewById(R.id.flDay1);
            viewHolder.flDay2 = convertView.findViewById(R.id.flDay2);
            viewHolder.flDay3 = convertView.findViewById(R.id.flDay3);
            viewHolder.flDay4 = convertView.findViewById(R.id.flDay4);
            viewHolder.flDay5 = convertView.findViewById(R.id.flDay5);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvStudentName.setText(studentAttendance.getStudent().getFull_name());

        FrameLayout[] frameLayouts = {viewHolder.flDay1, viewHolder.flDay2, viewHolder.flDay3, viewHolder.flDay4, viewHolder.flDay5};

        for (int i = 0; i < 5; i++) {
            if (studentAttendance.getAttendance()[i] == 1)
                frameLayouts[i].setBackgroundColor(Color.GREEN);
            else
                frameLayouts[i].setBackgroundColor(Color.BLACK);
        }
        viewHolder.tvStudentName.setTag(position);

        return convertView;
    }

}