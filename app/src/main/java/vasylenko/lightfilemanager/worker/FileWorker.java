package vasylenko.lightfilemanager.worker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ListFragment;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Formatter;
import java.util.List;

import vasylenko.lightfilemanager.adapter.ItemArrayAdapter;
import vasylenko.lightfilemanager.model.Item;

public class FileWorker implements Actioanable {
    private Activity activity;
    private ItemArrayAdapter itemArrayAdapter;
    private ListFragment fragment;
    private String currentDir;

    public FileWorker(Activity activity, ListFragment fragment) {
        this.activity = activity;
        this.fragment = fragment;
    }

    // ------------------------------------------------------------------------------------------ //
    @Override
    public List<Item> fillItemsList(File file) throws Exception {
        File[] dirs = file.listFiles();
        List<Item> dirsList = new ArrayList<Item>();
        List<Item> filesList = new ArrayList<Item>();

        for (File dirItem : dirs) {
            Date lastModifiedDate = new Date(dirItem.lastModified());
            DateFormat dateFormat = DateFormat.getDateTimeInstance();
            String dateModify = dateFormat.format(lastModifiedDate);

            if (dirItem.isDirectory()) {
                File[] dirListFiles = dirItem.listFiles();
                int countDirs = 0;
                if (dirListFiles != null) countDirs = dirListFiles.length;
                else countDirs = 0;

                String countItems = String.valueOf(countDirs);
                if (countDirs == 0) countItems = countItems + " item";
                else countItems = countItems + " items";

                dirsList.add(
                        new Item(dirItem.getName(), countItems, dateModify, dirItem.getAbsolutePath(), DIRECTORY_ICON_NAME)
                );
            } else {
                filesList.add(
                        new Item(dirItem.getName(), dirItem.length() + " Byte", dateModify, dirItem.getAbsolutePath(), FILE_ICON_NAME)
                );
            }
        }

        Collections.sort(dirsList);
        Collections.sort(filesList);
        dirsList.addAll(filesList);

        if (file.getParent() != null && !file.getParent().equalsIgnoreCase("//")) {
            dirsList.add(
                    0, new Item("..", PARENT_DIRECTORY_NAME, "", file.getParent(), DIRECTORY_UP_ICON_NAME)
            );
        }

        return dirsList;
    }

    // ------------------------------------------------------------------------------------------ //
    @Override
    public void selectedListItem(int position, ItemArrayAdapter adapter) throws Exception {
        Item item = adapter.getItem(position);
        if (item.getItemImage().equalsIgnoreCase(DIRECTORY_ICON_NAME)
                || item.getItemImage().equalsIgnoreCase(DIRECTORY_UP_ICON_NAME)) {
            fillItemsList(new File(item.getItemPath()));
            currentDir = item.getItemPath();
        } else {
            onItemClick(item);
        }
    }

    private void onItemClick(Item item) {
        Intent myIntent = new Intent(Intent.ACTION_VIEW);
        File file = new File(item.getItemPath());
        String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
        String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        myIntent.setDataAndType(Uri.fromFile(file), mimetype);
        activity.startActivity(myIntent);
    }

    // ------------------------------------------------------------------------------------------ //
    public static String convertFileToHex(String path) throws IOException {
        Formatter formatter = new Formatter();
        byte[] data = FileUtils.readFileToByteArray(new File(path));
        for (byte b : data)
            formatter.format("%02x", b);
        String result = formatter.toString();
        result = result.toUpperCase().replaceAll("..(?!$)", "$0 ");
        formatter.close();
        return result;
    }

    // ------------------------------------------------------------------------------------------ //
    public String getCurrentDir() {
        return currentDir;
    }

}
