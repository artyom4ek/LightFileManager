package vasylenko.lightfilemanager.model;

public class Item implements Comparable<Item>{
    private String itemName;
    private String itemSize;
    private String itemDate;
    private String itemPath;
    private String itemImage;

    public Item(String itemName, String itemSize, String itemDate, String itemPath, String itemImage)
    {
        this.itemName = itemName;
        this.itemSize = itemSize;
        this.itemDate = itemDate;
        this.itemPath = itemPath;
        this.itemImage = itemImage;
    }

    public String getItemName()
    {
        return itemName;
    }
    public String getItemSize()
    {
        return itemSize;
    }
    public String getItemDate()
    {
        return itemDate;
    }
    public String getItemPath() { return itemPath; }
    public String getItemImage() {
        return itemImage;
    }

    @Override
    public int compareTo(Item item) {
        if(this.itemName != null)
            return this.itemName.toLowerCase().compareTo(item.getItemName().toLowerCase());
        else
            throw new IllegalArgumentException();
    }

}