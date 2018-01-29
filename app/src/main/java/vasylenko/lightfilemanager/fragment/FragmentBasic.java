package vasylenko.lightfilemanager.fragment;

import android.support.v4.app.ListFragment;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public abstract class FragmentBasic extends ListFragment implements Actioanable {

    public abstract Set<String> getSelectedPath();
    public abstract int getSelectedPathCount();

    @Override
    public boolean createItem(int item, String pathItem, String nameItem) throws IOException {
        switch (item) {
            case 0:
                File newFolder = new File(pathItem + nameItem);
                if (!newFolder.exists()) {
                    newFolder.mkdir();
                    return true;
                } else {
                    return false;
                }
            case 1:
                File newFile = new File(pathItem + nameItem);
                if (newFile.createNewFile()) return true;
                else return false;
            default:
                return false;
        }
    }

    @Override
    public void deleteItems() throws IOException {
        File fileForDelete = null;
        for (String pathToItem : getSelectedPath()) {
            fileForDelete = new File(pathToItem);
            if (fileForDelete.exists())
                FileUtils.deleteQuietly(fileForDelete);
        }
    }

    @Override
    public void moveItems(String newPath) throws IOException {
        for (String pathToItem : getSelectedPath()) {
            FileUtils.moveToDirectory(
                    FileUtils.getFile(pathToItem),
                    FileUtils.getFile(newPath),
                    true
            );
        }
    }

    @Override
    public void copyItems(String newPath) throws IOException{
        for (String pathToItem : getSelectedPath()) {
            FileUtils.copyToDirectory(
                    FileUtils.getFile(pathToItem),
                    FileUtils.getFile(newPath)
            );
        }
    }

}
