import java.util.UUID;

public class CustomerGroup {
    private final int size; //number of people in the group
    private boolean isSeated;
    private final String idOfGroup;

    public CustomerGroup(int size) {
        this.size = size;
        this.isSeated = false; // initially, all groups are not seated
        this.idOfGroup = UUID.randomUUID().toString();
    }

    public int getSize() {
        return size;
    }

    public boolean isSeated() {
        return isSeated;
    }

    public void setSeated(boolean seated) {
        isSeated = seated;
    }

    public String getIdOfGroup() {
        return idOfGroup;
    }
}
