package net.tution.thilini.tution.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.tution.thilini.tution.R;

public class Payment_Slip extends Dialog implements View.OnClickListener {

    Button butPrint;
    TextView tvName, tvYear, tvMonth, tvAmountPaid;

    int month;
    String name, year, amountPaid;

    Context context;

    public Payment_Slip(@NonNull Context context, String name, String year, int month, String amountPaid) {
        super(context);
        this.context = context;

        this.name = name;
        this.year = year;
        this.month = month;
        this.amountPaid = amountPaid;
    }

    private void initiate_Fields() {
        butPrint = findViewById(R.id.butPrint);

        tvName = findViewById(R.id.tvName);
        tvYear = findViewById(R.id.tvYear);
        tvMonth = findViewById(R.id.tvMonth);
        tvAmountPaid = findViewById(R.id.tvAmountPaid);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_payment_slip);

        initiate_Fields();

        tvName.setText(name);
        tvYear.setText(year);
        tvAmountPaid.setText("Rs. " + amountPaid);
        switch (month) {
            case 1:
                tvMonth.setText("January");
                break;
            case 2:
                tvMonth.setText("February");
                break;
            case 3:
                tvMonth.setText("March");
                break;
            case 4:
                tvMonth.setText("April");
                break;
            case 5:
                tvMonth.setText("May");
                break;
            case 6:
                tvMonth.setText("June");
                break;
            case 7:
                tvMonth.setText("July");
                break;
            case 8:
                tvMonth.setText("August");
                break;
            case 9:
                tvMonth.setText("September");
                break;
            case 10:
                tvMonth.setText("October");
                break;
            case 11:
                tvMonth.setText("November");
                break;
            case 12:
                tvMonth.setText("December");
                break;
        }

        butPrint.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.butPrint:
                Toast.makeText(context, "Printing...", Toast.LENGTH_SHORT).show();
                dismiss();
                break;
            default:
                break;
        }
    }
}
