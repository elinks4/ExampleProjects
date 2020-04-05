package com.example.myproject;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class MyAdapter extends ArrayAdapter<Result> {

    private Context context;
    ArrayList<Result> games;
    private int item_layout;

    public MyAdapter(Context context, int item_layout, ArrayList<Result> games) {
        super(context, item_layout, games);
        this.context = context;
        this.games = games;
        this.item_layout = item_layout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater. inflate(item_layout, null);    }
        ImageView tvPic = (ImageView) convertView.findViewById(R.id.gamePicture);
        TextView tvName = (TextView) convertView.findViewById(R.id.gameName);
        TextView tvreleaseDate = (TextView) convertView.findViewById(R.id.releaseDate);
        TextView tvRating = (TextView) convertView.findViewById(R.id.rating);
        TextView invisibleId = (TextView) convertView.findViewById(R.id.invisibleView);
        Result p = games.get(position);
        //tvPic.setImageURI(Uri.parse(p.getbackGroundImgURL()));
        Picasso.get().load(Uri.parse(p.getbackGroundImgURL())).resize(450, 235).into(tvPic);
        tvName.setText(p.getName());
        tvreleaseDate.setText(p.getReleaseDate());
        tvRating.setText(p.getRating() + " out of " + p.getRatingTop());
        invisibleId.setText(p.getId());
        return convertView;
    }
}
