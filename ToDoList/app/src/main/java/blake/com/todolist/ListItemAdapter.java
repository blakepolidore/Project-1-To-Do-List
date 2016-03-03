package blake.com.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Raiders on 3/2/16.
 */
public class ListItemAdapter extends ArrayAdapter<ListItem> {

    public ListItemAdapter(Context context, ArrayList<ListItem> listItems) {
        super(context, 0, listItems);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        final ListItem listItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_row, parent, false);
        }

        final TextView itemTextTextView = (TextView) convertView.findViewById(R.id.item_text);

        itemTextTextView.setText(listItem.getItemText());


        return convertView;
    }
}
