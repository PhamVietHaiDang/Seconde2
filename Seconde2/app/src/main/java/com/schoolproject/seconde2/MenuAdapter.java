package com.schoolproject.seconde2;

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

public class MenuAdapter extends ArrayAdapter<String> {

    private Context context;
    private String[] menuItems;
    private int[] icons = {
            R.drawable.ic_archive,
            R.drawable.ic_trash,
            R.drawable.ic_mark_unread,
            R.drawable.ic_warning
    };

    public MenuAdapter(Context context, String[] menuItems) {
        super(context, R.layout.menu_item, menuItems);
        this.context = context;
        this.menuItems = menuItems;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Create a new view if we don't have one to reuse
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.menu_item, parent, false);
        }

        // Find the icon and text views
        ImageView icon = convertView.findViewById(R.id.menu_icon);
        TextView text = convertView.findViewById(R.id.menu_text);

        // Set the icon and text for this menu item
        icon.setImageResource(icons[position]);
        text.setText(menuItems[position]);

        // Make the "Report as spam" item red (it's the 4th item)
        if (position == 3) {
            text.setTextColor(Color.RED);
        } else {
            // All other items are black
            text.setTextColor(Color.BLACK);
        }

        return convertView;
    }
}