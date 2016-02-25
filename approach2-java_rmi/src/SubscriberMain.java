import java.rmi.RemoteException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * SubscriberMain class is to run a subscriber class object with different
 * functions provided from this class. This class also gives all option to
 * publisher/subscriber pattern.
 * 
 * @author Ajith, Harshit
 */
public class SubscriberMain {
    public static Subscriber subscriber;

    /**
     * Start the subscriber and connect it to publisher by given publisher host.
     * 
     * @param pHost
     *            publisher host
     * @param pPort
     *            publisher port
     * @param sHost
     *            subscriber host
     * @param sPort
     *            subscriber port
     */
    public static void startSubscriber(String pHost, int pPort, String sHost,
            int sPort) {

        try {
            // Start subscriber.
            subscriber = new Subscriber(pHost, pPort, sHost, sPort);
            subscriber.start();
        } catch (Exception e) {
            System.out
                    .println("Cannot connect to publisher. Please recheck publisher is running.");
            System.exit(404);
        }
        System.out.println("Subscriber successfully created. Running on - "
                + sHost + ":" + sPort);
    }

    /**
     * Print options for subscriber to select.
     */
    @SuppressWarnings("resource")
    public static void subscriberFunctions() throws RemoteException {
        System.out.println("\nPlease select any of the following options:");
        System.out.println("1. Join");
        System.out.println("2. Leave");
        System.out.println("3. Subscribe");
        System.out.println("4. Unsubscribe");
        System.out.println("5. Publish");
        System.out.print("Select option: ");
        Scanner in = new Scanner(System.in);
        int selection = 0;
        try {
            selection = in.nextInt();
        } catch (InputMismatchException e) {
            System.out
                    .println("\nInvalid option selected! Enter a number bewteen 1 to 5");
            return;
        }
        if (selection < 1 || selection > 5) {
            System.out
                    .println("\nInvalid option selected! Enter a number bewteen 1 to 5");
            return;
        }

        switch (selection) {
        case 1:
            subscriber.joinPublisher();
            break;
        case 2:
            subscriber.leavePublisher();
            break;
        case 3:
            for (String topic : Event.topics)
                System.out.print(topic + " ");

            System.out
                    .println("\nSelect event you want to subscribe from above topics: ");
            in = new Scanner(System.in);
            String event = in.nextLine().trim();
            if (Event.topicExists(event))
                subscriber.subscribe(event);
            else
                System.out.println("Please select an event from above topics.");

            break;
        case 4:
            for (String topic : Event.topics)
                System.out.print(topic + " ");

            System.out
                    .println("\nSelect event you want to unsubscribe from above topics: ");
            in = new Scanner(System.in);
            event = in.nextLine().trim();
            if (Event.topicExists(event))
                subscriber.unsubscribe(event);
            else
                System.out.println("Please select an event from above topics.");

            break;
        case 5:
            System.out
                    .println("Please enter event details to publish in the format: Topic; Contents ");
            Scanner in1 = new Scanner(System.in);
            String eventDetails = in1.nextLine().trim();
            System.out.println();
            if (Event.isInProperFormat(eventDetails))
                subscriber.publish(eventDetails);
            else
                System.out
                        .println("Please enter the event in the above mentioned correct format.");
            break;
        default:
            break;
        }

    }

    /**
     * Print usage message exit with error code.
     * 
     * @param i
     *            error_code
     */
    public static void usage(int i) {
        switch (i) {
        case 401:
            System.err
                    .println("Usage: java SubscriberMain <publisher_host> <publisher_port> <subscriber_host> <subscriber_port>");
            break;
        default:
            break;
        }
        System.exit(i);
    }

    /*
     * main method.
     */
    public static void main(String[] args) throws RemoteException {

        // Check if 3 arguments are provided or display usage message.
        if (args.length < 4) {
            usage(401);
        }

        System.out.println("-----------Subscriber Window-----------");

        // Initialize subscriber variables.
        startSubscriber(args[0], Integer.parseInt(args[1]), args[2],
                Integer.parseInt(args[3]));

        // Run subscriber for different functionalities.
        while (true) {
            subscriberFunctions();
        }

    }
}
