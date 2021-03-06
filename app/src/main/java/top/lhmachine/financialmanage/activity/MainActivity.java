package top.lhmachine.financialmanage.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.icu.text.LocaleDisplayNames;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import top.lhmachine.financialmanage.R;
import top.lhmachine.financialmanage.fragment.MainPageFragment;
import top.lhmachine.financialmanage.fragment.PurchaseFragment;
import top.lhmachine.financialmanage.fragment.SellFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //绑定控件
        TextView mainpage = (TextView)findViewById(R.id.main);
        TextView sell = (TextView)findViewById(R.id.sell);
        TextView purchase = (TextView)findViewById(R.id.purchase);

        //初始页面
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();  //开始事务
        fragmentTransaction.replace(R.id.real_content, new MainPageFragment());
        fragmentTransaction.commit();

        //绑定监听
        mainpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();  //开始事务
                fragmentTransaction.replace(R.id.real_content, new MainPageFragment());
                fragmentTransaction.commit();
            }
        });

        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();  //开始事务
                fragmentTransaction.replace(R.id.real_content, new SellFragment());
                fragmentTransaction.commit();
            }
        });

        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();  //开始事务
                fragmentTransaction.replace(R.id.real_content, new PurchaseFragment());
                fragmentTransaction.commit();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("请求码", String.valueOf(requestCode));
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null) {
            String result = scanResult.getContents();
            Log.d("商品条形码", result);
        }else{
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();  //开始事务
            fragmentTransaction.replace(R.id.real_content, new PurchaseFragment());
            fragmentTransaction.commit();
        }
    }
}
