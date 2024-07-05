import java.util.UUID;

public class Table {
    private final String idOfTable; // identification of the table, for the sake of simplicity I'll use UUID.randomUUID
    private final int size; // total number of chairs around the table
    private int emptySeats;

    public Table(int size) {
        this.idOfTable = UUID.randomUUID().toString();
        this.size = size;
        this.emptySeats = size; // when the restaurant opens, all tables are empty
    }

    public String getIdOfTable() {
        return idOfTable;
    }

    public int getSize() {
        return size;
    }

    public int getEmptySeats() {
        return emptySeats;
    }

    public void setEmptySeats(int newEmptySeats){
        this.emptySeats = newEmptySeats;
    }
}
