package vasylenko.lightfilemanager.worker;

import android.app.Activity;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import vasylenko.lightfilemanager.R;

public class StyleWorker {

    public static void setStylePaneMode(Activity activity){
        int index = PreferenceManager.getDefaultSharedPreferences(activity).getInt("tabSelectedIndex", 0);
        String fragmentOneCurrentDir =  PreferenceManager.getDefaultSharedPreferences(activity)
                .getString("fragmentOneCurrentDir", "/sdcard/");
        String fragmentTwoCurrentDir =  PreferenceManager.getDefaultSharedPreferences(activity)
                .getString("fragmentTwoCurrentDir", "/sdcard/");

        if(!activity.getResources().getBoolean(R.bool.twoPaneMode)) {
            TabLayout tabLayout = (TabLayout)activity.findViewById(R.id.tab_layout);
            tabLayout.getTabAt(0).setText(fragmentOneCurrentDir);
            tabLayout.getTabAt(1).setText(fragmentTwoCurrentDir);
        }else{
            TextView pathItemOneView = (TextView)activity.findViewById(R.id.path_item_one_view);
            pathItemOneView.setText(fragmentOneCurrentDir);

            TextView pathItemTwoView = (TextView)activity.findViewById(R.id.path_item_two_view);
            pathItemTwoView.setText(fragmentTwoCurrentDir);

            if (index == 0) {
                pathItemOneView.setTypeface(null, Typeface.BOLD);
                pathItemOneView.setTextColor(ContextCompat.getColor(activity, R.color.colorTabLayoutIndicator));
                pathItemTwoView.setTypeface(null, Typeface.NORMAL);
                pathItemTwoView.setTextColor(ContextCompat.getColor(activity, R.color.colorTextBasic));
            } else if (index == 1) {
                pathItemTwoView.setTypeface(null, Typeface.BOLD);
                pathItemTwoView.setTextColor(ContextCompat.getColor(activity, R.color.colorTabLayoutIndicator));
                pathItemOneView.setTypeface(null, Typeface.NORMAL);
                pathItemOneView.setTextColor(ContextCompat.getColor(activity, R.color.colorTextBasic));
            }
        }
    }

}
