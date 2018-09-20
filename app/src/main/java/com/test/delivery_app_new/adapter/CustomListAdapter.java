package com.test.delivery_app_new.adapter;



import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.test.delivery_app_new.R;
import com.test.delivery_app_new.app.AppController;
import com.test.delivery_app_new.model.Delivery;

public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Delivery> deliveryItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapter(Activity activity, List<Delivery> movieItems) {
        this.activity = activity;
        this.deliveryItems = movieItems;
    }

    @Override
    public int getCount() {
        return deliveryItems.size();
    }

    @Override
    public Object getItem(int location) {
        return deliveryItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);

        TextView title = (TextView) convertView.findViewById(R.id.title);

        // getting movie data for the row
        Delivery d = deliveryItems.get(position);

        // thumbnail image
        thumbNail.setImageUrl(d.getThumbnailUrl(), imageLoader);
        // title
        title.setText(d.getTitle());
        return convertView;
    }

}

