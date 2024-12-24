package net.tution.thilini.tution.ListAdapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import net.tution.thilini.tution.DataStructures.StudentPayment;
import net.tution.thilini.tution.R;

import java.util.ArrayList;

public class Payments_Adapter extends ArrayAdapter<StudentPayment> {

    String TAG = "Payments_Adapter";

    private ArrayList<StudentPayment> dataSet;

    private static class ViewHolder {
        TextView tvStudentName;
        FrameLayout flMonth1, flMonth2, flMonth3, flMonth4, flMonth5, flMonth6, flMonth7, flMonth8, flMonth9, flMonth10, flMonth11, flMonth12;
    }

    public Payments_Adapter(ArrayList<StudentPayment> dataSet, Context context) {
        super(context, R.layout.row_payments, dataSet);
        this.dataSet = dataSet;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        StudentPayment studentPayment = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_payments, parent, false);

            viewHolder.tvStudentName = convertView.findViewById(R.id.tvStudentName);
            viewHolder.flMonth1 = convertView.findViewById(R.id.flMonth1);
            viewHolder.flMonth2 = convertView.findViewById(R.id.flMonth2);
            viewHolder.flMonth3 = convertView.findViewById(R.id.flMonth3);
            viewHolder.flMonth4 = convertView.findViewById(R.id.flMonth4);
            viewHolder.flMonth5 = convertView.findViewById(R.id.flMonth5);
            viewHolder.flMonth6 = convertView.findViewById(R.id.flMonth6);
            viewHolder.flMonth7 = convertView.findViewById(R.id.flMonth7);
            viewHolder.flMonth8 = convertView.findViewById(R.id.flMonth8);
            viewHolder.flMonth9 = convertView.findViewById(R.id.flMonth9);
            viewHolder.flMonth10 = convertView.findViewById(R.id.flMonth10);
            viewHolder.flMonth11 = convertView.findViewById(R.id.flMonth11);
            viewHolder.flMonth12 = convertView.findViewById(R.id.flMonth12);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvStudentName.setText(studentPayment.getStudent().getFull_name());

        FrameLayout[] frameLayouts = {
                viewHolder.flMonth1,
                viewHolder.flMonth2,
                viewHolder.flMonth3,
                viewHolder.flMonth4,
                viewHolder.flMonth5,
                viewHolder.flMonth6,
                viewHolder.flMonth7,
                viewHolder.flMonth8,
                viewHolder.flMonth9,
                viewHolder.flMonth10,
                viewHolder.flMonth11,
                viewHolder.flMonth12,
        };

        for (int i = 0; i < 12; i++) {
            if (studentPayment.getPayments()[i] == 1)
                frameLayouts[i].setBackgroundColor(Color.GREEN);
            else
                frameLayouts[i].setBackgroundColor(Color.BLACK);
        }
        viewHolder.tvStudentName.setTag(position);

        return convertView;
    }

}