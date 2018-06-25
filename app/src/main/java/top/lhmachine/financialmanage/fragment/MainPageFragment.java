package top.lhmachine.financialmanage.fragment;

import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

import top.lhmachine.financialmanage.R;
import top.lhmachine.financialmanage.sql.DBHelper;

/**
 * Created by lhmachine on 2018/6/25.
 * 主页
 */

public class MainPageFragment extends Fragment {

    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View mainpageView = inflater.inflate(R.layout.mainpage, container, false);

        //连接数据库
        dbHelper = new DBHelper(getContext(), "data.db", null, 1);
        db = dbHelper.getWritableDatabase();

        return mainpageView;
    }
}
