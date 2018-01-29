package vasylenko.lightfilemanager.fragment;

import java.io.IOException;

interface Actioanable {
    boolean createItem(int item, String pathItem, String nameItem) throws IOException;
    void deleteItems() throws IOException;
    void moveItems(String newPath) throws IOException;
    void copyItems(String newPath) throws IOException;
}
