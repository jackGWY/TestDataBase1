package com.guo.testdatabase;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button mBtn_CreateBase,mBtn_addData,mBtn_quaryButton,mBtn_update;
    private MyDatabaseHelper dbHelper;
    private AFragment aFragment=new AFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getFragmentManager().beginTransaction().add(R.id.fragmentContainer,aFragment,"a").
                commitAllowingStateLoss();
        dbHelper=new MyDatabaseHelper(this,"BookSttore.db",null,2);
        mBtn_CreateBase=(Button) findViewById(R.id.btn_createdatabase);
        mBtn_CreateBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建或打开现有的数据库
                dbHelper.getWritableDatabase();
            }
        });
        mBtn_addData=(Button)findViewById(R.id.btn_add_data);
        mBtn_addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                ContentValues values=new ContentValues();
                values.put("name","The Da Vinci Code");
                values.put("price",20);
                values.put("author","scott");
                db.insert("Book",null,values);
                values.clear();
                values.put("name","The Lost Symbol");
                values.put("author","scott");
                values.put("price",500);
                db.insert("Book",null,values);
            }
        });

        mBtn_quaryButton=(Button)findViewById(R.id.btn_quary_button);
        mBtn_quaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                //指定去查book
                Cursor cursor=db.query("Book",
                        null,null,null,
                        null,null,null);
                //调用moveToFirst()将指针移动到第一行的位置
                if(cursor.moveToFirst()){
                    do{
                        String name=cursor.getString(cursor.getColumnIndex("name"));
                        String author=cursor.getString(cursor.getColumnIndex("author"));
                        double price=cursor.getDouble(cursor.getColumnIndex("price"));
                        Log.d("Mainactivity","book name is"+name);
                        Log.d("Mainactivity","book author is"+author);
                        Log.d("mainactivity","book price is "+price);

                    }while (cursor.moveToNext());
                }
                cursor.close();
            }
        });
        mBtn_update=(Button)findViewById(R.id.btn_update);
        mBtn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("price",19.99);
                //仔细update中提示的参数（String table,ContentValues,String whereClause,String[] whereArgs）
                //第三滴四行指定具体更新那几行。注意第三个参数中的？是一个占位符，通过第四个参数为第三个参数中占位符指定相应的内容。
                db.update("Book",values,"name=?",new String[]{"The Da Vinci Code"});

            }
        });
    }
}
