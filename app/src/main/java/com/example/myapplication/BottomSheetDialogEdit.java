package com.example.myapplication;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class BottomSheetDialogEdit extends BottomSheetDialogFragment
{

    private String selectedGender,selectedProfession;
    private Bottomsheetistener bottomsheetistener;
    SharedPreferences sharedPreferences;
    private int REQ_ID=123;
    private int GALLERY = 1;
    public static final int RESULT_OK=-1;
     CircleImageView circleImageView;
     Uri uri;
     Spinner spGender;
     Spinner spProfession;
     String []gender={"Male","Female"};
     String []strprofession={"Self-employed","Un-employed"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View view=inflater.inflate(R.layout.bottomsheet_layout,container,false);
         sharedPreferences=getContext().getSharedPreferences("id",Context.MODE_PRIVATE);



        getIds(view);


        return view;
    }


    private void getIds(View view)
    {
         circleImageView=view.findViewById(R.id.circleImageView);
        final EditText etName=view.findViewById(R.id.etName);
        final EditText etemail=view.findViewById(R.id.etemail);
        final EditText etDate=view.findViewById(R.id.etdate);
         spGender=view.findViewById(R.id.spGender);
         spProfession=view.findViewById(R.id.spProfession);
        final Button updateBtn=view.findViewById(R.id.updateBtn);

        ArrayAdapter genderarrayAdapter=new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item,gender);
        ArrayAdapter professionAdapter=new ArrayAdapter(getContext(),android.R.layout.simple_spinner_dropdown_item,strprofession);
        spGender.setAdapter(genderarrayAdapter);
        spProfession.setAdapter(professionAdapter);

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
            checkPermission();
            }
        });
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                etDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                datePickerDialog.show();


            }
        });
        spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selectedGender=gender[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spProfession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedProfession=strprofession[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Users users=new Users();
                users.setId(sharedPreferences.getInt("id",0));
                users.setName(etName.getText().toString());
                users.setEmail(etemail.getText().toString());
                users.setDate(etDate.getText().toString());
                users.setGender(selectedGender);
                users.setProfession(selectedProfession);


if (uri!=null)
{
    users.setUrl(uri.toString());

}
else
{
 String url=sharedPreferences.getString("url","");
    uri= Uri.parse(url);
users.setUrl(uri.toString());
}

                AppDatabase appDatabase;

                DatabaseClient.getInstance(getContext()).getAppDatabase().userDao().update(users);
                Intent intent=new Intent(getContext(),DisplayUsers.class);
                startActivity(intent);

            }
        });

        etName.setText(sharedPreferences.getString("name",""));
        etDate.setText(sharedPreferences.getString("date",""));
        etemail.setText(sharedPreferences.getString("email",""));
        Picasso.with(getContext()).load(sharedPreferences.getString("url","")).into(circleImageView);



    }

    private void checkPermission()
    {
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M)
        {

            if (getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED)
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
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK && requestCode==GALLERY)
        {
            uri=data.getData();

            circleImageView.setImageURI(data.getData());
        }
        if (data!=null)
        {
            // uri=data.getData();
            try
            {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                circleImageView.setImageBitmap(bitmap);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getContext(), "Failed to browse image", Toast.LENGTH_SHORT).show();
            }
        }



    }


    public interface Bottomsheetistener
    {
        void onButtonClicked(String text);


    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        try {

            bottomsheetistener=(Bottomsheetistener) context;

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
