package top.lhmachine.financialmanage.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.LocaleDisplayNames;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import top.lhmachine.financialmanage.R;
import top.lhmachine.financialmanage.activity.AddPurchaseActivity;
import top.lhmachine.financialmanage.sql.DBHelper;

import static android.app.Activity.RESULT_OK;

/**
 * Created by lhmachine on 2018/6/24.
 * 进货列表
 */

public class PurchaseFragment extends Fragment {

    private ListView mListView;
    private TextView no_purchase;
    private List<HashMap<String, String>> list = new ArrayList<>();
    private SimpleAdapter adapter;
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View purchaseView = inflater.inflate(R.layout.purchase_main, container, false);

        //连接数据库
        dbHelper = new DBHelper(getContext(), "data.db", null, 1);
        db = dbHelper.getWritableDatabase();

        //绑定控件
        Button add_purchase = (Button)purchaseView.findViewById(R.id.bt_add_purchase);
        mListView = (ListView)purchaseView.findViewById(R.id.purchase_menu);
        no_purchase = (TextView)purchaseView.findViewById(R.id.no_purchase);

        //初始化和显示数据
        initdata();

        //绑定监听事件
        add_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddPurchaseActivity.class);
                intent.putExtra("purchase_id", list.size()+1);
                startActivityForResult(intent, 300);
            }
        });

        return purchaseView;
    }

    //初始化数据
    private void initdata(){
        Cursor cursor = db.query("Purchase", null, null, null, null, null, null);
        if (cursor.moveToFirst()){
            do{
                HashMap<String, String> map = new HashMap<>();
                map.put("id", cursor.getString(0));
                map.put("time", cursor.getString(1));
                map.put("num", cursor.getString(2));
                map.put("price", cursor.getString(3));
                list.add(map);
            }while(cursor.moveToNext());
            //显示数据
            mListView.setVisibility(View.VISIBLE);
            no_purchase.setVisibility(View.GONE);
            adapter = new SimpleAdapter(getActivity(), list, R.layout.purchase_menu_item, new String[]{"id", "time", "num", "price"}, new int[]{R.id.purchase_item_id, R.id.purchase_item_time, R.id.purchase_item_num, R.id.purchase_item_price});
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getActivity(), "查看进货详情", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            no_purchase.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }
        cursor.close();
    }
}
