import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import static org.junit.Assert.*;

public class SeatingManagerTest {
    private SeatingManager seatingManager;
    private Table table3;
    public Table table6;

    @Before
    public void setUp() {

        Table table1 = new Table(6);
        Table table2 = new Table(6);
        table3 = new Table(6);
        Stack<Table> tablesOfSix = new Stack<>();
        tablesOfSix.add(table1);
        tablesOfSix.add(table2);
        tablesOfSix.add(table3);


        Table table4 = new Table(5);
        Stack<Table> tablesOfFive = new Stack<>();
        tablesOfFive.add(table4);


        Table table5 = new Table(2);
        Stack<Table> tablesOfTwo = new Stack<>();
        tablesOfTwo.add(table5);


        table6 = new Table(3);
        Stack<Table> tablesOfThree = new Stack<>();
        tablesOfThree.add(table6);



        Map<Integer, Stack<Table>> tablesMap =new HashMap<>();
        tablesMap.put(6, tablesOfSix);
        tablesMap.put(5, tablesOfFive);
        tablesMap.put(2, tablesOfTwo);
        tablesMap.put(3, tablesOfThree);

        seatingManager = new SeatingManager(tablesMap);

    }

    @Test
    public void testArrives() throws Exception {

        // A customer group of 6 arrives
        CustomerGroup group1 = new CustomerGroup(6);
        seatingManager.arrives(group1);

        assertEquals(2, seatingManager.getTablesMap().get(6).size());
        assertEquals(1, seatingManager.getTableToCustomerMap().get(table3).size());

        // another 2 groups of 6 arrive, all the tables of 6 will be full
        CustomerGroup group2 = new CustomerGroup(6);
        CustomerGroup group3 = new CustomerGroup(6);

        seatingManager.arrives(group2);
        seatingManager.arrives(group3);

        assertEquals(0, seatingManager.getTablesMap().get(6).size()); // no more tables of 6

        // another group of 6 arrives, it will stay in the line
        CustomerGroup group4 = new CustomerGroup(6);
        seatingManager.arrives(group4); // all the tables of 6 are occupied for now, so the group remains in the waiting line
        //assertEquals(1, seatingManager.getListofCustomersWaiting().size());
        assertFalse(group4.isSeated());

        CustomerGroup group5 = new CustomerGroup(5); // one table of 5 and zero of 6, so they have to occupy the table of 5
        seatingManager.arrives(group5);
        assertEquals(0, seatingManager.getTablesMap().get(group5.getSize()).size());
        assertTrue(group5.isSeated());

        CustomerGroup group6 = new CustomerGroup(5); // no table to put them at
        seatingManager.arrives(group6);
        assertEquals(2, seatingManager.getListofCustomersWaiting().size()); // now there s a group of 6 and a group of 5 in the waiting line
        assertFalse(group6.isSeated());

        CustomerGroup group7 = new CustomerGroup(2);
        CustomerGroup group8 = new CustomerGroup(2);

        // one of the above groups will sit at a table of 2, the other at a table of 3 with a remaining empty seat
        seatingManager.arrives(group7);
        seatingManager.arrives(group8);

        //assertEquals(6, seatingManager.getTableToCustomerMap().size()); // at this point we have 6 occupied tables
        assertEquals(1, table6.getEmptySeats());
        assertEquals(3, table6.getSize());
        assertEquals(1, seatingManager.getTableToCustomerMap().get(table6).size());

    }

}
