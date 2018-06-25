package top.lhmachine.financialmanage.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;

import top.lhmachine.financialmanage.R;

/**
 * Created by 14501 on 2018/6/25.
 * 售卖页面，扫描商品条形码
 */

public class SellFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View sellView = inflater.inflate(R.layout.sell, container, false);

        //绑定控件
        Button scan = (Button)sellView.findViewById(R.id.scan_code);
        //绑定监听事件
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(getActivity());
                // 设置要扫描的条码类型，ONE_D_CODE_TYPES：一维码，QR_CODE_TYPES-二维码
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                integrator.setCaptureActivity(CaptureActivity.class);           //设置打开摄像头的Activity
                integrator.setPrompt("请扫描条形码");                             //底部的提示文字，设为""可以置空
                integrator.setCameraId(0);                                      //前置或者后置摄像头
                integrator.setBeepEnabled(true);                                //扫描成功的「哔哔」声，默认开启
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        });

        return sellView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null) {
            String result = scanResult.getContents();
            Log.d("条形码", result);
        }
    }
}
