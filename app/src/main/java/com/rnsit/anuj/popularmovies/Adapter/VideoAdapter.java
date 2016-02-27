package com.rnsit.anuj.popularmovies.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rnsit.anuj.popularmovies.Contents.VideoContents;
import com.rnsit.anuj.popularmovies.R;

import java.util.ArrayList;

/**
 * Created by anuj on 2/13/2016.
 */
public class VideoAdapter extends ArrayAdapter<VideoContents> {
    Context context;
    int resource;
    ArrayList<VideoContents> objects;

    public VideoAdapter(Context context, int resource, ArrayList<VideoContents> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Trailers trailers;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);
            trailers = new Trailers();
            trailers.trailerTitle = (TextView) convertView.findViewById(R.id.item_trailer_name);
            convertView.setTag(trailers);
        } else {
            trailers = (Trailers) convertView.getTag();
        }
        VideoContents contents = objects.get(position);
        trailers.trailerTitle.setText(contents.getNAME());
        return convertView;
    }

    private class Trailers {
        TextView trailerTitle;
    }
}
