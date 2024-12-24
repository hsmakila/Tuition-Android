package net.tution.thilini.tution.ListAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.tution.thilini.tution.DataStructures.Teacher;
import net.tution.thilini.tution.R;

import java.util.ArrayList;

public class Teacher_Adapter extends ArrayAdapter<Teacher> {

    private ArrayList<Teacher> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView tvName;
        TextView tvEmail;
        TextView tvTelephone;
        ImageView ivPic;
    }

    public Teacher_Adapter(ArrayList<Teacher> data, Context context) {
        super(context, R.layout.row_student_teacher, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Teacher teacher = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_student_teacher, parent, false);

            viewHolder.tvName = convertView.findViewById(R.id.tvFullName);
            viewHolder.tvTelephone = convertView.findViewById(R.id.tvTelephoneOrGradeClass);
            viewHolder.tvEmail = convertView.findViewById(R.id.tvEmail);
            viewHolder.ivPic = convertView.findViewById(R.id.ivPic);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvName.setText(teacher.getFirst_name() + " " + teacher.getLast_name());
        viewHolder.tvEmail.setText("Email : " + teacher.getEmail());
        viewHolder.tvTelephone.setText("Tel : " + teacher.getTelephone());
        viewHolder.ivPic.setTag(position);

        return convertView;
    }
}