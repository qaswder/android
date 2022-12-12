package com.example.lab5.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Users.db";
    private String message = "null";
    Context context;
    View view;
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + DBContract.UserEntry.TABLE_NAME + "("
                + DBContract.UserEntry.COLUMN_NAME_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DBContract.UserEntry.COLUMN_NAME_LOGIN + " TEXT,"
                + DBContract.UserEntry.COLUMN_NAME_PASS + " TEXT" + ")";

        sqLiteDatabase.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBContract.UserEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    int i=0;

    public void addUser(User user, View view, Button button) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                button.post(new Runnable() {
                    @Override
                    public void run() {

                        button.setEnabled(false);
                    }
                });
                DatabaseHandler.this.view = view;
                i++;
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i("Thread_DB"+Integer.toString(i), Thread.currentThread().getName() + " start");
                SQLiteDatabase sqLiteDatabase = DatabaseHandler.this.getWritableDatabase();
                ContentValues values = new ContentValues();
                boolean flag = true;
                List<User> userList;
                userList = DatabaseHandler.this.getAllUsers();

                if (!userList.isEmpty()) {
                    for (User userGetDB : userList) {
                        String log = userGetDB.getLogin();
                        Log.i("DB user login ", log + " " + user.getLogin());
                        if (log.equals(user.getLogin())) {
                            flag = false;
                            view.post(() -> Toast.makeText(context, "Логин не доступен", Toast.LENGTH_SHORT).show());
                            break;
                        }
                    }
                    if (flag == true) {
                        values.put(DBContract.UserEntry.COLUMN_NAME_LOGIN, user.getLogin());
                        values.put(DBContract.UserEntry.COLUMN_NAME_PASS, user.getPass());
                        sqLiteDatabase.insert(DBContract.UserEntry.TABLE_NAME, null, values);
                        view.post(() -> Toast.makeText(context, "Пользователь зарегистрирован <Логин доступен>", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    values.put(DBContract.UserEntry.COLUMN_NAME_LOGIN, user.getLogin());
                    values.put(DBContract.UserEntry.COLUMN_NAME_PASS, user.getPass());
                    sqLiteDatabase.insert(DBContract.UserEntry.TABLE_NAME, null, values);
                    view.post(() -> Toast.makeText(context, "Пользователь зарегистрирован <Логин доступен>", Toast.LENGTH_SHORT).show());
                }
                sqLiteDatabase.close();
                button.post(new Runnable() {
                    @Override
                    public void run() {

                        button.setEnabled(true);
                    }
                });
            }
        }).start();
    }

    public List<User> getAllUsers() {

        final List<User> usersList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Runnable runThr = new Runnable(){
            @Override
            public  void run(){
                String selectQuery = "SELECT * FROM " + DBContract.UserEntry.TABLE_NAME;
                Cursor cursor = db.rawQuery(selectQuery, null);

                if (cursor.moveToFirst()) {
                    do {
                        User user = new User();
                        user.setID(Integer.parseInt(cursor.getString(0)));
                        user.setLogin(cursor.getString(1));
                        user.setPass(cursor.getString(2));
                        usersList.add(user);
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        };
        Thread thread = new Thread(runThr);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return usersList;
        /*List<User> usersList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DBContract.UserEntry.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setID(Integer.parseInt(cursor.getString(0)));
                user.setLogin(cursor.getString(1));
                user.setPass(cursor.getString(2));
                usersList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return usersList;*/
    }

    public List<User> getUser(String login){

        SQLiteDatabase db = this.getWritableDatabase();
        final List<User> userLog = new ArrayList<>();
        Runnable runThr = new Runnable(){
            @Override
            public  void run(){
                Log.i("ThreadDB", "Thread Start");
                String selectQuery = "SELECT * FROM " + DBContract.UserEntry.TABLE_NAME + " WHERE " + DBContract.UserEntry.COLUMN_NAME_LOGIN + " =?";

                Cursor cursor = db.rawQuery(selectQuery, new String[]{login});

                if(cursor.getCount() != 0){
                    if (cursor.moveToFirst()) {
                        do {
                            User user = new User();
                            user.setLogin(cursor.getString(1));
                            user.setPass(cursor.getString(2));
                            userLog.add(user);
                        } while (cursor.moveToNext());
                    }
                }
                Log.i("ThreadDB", "User");
                cursor.close();
            }
        };
            Log.i("ThreadDB", "Thread 1");
        Thread thread = new Thread(runThr);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i("ThreadDB", "return");

/*        String selectQuery = "SELECT * FROM " + DBContract.UserEntry.TABLE_NAME + " WHERE " + DBContract.UserEntry.COLUMN_NAME_LOGIN + " =?";
        String userLog = null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{login});

        if(cursor.getCount() != 0){
            if (cursor.moveToFirst()) {
                do {
                    User user = new User();
                    user.setLogin(cursor.getString(1));
                    user.setPass(cursor.getString(2));
                    userLog = user.getLogin() + " " + user.getPass();
                } while (cursor.moveToNext());
            }
        }
        else{
            userLog = "null";
        }
        cursor.close();*/
        return userLog;
    }

    public void updatePassword(String login, String newPass) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        new Thread(new Runnable() {
            @Override
            public void run() {

                ContentValues values = new ContentValues();

                values.put(DBContract.UserEntry.COLUMN_NAME_PASS, newPass);

                sqLiteDatabase.update(DBContract.UserEntry.TABLE_NAME, values, DBContract.UserEntry.COLUMN_NAME_LOGIN + "=?", new String[]{login});
                sqLiteDatabase.close();
            }
        }).start();

    }
    public void deleteUser(String login){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        new Thread(new Runnable() {
            @Override
            public void run() {
                sqLiteDatabase.delete(DBContract.UserEntry.TABLE_NAME, DBContract.UserEntry.COLUMN_NAME_LOGIN + "=?", new String[]{login});
                sqLiteDatabase.close();
            }
        }).start();

    }

}
