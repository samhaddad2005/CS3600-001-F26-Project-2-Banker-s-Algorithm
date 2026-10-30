import java.io.*;
import java.util.*;

public class TestBankers {

    public static final int NUMBER_OF_CUSTOMERS = 5;

    public static void main(String[] args) throws IOException {

        /*
         * Usage:
         *
         * java TestBankers <input-file> <R0> <R1> ... <Rm-1>
         *
         * Example:
         *
         * java TestBankers inPutFile.txt 10 5 7 8
         */

        if (args.length < 2) {
            System.err.println(
                "Usage: java TestBankers "
                + "<input-file> <R0> <R1> ... <Rm-1>"
            );
            System.exit(1);
        }

        /*
         * TODO:
         *
         * 1. Read the input filename.
         *
         * 2. Determine the number of resource types.
         *
         * 3. Read the initial resources from
         *    the command-line arguments.
         *
         * 4. Create the BankImpl object.
         *
         * 5. Open the input file.
         *
         * 6. Read the maximum demand for each customer.
         *
         * 7. Call addCustomer() for each customer.
         */

        // 1. Input filename
        String fileName = args[0];

        // 2. Number of resource types
        int resources = args.length - 1;

        // 3. Initial resources from the command line
        int[] availableResources = new int[resources];
        try {
            for (int i = 1; i < args.length; i++) {
                int resource = Integer.parseInt(args[i].trim());
                if (resource < 0) {
                    fail("Resource '" + args[i] + "' cannot be negative.");
                }
                availableResources[i - 1] = resource;
            }
        } catch (NumberFormatException e) {
            fail("Resources must be valid integers.");
        }

        // 4. Create the bank
        BankImpl bank = new BankImpl(NUMBER_OF_CUSTOMERS, availableResources);

        // 5. Open the input file
        int rows = 0;
        try (Scanner fileIn = new Scanner(new File(fileName))) {
            int lineNum = 0;

            // 6. Read the maximum demand for each customer
            while (fileIn.hasNextLine()) {
                String line = fileIn.nextLine().trim();
                lineNum++;

                if (line.isEmpty()) {
                    continue;
                }
                if (rows >= NUMBER_OF_CUSTOMERS) {
                    fail("Input file has more than " + NUMBER_OF_CUSTOMERS
                            + " customer rows.");
                }

                String[] values = line.split(",", -1);
                if (values.length != resources) {
                    fail("Line " + lineNum + ": expected " + resources
                            + " values but found " + values.length + ".");
                }

                int[] maxDemand = new int[resources];
                for (int col = 0; col < resources; col++) {
                    try {
                        int v = Integer.parseInt(values[col].trim());
                        if (v < 0) {
                            fail("Line " + lineNum + ": negative value '" + v + "'.");
                        }
                        maxDemand[col] = v;
                    } catch (NumberFormatException e) {
                        fail("Line " + lineNum + ": '" + values[col].trim()
                                + "' is not a valid integer.");
                    }
                }

                // 7. Add the customer
                bank.addCustomer(rows, maxDemand);
                rows++;
            }
        } catch (FileNotFoundException e) {
            fail("Input file '" + fileName + "' not found.");
        }

        if (rows != NUMBER_OF_CUSTOMERS) {
            fail("Input file has " + rows + " customer rows, expected "
                    + NUMBER_OF_CUSTOMERS + ".");
        }

        /*
         * TODO:
         *
         * Repeatedly read commands from the user.
         *
         * Required commands:
         *
         * RQ <customer> <R0> <R1> ... <Rm-1>
         *
         * RL <customer> <R0> <R1> ... <Rm-1>
         *
         * *
         *
         *
         * RQ:
         * Call requestResources()
         *
         * RL:
         * Call releaseResources()
         *
         * *:
         * Call getState()
         */

        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            if (!in.hasNextLine()) {
                break;
            }

            String line = in.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }

            String[] tokens = line.split("\\s+");
            String cmd = tokens[0];

            if (cmd.equals("*")) {
                if (tokens.length != 1) {
                    System.err.println("Error: '*' takes no arguments.");
                    continue;
                }
                bank.getState();

            } else if (cmd.equals("RQ") || cmd.equals("RL")) {
                int expected = 2 + resources;
                if (tokens.length != expected) {
                    System.err.println("Error: " + cmd + " needs a customer number and "
                            + resources + " values ("
                            + (tokens.length < expected ? "too few" : "too many")
                            + " given).");
                    continue;
                }

                int customer;
                try {
                    customer = Integer.parseInt(tokens[1]);
                } catch (NumberFormatException e) {
                    System.err.println("Error: customer number '" + tokens[1]
                            + "' is not an integer.");
                    continue;
                }
                if (customer < 0 || customer >= NUMBER_OF_CUSTOMERS) {
                    System.err.println("Error: customer must be 0 to "
                            + (NUMBER_OF_CUSTOMERS - 1) + ".");
                    continue;
                }

                int[] amounts = parseAmounts(tokens, 2, resources);
                if (amounts == null) {
                    continue;
                }

                if (cmd.equals("RQ")) {
                    boolean ok = bank.requestResources(customer, amounts);
                    System.out.println(ok ? "Request approved." : "Request denied.");
                } else {
                    bank.releaseResources(customer, amounts);
                }

            } else {
                System.err.println("Error: invalid command '" + cmd
                        + "'. Use RQ, RL, or *.");
            }
        }
    }

    private static int[] parseAmounts(String[] tokens, int start, int count) {
        int[] amounts = new int[count];
        for (int i = 0; i < count; i++) {
            try {
                int v = Integer.parseInt(tokens[start + i]);
                if (v < 0) {
                    System.err.println("Error: negative amount '" + v + "'.");
                    return null;
                }
                amounts[i] = v;
            } catch (NumberFormatException e) {
                System.err.println("Error: '" + tokens[start + i]
                        + "' is not a valid integer.");
                return null;
            }
        }
        return amounts;
    }

    private static void fail(String message) {
        System.err.println("Error: " + message);
        System.exit(1);
    }
}