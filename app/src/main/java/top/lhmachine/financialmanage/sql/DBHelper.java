package top.lhmachine.financialmanage.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by 14501 on 2018/6/24.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String CREATE_PURCHASE = "create table Purchase(" +
            "id integer," +
            "time date, "+
            "num number," +
            "price number)";

    private static final String CREATE_PURCHASE_PRODUCT = "create table Product(" +
            "purchase_id integer," +
            "product_id varchar(20)," +
            "product_pic text,"+
            "num number," +
            "purchase_price number," +
            "advise_price number," +
            "sell_price number,"+
            "sell_num number)";

    private Context mContext;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_PURCHASE);
        db.execSQL(CREATE_PURCHASE_PRODUCT);
        Toast.makeText(mContext, "创建成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
    }

}
