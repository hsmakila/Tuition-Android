package net.tution.thilini.tution.ListAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.tution.thilini.tution.DataStructures.Class;
import net.tution.thilini.tution.R;

import java.util.ArrayList;

public class Class_Adapter extends ArrayAdapter<Class> {

    private ArrayList<Class> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView tvSubject, tvGrade, tvDay, tvStartToEndTime, tvType;
        ImageView ivPic;
    }

    public Class_Adapter(ArrayList<Class> data, Context context) {
        super(context, R.layout.row_class, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Class aClass = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_class, parent, false);

            viewHolder.tvSubject = convertView.findViewById(R.id.tvSubject);
            viewHolder.tvGrade = convertView.findViewById(R.id.tvGrade);
            viewHolder.tvDay = convertView.findViewById(R.id.tvDay);
            viewHolder.tvStartToEndTime = convertView.findViewById(R.id.tvStartToEndTime);
            viewHolder.tvType = convertView.findViewById(R.id.tvType);
            viewHolder.ivPic = convertView.findViewById(R.id.ivPic);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(aClass!=null) {
            viewHolder.tvSubject.setText(aClass.getSubject());
            viewHolder.tvGrade.setText("Grade : " + aClass.getClass_grade());
            viewHolder.tvDay.setText("Day : " + aClass.getClass_day());
            viewHolder.tvStartToEndTime.setText("Time : " + aClass.getStart_time() + " - " + aClass.getEnd_time());
            viewHolder.tvType.setText("Type : " + aClass.getType());
            viewHolder.ivPic.setTag(position);
        }
        return convertView;
    }
}