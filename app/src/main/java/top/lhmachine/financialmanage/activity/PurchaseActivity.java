package top.lhmachine.financialmanage.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.ProcessingInstruction;

import top.lhmachine.financialmanage.R;
import top.lhmachine.financialmanage.sql.DBHelper;

/**
 * @author lhmachine
 * 增加商品信息
 */

public class PurchaseActivity extends AppCompatActivity {

    private String product_id;      //商品编号
    private int purchase_id;        //进货编号
    private EditText input_num, input_purchase_price, input_advise_price; //输入控件

    private DBHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        //获取产品二维码数据
        final Intent intent = getIntent();
        product_id = intent.getStringExtra("product_id");
        purchase_id = intent.getIntExtra("purchase_id", 0);

        //连接数据库文件
        dbHelper = new DBHelper(this, "data.db",null,  1);
        db = dbHelper.getWritableDatabase();

        //获取控件
        TextView id_view = (TextView)findViewById(R.id.purchase_product_id);
        input_num = (EditText)findViewById(R.id.purchase_product_number);
        input_purchase_price = (EditText)findViewById(R.id.purchase_product_purchase_price);
        input_advise_price = (EditText)findViewById(R.id.purchase_product_advise_price);
        Button save = (Button)findViewById(R.id.purchase_product_save);
        Button back = (Button)findViewById(R.id.purchase_product_back);

        id_view.setText(product_id);

        //按钮监听
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(input_num.getText()) || input_num.getText().toString().equals("0")){
                    Toast.makeText(PurchaseActivity.this, "请输入采购数量!", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(input_purchase_price.getText()) || input_purchase_price.equals("0")){
                    Toast.makeText(PurchaseActivity.this, "请输入进货单价!", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(input_advise_price.getText()) || input_advise_price.equals("0")){
                    Toast.makeText(PurchaseActivity.this, "请输入建议售价!", Toast.LENGTH_SHORT).show();
                }else{
                    ContentValues values = new ContentValues();
                    values.put("purchase_id", purchase_id);
                    values.put("product_id", product_id);
                    values.put("product_pic", "");
                    values.put("num", Integer.valueOf(input_num.getText().toString()));
                    values.put("purchase_price", Integer.valueOf(input_purchase_price.getText().toString()));
                    values.put("advise_price", Integer.valueOf(input_advise_price.getText().toString()));
                    db.insert("Product", null, values);
                    Toast.makeText(PurchaseActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
}
