import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Publisher class implements PublisherI interface and will publish events to
 * all subscribed subscribers.
 * 
 * Our publisher/subscriber pattern and functionality is similar to mentioned
 * below.
 * 
 * Reference: https://en.wikipedia.org/wiki/Publish%E2%80%93subscribe_pattern
 * 
 * @author Ajith, Harshit
 */
public class Publisher implements PublisherI {

    // Define instance variables.
    public String host;
    public int port;
    ArrayList<Broker> subscribers = new ArrayList<Broker>();
    ArrayList<Event> events = new ArrayList<Event>();

    /**
     * Constructor to assign host and port.
     * 
     * @param host
     * @param port
     * @throws RemoteException
     */
    public Publisher(String host, int port) throws RemoteException {
        this.host = host;
        this.port = port;
    }

    /*
     * (non-Javadoc)
     * 
     * @see PublisherI#join(java.lang.String, int)
     */
    @Override
    public boolean join(String sHost, int sPort) throws RemoteException {

        // Check if subscriber already joined the server.
        for (Broker sub : subscribers) {
            if (sub.host.equals(sHost) && sub.port == sPort) {
                System.out.println("Subscriber: " + sHost + ":" + sPort
                        + " has already joined server.");
                return false;
            }
        }

        // Add new subscriber to publisher and store info in the
        // middleware/broker.
        Broker sub = new Broker(sHost, sPort);
        subscribers.add(sub);
        System.out.println(sHost + ":" + sPort + " joined server");
        printSubscribers();
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see PublisherI#leave(java.lang.String, int)
     */
    @Override
    public boolean leave(String sHost, int sPort) throws RemoteException {
        // Leave subscriber if it has joined any publisher.
        for (Broker sub : subscribers) {
            if (sub.host.equals(sHost) && sub.port == sPort) {
                subscribers.remove(sub);
                System.out.println("Subscriber " + sHost + ":" + sPort
                        + " has left the publisher");
                printSubscribers();
                return true;
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see PublisherI#subscribe(java.lang.String, int, java.lang.String)
     */
    @Override
    public boolean subscribe(String sHost, int sPort, String topic)
            throws RemoteException {
        // Add subscriber to publisher for specific topic if it has already
        // joined to publisher.
        for (Broker sub : subscribers) {
            if (sub.host.equals(sHost) && sub.port == sPort) {
                int index = subscribers.indexOf(sub);
                if (sub.addTopic(topic)) {
                    // replace the old object with updated one.
                    subscribers.remove(index);
                    subscribers.add(sub);
                    printSubscribers();
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see PublisherI#unsubscribe(java.lang.String, int, java.lang.String)
     */
    @Override
    public boolean unsubscribe(String sHost, int sPort, String topic)
            throws RemoteException {
        // Unsubscribe to specific topic.
        for (Broker sub : subscribers) {
            if (sub.host.equals(sHost) && sub.port == sPort) {
                int index = subscribers.indexOf(sub);
                if (sub.removeTopic(topic)) {
                    // replace the old object with updated one.
                    subscribers.remove(index);
                    subscribers.add(sub);
                    printSubscribers();
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * Print all joined subscribers.
     */
    public void printSubscribers() {
        for (Broker sub : subscribers) {
            System.out.println("Subscriber: " + sub.toString());
            System.out.println("Subscribed to Topics: "
                    + (sub.allSubscribedEvents().isEmpty() ? "None" : sub
                            .allSubscribedEvents()));
        }
        System.out.println();
    }

    /*
     * (non-Javadoc)
     * 
     * @see PublisherI#publish(java.lang.String, java.lang.String, int)
     */
    @Override
    public boolean publish(String eventDetails, String sHost, int sPort)
            throws RemoteException {
        System.out.println("Publisher received event details:" + eventDetails);
        Event event = Event.createEvent(eventDetails, sHost, sPort);

        // Check if the same event is already exists. If not then add and
        // publish.
        if (events.contains(event)) {
            System.out
                    .println("Same event details is already exists, so not allowed");
            return false;
        }
        events.add(event);

        // Publish the events to the interested subscribers.
        if (subscribers != null) {
            int count = 0;
            for (Broker sub : subscribers) {
                if (sub.subscribedTopics != null
                        && sub.subscribedTopics.contains(event.topic)) {
                    sendEventDetails(event.toString(), sub);
                    count++;
                }
            }
            if (count > 0)
                System.out
                        .println("Event details is publised to all interested subscribers.");
            else
                return false;
        }

        // Print all active events
        for (Event a : events) {
            System.out.println("Event: " + a.toString());
        }
        System.out.println();

        return true;
    }

    /**
     * This method send an event to given subscriber through the broker.
     * 
     * @param event
     * @param sub
     *            subscriber details in broker.
     */
    public void sendEventDetails(String event, Broker sub) {
        try {
            byte buf[] = new byte[1024];
            buf = event.getBytes();

            // Get InetAddress.
            String host = sub.host;
            InetAddress address = InetAddress.getByName(host);

            // Publisher publishes an event to the subscriber.
            DatagramSocket socket = new DatagramSocket();
            DatagramPacket packet = new DatagramPacket(buf, buf.length,
                    address, sub.port);
            socket.send(packet);
            System.out.println("EventDetails:" + event + " Sent to:" + host
                    + ":" + sub.port);

            // Receive Ack message.
            buf = new byte[1024];
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            String subAck = new String(packet.getData());
            System.out.println("Ack received for Subscriber: " + host + ":"
                    + sub.port + " confirm " + subAck);

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Print usage message exit with error code.
     * 
     * @param i
     *            error code
     */
    public static void usage(int i) {
        switch (i) {
        case 401:
            System.err.println("Usage: java Publisher <host> <port>");
            break;
        default:
            break;
        }
        System.exit(i);
    }

    /*
     * main method
     */
    public static void main(String args[]) {

        try {
            if (args.length < 2) {
                usage(401);
            }

            System.out.println("-----------Publisher Window-----------");

            Publisher obj = new Publisher(args[0], Integer.parseInt(args[1]));

            PublisherI stub = (PublisherI) UnicastRemoteObject.exportObject(
                    obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.createRegistry(obj.port);
            registry.bind("publisher.PublisherI", stub);

            System.out.println("Publisher server running on - " + obj.host
                    + ":" + obj.port);
        } catch (NumberFormatException e) {
            System.err
                    .println("Please try again and enter numeric port number.");
        } catch (Exception e) {
            System.err.println("Exception occured in publisher.");
            e.printStackTrace();
        }
    }
}
