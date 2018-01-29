package vasylenko.lightfilemanager.model;

public class HistoryChanges {
    private int id;
    private String operation;
    private String date;

    public HistoryChanges(int id, String date, String operation) {
        this.id = id;
        this.date = date;
        this.operation = operation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

}
