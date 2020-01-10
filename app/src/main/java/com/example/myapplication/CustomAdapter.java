package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class CustomAdapter extends BaseAdapter
{

    Context context;
    List<Users>usersList;
    LayoutInflater layoutInflater;
    View view;
    public CustomAdapter(Context context, List<Users> usersList)
    {
        this.context = context;
        this.usersList = usersList;

        layoutInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount()
    {
        return usersList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view=layoutInflater.inflate(R.layout.users_layout,null);
final Users users=usersList.get(position);
        TextView txtName=view.findViewById(R.id.username);
        TextView tvDate=view.findViewById(R.id.date);
        TextView tvEmail=view.findViewById(R.id.email);
        TextView tvgender=view.findViewById(R.id.tvGender);
        TextView tvProfession=view.findViewById(R.id.tvProfession);
        ImageView imageView=view.findViewById(R.id.img);
        RelativeLayout relativeLayout=view.findViewById(R.id.rl);

        relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v)
            {
                SharedPreferences sharedPreferences=context.getSharedPreferences("id",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putInt("id",users.getId());
                editor.putString("name",users.getName());
                editor.putString("email",users.getEmail());
                editor.putString("date",users.getDate());
                editor.putString("gender",users.getGender());
                editor.putString("profession",users.getProfession());
                editor.putString("url",users.getUrl());
                editor.apply();
                editor.commit();

                return false;
            }
        });
        Picasso.with(context).load(users.getUrl()).into(imageView);
        tvEmail.setText(users.getEmail());
        tvDate.setText("Birth Date:-"+users.getDate());
        txtName.setText(users.getName());
        tvgender.setText(users.getGender());
        tvProfession.setText(users.getProfession());

        return view;
    }
}
