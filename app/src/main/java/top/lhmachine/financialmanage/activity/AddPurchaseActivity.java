package top.lhmachine.financialmanage.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import top.lhmachine.financialmanage.R;
import top.lhmachine.financialmanage.sql.DBHelper;

/**
 * Created by lhmachine in 2018/06/25
 * 添加进货
 */

public class AddPurchaseActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private int purchase_id;
    private ListView product_menu;
    private TextView no_product;
    private SimpleAdapter adapter;
    private List<HashMap<String, String>> list = new ArrayList<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_purchase);

        //获取当前进货id
        final Intent intent = getIntent();
        purchase_id = intent.getIntExtra("purchase_id", 0);
        Log.d("当前进货id", String.valueOf(purchase_id));

        //连接数据库
        dbHelper = new DBHelper(AddPurchaseActivity.this, "data.db", null, 1);
        db = dbHelper.getWritableDatabase();

        //绑定控件
        Button save = (Button)findViewById(R.id.add_purchase_save);
        Button back = (Button)findViewById(R.id.add_purchase_back);
        Button add_product = (Button)findViewById(R.id.add_purchase_add_product);
        product_menu =(ListView)findViewById(R.id.add_purchase_product_menu);
        no_product = (TextView)findViewById(R.id.no_product);

        //绑定监听事件
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(AddPurchaseActivity.this);
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
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = 0;
                int price = 0;
                //查询总数
                Cursor cursor = db.rawQuery("SELECT count(num), count(purchase_price) FROM Product WHERE purchase_id = "+String.valueOf(purchase_id), null);
                if (cursor.moveToFirst()){
                    do{
                        num = cursor.getInt(0);
                        price = cursor.getInt(1);
                    }while (cursor.moveToNext());
                }
                if(num !=0 && price!=0){
                    //存储到进货表中
                    ContentValues values = new ContentValues();
                    values.put("id", purchase_id);
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);//设置日期格式
                    values.put("time", df.format(new Date()));
                    values.put("num", num);
                    values.put("price", price);
                    db.insert("Purchase", null, values);
                    Toast.makeText(AddPurchaseActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK, intent);
                    finish();
                }else{
                    Toast.makeText(AddPurchaseActivity.this, "请先添加产品信息", Toast.LENGTH_SHORT).show();
                }
                cursor.close();

            }
        });

        initdata();
    }

    //初始获取数据
    private void initdata(){
        Cursor cursor = db.rawQuery("SELECT * FROM Product WHERE purchase_id = "+String.valueOf(purchase_id)+";", null);
        if (cursor.moveToFirst()){
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put("product_id", cursor.getString(1));
                map.put("product_pic", cursor.getString(2));
                map.put("num", cursor.getString(3));
                map.put("sell_price", cursor.getString(5));
                list.add(map);
            }while(cursor.moveToNext());
            //显示数据
            product_menu.setVisibility(View.VISIBLE);
            no_product.setVisibility(View.GONE);
            adapter = new SimpleAdapter(AddPurchaseActivity.this, list, R.layout.product_item, new String[]{"product_id", "num", "sell_price"}, new int[]{R.id.product_id, R.id.product_num, R.id.product_price});
            product_menu.setAdapter(adapter);
        }else{
            no_product.setVisibility(View.VISIBLE);
        }
        cursor.close();
    }

    //条形码扫描结果回调
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 301){
            if (resultCode == RESULT_OK){
                initdata();
            }
        }else{
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (scanResult != null) {
                String result = scanResult.getContents();
                Log.d("条形码", result);
                Intent intent = new Intent(AddPurchaseActivity.this, PurchaseActivity.class);
                intent.putExtra("purchase_id", purchase_id);
                intent.putExtra("product_id", result);
                startActivityForResult(intent, 301);
            }else {
                Toast.makeText(AddPurchaseActivity.this, "未获取到任何条形码信息", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
