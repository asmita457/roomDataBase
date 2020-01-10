package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
{


    ImageView imageView;
    Button buttonSave,btnNext;
    EditText etName,etDate,etEmail;
    String name,date,selectedgender,selectedprofession,email;

    Spinner spGender,spProfession;
    private AppDatabase appDatabase;
    private int REQ_ID=123;
    private int GALLERY = 1;
    SharedPreferences sharedPreferences;
    String[] gender={"Male","Female"};
    String[] profession={"Self-employed","Un-employed"};
    ArrayAdapter genderAdapter,professionAdapter;

    Uri uri;
    Calendar c1;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        c1=Calendar.getInstance();
        getAllIds();
        events();
    }

    private void events()
    {
        etDate.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {

                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                etDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                datePickerDialog.show();




            }
        });

        spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedgender=gender[position];

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spProfession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedprofession=profession[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
pickImage();
                checkpermision();


            }
        });
        btnNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(MainActivity.this,DisplayUsers.class);
                startActivity(intent);

            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                name=etName.getText().toString().trim();
                date=etDate.getText().toString().trim();
                email=etEmail.getText().toString();

                
                if (name.isEmpty() || date.isEmpty() || uri==null)
                {
                    Toast.makeText(MainActivity.this, "All fields are mendatory", Toast.LENGTH_SHORT).show();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches())
                {
                    Toast.makeText(MainActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    Users users=new Users();

                    if (sharedPreferences.contains(" "))
                    {
                        users.setId(1);
                    }
                    else
                    {
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("key","add");
                        editor.apply();
                        editor.commit();
                    }
                    //users.setId();
                    users.setUrl(uri.toString());
                    users.setName(name);
                    users.setDate(date);
                    users.setEmail(email);
                    users.setGender(selectedgender);
                    users.setProfession(selectedprofession);



                    DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().userDao().insert(users);
                    Toast.makeText(MainActivity.this, "User saved successfully", Toast.LENGTH_SHORT).show();



                    Intent intent=new Intent(MainActivity.this,DisplayUsers.class);
                    startActivity(intent);


                }

            }
        });
    }

    private void checkpermision()
    {
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M)
        {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED)
            {

                String []permissions={Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions,GALLERY);
            }
            else
            {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY);


            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK && requestCode==GALLERY)
        {
            uri=data.getData();

            imageView.setImageURI(data.getData());
        }


        if (data!=null)
        {
            // uri=data.getData();
            try
            {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                imageView.setImageBitmap(bitmap);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(this, "Failed to browse image", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void pickImage()
    {
        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

    }

    private void getAllIds()
    {
        sharedPreferences=getSharedPreferences("user",MODE_PRIVATE);

        etEmail=findViewById(R.id.etemail);
        btnNext=findViewById(R.id.next);
        imageView=findViewById(R.id.imageView);
        buttonSave=findViewById(R.id.btnSave);
        etName=findViewById(R.id.etName);
        etDate=findViewById(R.id.etDate);

        spGender=findViewById(R.id.spGender);
        spProfession=findViewById(R.id.spprofession);

        genderAdapter=new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,gender);
        professionAdapter=new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,profession);

        spGender.setAdapter(genderAdapter);
        spProfession.setAdapter(professionAdapter);

    }
}
