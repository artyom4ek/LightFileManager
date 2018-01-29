package vasylenko.lightfilemanager.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import vasylenko.lightfilemanager.R;
import vasylenko.lightfilemanager.model.Item;

public class ItemArrayAdapter extends ArrayAdapter<Item> {
    private Context context;
    private int textViewResourceId;
    private List<Item> items;
    private boolean[] selections;

    public ItemArrayAdapter(Context context, int textViewResourceId, List<Item> items) {
        super(context, textViewResourceId, items);
        this.context = context;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        this.selections = new boolean[items.size()];
    }

    // ------------------------------------------------------------------------------------------ //
    public List<Item> getItems() { return items; }
    public Item getItem(int i)
    {
        return items.get(i);
    }

    public void switchSelection(int position){
        selections[position] = !selections[position];
        notifyDataSetChanged();
    }

    // ------------------------------------------------------------------------------------------ //
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater LayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = LayoutInflater.inflate(textViewResourceId, null);
        }

        boolean isSelected = selections[position];
        if (isSelected){
            view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorBackgroundSelectItem));
        }else {
            view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorBackgroundBasic));
        }

        final Item item = items.get(position);
        if (item != null) {
            TextView itemName = (TextView) view.findViewById(R.id.item_name);
            TextView itemSize = (TextView) view.findViewById(R.id.item_size);
            TextView itemDateCreation = (TextView) view.findViewById(R.id.item_date_creation);

            ImageView itemIcon = (ImageView) view.findViewById(R.id.item_icon);
            String uri = "drawable/" + item.getItemImage();
            int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
            Drawable image = context.getResources().getDrawable(imageResource);
            itemIcon.setImageDrawable(image);

            if(itemName!=null)
                itemName.setText(item.getItemName());
            if(itemSize!=null)
                itemSize.setText(item.getItemSize());
            if(itemDateCreation!=null)
                itemDateCreation.setText(item.getItemDate());
        }

        return view;
    }

}
