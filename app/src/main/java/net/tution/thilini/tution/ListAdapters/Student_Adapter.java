package net.tution.thilini.tution.ListAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.tution.thilini.tution.DataStructures.Student;
import net.tution.thilini.tution.R;

import java.util.ArrayList;

public class Student_Adapter extends ArrayAdapter<Student>  {

    private ArrayList<Student> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView tvName;
        TextView tvGradeClass;
        TextView tvEmail;
        ImageView ivPic;
    }

    public Student_Adapter(ArrayList<Student> data, Context context) {
        super(context, R.layout.row_student_teacher, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Student student = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_student_teacher, parent, false);

            viewHolder.tvName = convertView.findViewById(R.id.tvFullName);
            viewHolder.tvGradeClass =  convertView.findViewById(R.id.tvTelephoneOrGradeClass);
            viewHolder.tvEmail =  convertView.findViewById(R.id.tvEmail);
            viewHolder.ivPic =  convertView.findViewById(R.id.ivPic);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvName.setText(student.getFull_name());
        viewHolder.tvGradeClass.setText("Grade : " + student.getGrade());
        viewHolder.tvEmail.setText("Email : " + student.getEmail());
        viewHolder.ivPic.setTag(position);

        return convertView;
    }
}