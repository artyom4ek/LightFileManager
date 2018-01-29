package vasylenko.lightfilemanager.worker;

import java.io.File;
import java.util.List;

import vasylenko.lightfilemanager.adapter.ItemArrayAdapter;
import vasylenko.lightfilemanager.model.Item;

interface Actioanable {
    String PARENT_DIRECTORY_NAME = "Parent Directory";
    String DIRECTORY_ICON_NAME = "directory_icon";
    String FILE_ICON_NAME = "file_icon";
    String DIRECTORY_UP_ICON_NAME = "directory_up";

    List<Item> fillItemsList(File file) throws Exception;
    void selectedListItem(int position, ItemArrayAdapter itemArrayAdapterOne) throws Exception;
}
