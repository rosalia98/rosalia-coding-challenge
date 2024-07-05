import java.util.*;

import static java.util.Map.entry;

public class Main {

    public static void main(String[] args) {
        Table table1 = new Table(6);
        Table table2 = new Table(6);
        Table table3 = new Table(6);
        Stack<Table> tablesOfSix = new Stack<>();
        tablesOfSix.add(table1);
        tablesOfSix.add(table2);
        tablesOfSix.add(table3);

        Table table4 = new Table(4);
        Table table5 = new Table(4);
        Stack<Table> tablesOfFour = new Stack<>();
        tablesOfFour.add(table4);
        tablesOfFour.add(table5);

        Table table6 = new Table(5);
        Stack<Table> tablesOfFive = new Stack<>();
        tablesOfFive.add(table6);

        Table table7 = new Table(2);
        Table table8 = new Table(2);
        Table table9 = new Table(2);
        Stack<Table> tablesOfTwo = new Stack<>();
        tablesOfTwo.add(table7);
        tablesOfTwo.add(table8);
        tablesOfTwo.add(table9);

        Table table10 = new Table(3);
        Stack<Table> tablesOfThree = new Stack<>();
        tablesOfThree.add(table10);

        Map<Integer, Stack<Table>> tablesMap = Map.ofEntries(
                entry(6, tablesOfSix),
                entry(4, tablesOfFour),
                entry(5, tablesOfFive),
                entry(2, tablesOfTwo),
                entry(3, tablesOfThree)
        );

        SeatingManager seatingManager = new SeatingManager(tablesMap);

        // A customer group of 6 arrives
        CustomerGroup group1 = new CustomerGroup(6);
        seatingManager.arrives(group1);

    }
}