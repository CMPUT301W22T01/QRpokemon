package com.qrpokemon.qrpokemon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.Settings;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;
import com.yzq.zxinglibrary.encode.CodeCreator;

public class QRActivity extends AppCompatActivity implements View.OnClickListener {
    private Button scanBtn;
    private TextView result;
    private EditText contentEt;
    private int REQUEST_CODE_SCAN = 111;
    /**
     * Generate the QR code with logo
     */
    private Button encodeBtnWithLogo;
    private ImageView contentIvWithLogo;
    private String contentEtString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qractivity);
        initView();
    }


    private void initView() {
        //scan button
        scanBtn = findViewById(R.id.scanBtn);
        scanBtn.setOnClickListener(this);
        //scan result
        result = findViewById(R.id.result);

        //input field for QR code
        contentEt = findViewById(R.id.contentEt);
        result = (TextView) findViewById(R.id.result);
        scanBtn = (Button) findViewById(R.id.scanBtn);
        contentEt = (EditText) findViewById(R.id.contentEt);
        encodeBtnWithLogo = (Button) findViewById(R.id.encodeBtnWithLogo);
        encodeBtnWithLogo.setOnClickListener(this);
        contentIvWithLogo = (ImageView) findViewById(R.id.contentIvWithLogo);
    }

    @Override
    public void onClick(View v) {


        Bitmap bitmap = null;
        switch (v.getId()) {
            case R.id.scanBtn:
                AndPermission.with(this)
                        .runtime()
                        .permission(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE)
                        .onGranted(data -> {
                            Intent intent = new Intent(QRActivity.this, CaptureActivity.class);
                            /*ZxingConfig
                             * set additional features
                             * prompt tone
                             * border color
                             * */
                            ZxingConfig config = new ZxingConfig();
                            // config.setPlayBeep(false);//prompt tone(default:true)
                            // config.setShake(false);//mobile phone vibration(default:true)
                            // config.setDecodeBarCode(false);//Decode bar code(default:true)
                            // config.setReactColor(R.color.colorAccent);//React color(default:white)
                            // config.setFrameLineColor(R.color.colorAccent);//FrameLineColor(default:blank)
                            // config.setScanLineColor(R.color.colorAccent);//ScanLineColor(default:white)
                            config.setFullScreenScan(false);//scan in full screen(default:true)
                            intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                            startActivityForResult(intent, REQUEST_CODE_SCAN);
                        })
                        .onDenied(data -> {
                            Uri packageURI = Uri.parse("package:" + getPackageName());
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            startActivity(intent);

                            Toast.makeText(QRActivity.this, "no permission", Toast.LENGTH_LONG).show();
                        })
                        .start();

                break;

            case R.id.encodeBtnWithLogo:

                contentEtString = contentEt.getText().toString().trim();
                if (TextUtils.isEmpty(contentEtString)) {
                    Toast.makeText(QRActivity.this, "input string", Toast.LENGTH_SHORT).show();
                    return;
                }

                Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                try {
                    bitmap = CodeCreator.createQRCode(contentEtString, 400, 400, logo);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (bitmap != null) {
                    contentIvWithLogo.setImageBitmap(bitmap);
                }

                break;
            default:
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Back parameters
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                result.setText("scan content：" + content);
            }
        }
    }


}