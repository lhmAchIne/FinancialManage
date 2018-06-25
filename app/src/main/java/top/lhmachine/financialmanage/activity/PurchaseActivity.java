package top.lhmachine.financialmanage.activity;

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
        Intent intent = getIntent();
        product_id = intent.getStringExtra("product_id");
        purchase_id = intent.getIntExtra("purchase_id", 0);

        //连接数据库文件
        dbHelper = new DBHelper(this, "data.db",null,  1);
        db = dbHelper.getWritableDatabase();

        //获取控件
        TextView id_view = (TextView)findViewById(R.id.purchase_product_id);
        input_num = (EditText)findViewById(R.id.purchase_product_number);
        input_purchase_price = (EditText)findViewById(R.id.purchase_product_purchase_price);
        input_advise_price = (EditText)findViewById(R.id.purchase_product_sell_price);
        Button save = (Button)findViewById(R.id.purchase_product_save);
        Button back = (Button)findViewById(R.id.purchase_product_back);

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
                    db.execSQL("INSERT INTO Product(purchase_id, product_id, product_pic, num, purchase_price, advise_price, sell_price, sell_num) VALUES ("+
                            String.valueOf(purchase_id)+", "+
                            product_id+
                            "', null,  "+
                            input_num.getText().toString()+", "+
                            input_purchase_price.getText().toString()+", "+
                            input_advise_price.getText().toString()+
                            " null, 0)");
                    Toast.makeText(PurchaseActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
