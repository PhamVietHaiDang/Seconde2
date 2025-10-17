package com.schoolproject.seconde2.AdapterFragment;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.schoolproject.seconde2.R;

public class MenuAdapter extends ArrayAdapter<String> {

    private Context context;
    private String[] menuItems;
    private int[] icons = {
            R.drawable.ic_archive,
            R.drawable.ic_trash,
            R.drawable.ic_mark_unread,
            R.drawable.ic_warning
    };

    // Constants for better maintainability
    private static final int SPAM_ITEM_POSITION = 3;
    private static final int DEFAULT_TEXT_COLOR = Color.BLACK;
    private static final int SPAM_TEXT_COLOR = Color.RED;

    public MenuAdapter(Context context, String[] menuItems) {
        super(context, R.layout.menu_item, menuItems);
        this.context = context;
        this.menuItems = menuItems;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.menu_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.icon = convertView.findViewById(R.id.menu_icon);
            viewHolder.text = convertView.findViewById(R.id.menu_text);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        setupMenuItem(viewHolder, position);
        return convertView;
    }

    private void setupMenuItem(ViewHolder viewHolder, int position) {
        // Set icon and text
        viewHolder.icon.setImageResource(icons[position]);
        viewHolder.text.setText(menuItems[position]);

        // Set text color - spam item is red, others are black
        if (position == SPAM_ITEM_POSITION) {
            viewHolder.text.setTextColor(SPAM_TEXT_COLOR);
        } else {
            viewHolder.text.setTextColor(DEFAULT_TEXT_COLOR);
        }
    }

    // ViewHolder pattern for better performance
    private static class ViewHolder {
        ImageView icon;
        TextView text;
    }
}