package com.eleventigers.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eleventigers.app.ImageFetcher;
import com.eleventigers.app.R;
import com.eleventigers.app.models.Scene;
import com.squareup.picasso.Picasso;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.util.functions.Action1;

/**
 * Created by eleventigers on 14/02/14.
 */
public class ScenesListViewAdapter extends ArrayAdapter<Scene> {

    Context context;

    public ScenesListViewAdapter(
            Context context,
            int resourceId,
            List<Scene> items
    ) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Scene rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.scene_item, null);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtTitle.setText(rowItem.toString());

        Picasso.with(context).load(rowItem.getImageUrl()).into(holder.imageView);

        return convertView;
    }
}
