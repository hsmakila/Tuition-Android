package net.tution.thilini.tution.Activities.Viewers;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import net.tution.thilini.tution.R;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class QR_Viewer extends Activity {

    String TAG = "QR_Viewer";
    String id;

    ImageView ivQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_viewr);

        setTitle("Add Home_Student");

        init_Fields();

        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        loadQR();

    }

    private void init_Fields() {
        ivQR = findViewById(R.id.ivQR);
    }

    private void loadQR(){
        QRGEncoder qrgEncoder = new QRGEncoder(id, null, QRGContents.Type.TEXT, 500);
        try {
            Bitmap bitmap = qrgEncoder.encodeAsBitmap();
            ivQR.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.v(TAG, e.toString());
        }
    }

}
