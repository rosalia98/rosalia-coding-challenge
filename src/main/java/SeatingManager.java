import java.util.*;

public class SeatingManager {
    // map from available seats of the table to the list of tables
    // example:
    // 6 -> [(id1, 6, 6), (id2, 6, 6)] // 2 tables of 6 each, with all seats empty
    // 4 -> [(id3, 6, 4), (id4, 4,4)] // 1 table of 6 with 4 seats empty and 1 table of 4 with 4 seats empty
    // this map gets modified as groups of customers come and go
    private Map<Integer, Stack<Table>> tablesMap;
    private List<CustomerGroup> listofCustomersWaiting = new ArrayList<>();
    // each table can accommodate one or more groups, this relationship is maintained here
    // if the table is completely empty it will not appear in this map
    private Map<Table, List<CustomerGroup>> tableToCustomerMap = new HashMap<>();

    // constructor, getter
    public SeatingManager(Map<Integer, Stack<Table>> tablesMap) {
        this.tablesMap = tablesMap;
    }

    public List<CustomerGroup> getListofCustomersWaiting() {
        return listofCustomersWaiting;
    }

    public Map<Integer, Stack<Table>> getTablesMap() { return tablesMap;}

    public Map<Table, List<CustomerGroup>> getTableToCustomerMap() { return tableToCustomerMap; }

    /* Group arrives and wants to be seated. */
    public void arrives(CustomerGroup group){
        listofCustomersWaiting.add(group); // the group arrives and add itself to the waiting line
        int groupSize = group.getSize();
        // case 1 - there s a table that can exactly accommodate that number of people
        if(tablesMap.containsKey(groupSize) && !tablesMap.get(groupSize).isEmpty()) {
            listofCustomersWaiting.remove(group); // found a table, remove group from waiting line
            Table tableOccupied = tablesMap.get(groupSize).pop(); // get the first table that has the number of empty places needed and remove it from that mapping
            commonOperations(group, groupSize, groupSize, tableOccupied);
        } else { // maybe there's a bigger table than the number of people

            int nrOfEmptySeats = 0;
            Table tableOccupied = new Table(-1);
            for (Integer nrOfSeatsAvailable : tablesMap.keySet()) {
                if(nrOfSeatsAvailable > groupSize && !tablesMap.get(nrOfSeatsAvailable).isEmpty()) {
                    listofCustomersWaiting.remove(group); // found a table, remove group from waiting line
                    tableOccupied = tablesMap.get(nrOfSeatsAvailable).pop(); // get the first table that has the number of empty places needed and remove it from that mapping
                    commonOperations(group, nrOfSeatsAvailable, groupSize, tableOccupied);
                    nrOfEmptySeats = tableOccupied.getEmptySeats();
                    break;
                }
            }
            // if the table still has empty seats after this group was seated, then I have to update the tablesMap
            //int nrOfEmptySeats = tableOccupied.getEmptySeats();
            if(nrOfEmptySeats != 0) {
                if(tablesMap.get(nrOfEmptySeats) == null) {
                    tablesMap.put(nrOfEmptySeats, new Stack<>());
                } else {
                    tablesMap.get(nrOfEmptySeats).add(tableOccupied);
                }
            }

        }
    }

    public void commonOperations(CustomerGroup group, int nrofSeatsAvailable, int groupSize, Table tableOccupied) {

        tableOccupied.setEmptySeats(tableOccupied.getEmptySeats() - groupSize); // will be zero if the group fits the table exactly, will be more than zero if there's still available seats
        group.setSeated(true);

        // if the table was completely empty before the group arrived, I need to add it as a new key in the map
        if(!tableToCustomerMap.containsKey(tableOccupied)) {
            tableToCustomerMap.put(tableOccupied, new ArrayList<>(Arrays.asList(group)));
        } else { // maybe the table had 2 people seated and I just added another 2, so it is in the hashmap, but I need to add the new group
            tableToCustomerMap.get(tableOccupied).add(group);
        }


    }

    /* Whether seated or not, the group leaves the restaurant. */
    public void leaves(CustomerGroup group){
        // group not seated => it just gets eliminated from the waiting line
        if(!group.isSeated()) {
            listofCustomersWaiting.remove(group);
        } else {// group seated => gets eliminated from the table-seated groups relationship
            // tablesMap gets updated because some seats become available
            for (Map.Entry<Table, List<CustomerGroup>> entry: tableToCustomerMap.entrySet()) {
                List<CustomerGroup> currentGroup = entry.getValue();
                // removeIf method returns true if the element was found and eliminated
                if(currentGroup.removeIf(el -> el.getIdOfGroup() == group.getIdOfGroup())){ // here i could have also used == instead of uniqueid equality
                    // if the size of the group is equal to the size of the table, then it was the only seated group at that table
                    Table tableWhereTheyWereSeated = entry.getKey();
                    if(tableWhereTheyWereSeated.getSize() == group.getSize()) {
                        tableToCustomerMap.remove(tableWhereTheyWereSeated); // eliminate the table from here as it became empty
                    }
                    tableWhereTheyWereSeated.setEmptySeats(tableWhereTheyWereSeated.getEmptySeats() + group.getSize());
                    // update tablesMap
                    if(tablesMap.containsKey(tableWhereTheyWereSeated.getEmptySeats())) {
                        tablesMap.get(tableWhereTheyWereSeated.getEmptySeats()).add(tableWhereTheyWereSeated);
                    } else {
                        Stack<Table> tables = new Stack<>();
                        tables.push(tableWhereTheyWereSeated);
                        tablesMap.put(tableWhereTheyWereSeated.getEmptySeats(), tables);
                    }
                    // there's just 1 group to look for, so break the for loop if it was removed
                    break;
                }
            }
        }
    }

    /* Return the table at which the group is seated, or null if
    they are not seated (whether they're waiting or already left). */
    public Table locate(CustomerGroup group){
        if(!group.isSeated()) {
            return null;
        }
        for (Map.Entry<Table, List<CustomerGroup>> entry: tableToCustomerMap.entrySet()) {
            List<CustomerGroup> currentGroup = entry.getValue();
            if(currentGroup.contains(group)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
