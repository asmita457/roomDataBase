package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class DisplayUsers extends AppCompatActivity
{
    ListView listView;
    List<Users>usersList;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_users);


        getAllIds();

        displayUsers();

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0,v.getId(),0,"Edit");
        menu.add(0,v.getId(),0,"Delete");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item)
    {
        if (item.getTitle()=="Edit")
        {

            BottomSheetDialogEdit bottomSheetDialogEdit=new BottomSheetDialogEdit();

            bottomSheetDialogEdit.show(getSupportFragmentManager(),"EditBottomSheet");


        }
        if (item.getTitle()=="Delete")
        {
            SharedPreferences sharedPreferences=getSharedPreferences("id", Context.MODE_PRIVATE);
            int  id=sharedPreferences.getInt("id",0);

            Users users=new Users();
            users.setId(id);
            AppDatabase appDatabase;

            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().userDao().delete(users);

            Intent intent=new Intent(DisplayUsers.this,DisplayUsers.class);
            startActivity(intent);
            this.finish();
        }

        return true;

    }

    private void editUser() {
    }

    private void displayUsers()
    {
        Users users=new Users();

        usersList=DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().userDao().gettUsers();
        CustomAdapter customAdapter=new CustomAdapter(this,usersList);
        listView.setAdapter(customAdapter);


    }

    private void getAllIds()
    {
        toolbar=findViewById(R.id.toolbar);
        listView=findViewById(R.id.usersList);

        registerForContextMenu(listView);

    }
}
