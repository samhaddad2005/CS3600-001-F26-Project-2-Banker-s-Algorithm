public class BankImpl implements BankInterface {

    private int numberOfCustomers;
    private int numberOfResources;

    // Banker's Algorithm data structures
    private int[] available;
    private int[][] maximum;
    private int[][] allocation;
    private int[][] need;

    /**
     * Creates a new bank with the specified resources.
     */
    public BankImpl(int numberOfCustomers, int[] resources) {

        // TODO:
        // Initialize numberOfCustomers and numberOfResources.
        this.numberOfCustomers = numberOfCustomers;
        this.numberOfResources = resources.length;
        this.available = resources.clone();

        this.maximum = new int[numberOfCustomers][numberOfResources];
        this.allocation = new int[numberOfCustomers][numberOfResources];
        this.need = new int[numberOfCustomers][numberOfResources];

        // TODO:
        // Initialize available, maximum,
        // allocation, and need.
    }

    /**
     * Adds a customer and records the customer's
     * maximum resource demand.
     */
    @Override
    public void addCustomer(int customerNum, int[] maxDemand) {

        // TODO
    }

    /**
     * Displays the current state of the bank:
     *
     * Available
     * Maximum
     * Allocation
     * Need
     */
    @Override
    public void getState() {

        // TODO
    }

    /**
     * Determines whether the system would remain
     * in a safe state if the request were granted.
     */
    private boolean isSafeState(int customerNum, int[] request) {

        // TODO:
        // Implement the Banker's Safety Algorithm.

        return true;
    }

    /**
     * Attempts to allocate resources to a customer.
     *
     * @return true if approved
     *         false if denied
     */
    @Override
    public synchronized boolean requestResources(
            int customerNum, int[] request) {
        int numberOfResources = available.length;

        for (int i = 0; i < numberOfResources; i++) {
            if (request[i] > need[customerNum][i]) {
                return false; 
            }
        }

        for (int i = 0; i < numberOfResources; i++) {
            if (request[i] > available[i]) {
                return false; 
            }
        }

        for (int i = 0; i < numberOfResources; i++) {
            available[i] -= request[i];
            allocation[customerNum][i] += request[i];
            need[customerNum][i] -= request[i];
        }

        boolean safe = isSafeState(customerNum, request);

        if (safe) {
            return true;
        } else {
            for (int i = 0; i < numberOfResources; i++) {
                available[i] += request[i];
                allocation[customerNum][i] -= request[i];
                need[customerNum][i] += request[i];
            }
            return false; 
        }
    }
    @Override
    public synchronized void releaseResources(
            int customerNum, int[] release) {

        // TODO:
        // Validate and release the resources.
    }
}