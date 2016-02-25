import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

/**
 * Subscriber class communicate to publisher by RMI and also extends Thread
 * class to listen incoming requests.
 * 
 * Our publisher/subscriber pattern and functionality is similar to mentioned
 * below.
 * 
 * Reference: https://en.wikipedia.org/wiki/Publish%E2%80%93subscribe_pattern
 * 
 * @author Ajith, Harshit
 */
public class Subscriber extends Thread {
    public Registry registry;
    public PublisherI stub;

    // Define instance variables.
    public String host;
    public int port;
    public ArrayList<Event> eventList = new ArrayList<Event>();

    /**
     * Constructor to create a subscriber object. publisher host is required to
     * connect to the publisher running on that host.
     * 
     * @param publisherHost
     * @param publisherPort
     * @param subscriberHost
     * @param subscriberPort
     */
    public Subscriber(String publisherHost, int publisherPort,
            String subscriberHost, int subscriberPort) {
        try {
            // Locate the registry on the publisher.
            registry = LocateRegistry.getRegistry(publisherHost, publisherPort);
            stub = (PublisherI) registry.lookup("publisher.PublisherI");

            host = subscriberHost;
            port = subscriberPort;
        } catch (Exception e) {
            System.err.println("Cannot connect to publisher.");
            System.exit(404);
        }

    }

    /*
     * Thread will continue monitoring for incoming events published by the
     * publisher.
     */
    @SuppressWarnings("resource")
    public void run() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            System.err.println("Subscriber socket exception.");
        }

        // Loop until all events are received.
        while (true) {
            try {
                // Receive an event.
                byte buf[] = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String eventDetails = new String(packet.getData());

                Event event = Event.createEvent(eventDetails);
                eventList.add(event);

                // Send an Ack.
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                System.out.println("\nReceived event: " + eventDetails
                        + " from publisher: " + address);
                buf = new byte[1024];
                buf = "Event received".getBytes();
                packet = new DatagramPacket(buf, buf.length, address, port);
                socket.send(packet);
                System.out
                        .println("Acknowledgement Sent for the received event.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Subscriber joins the publisher.
     * 
     * @throws RemoteException
     */
    public void joinPublisher() throws RemoteException {
        if (stub.join(host, port))
            System.out.println("Subscriber joined to publisher successfully.");
        else
            System.out.println("Subscriber has already joined server.");
    }

    /**
     * Subscriber leaves the publisher.
     * 
     * @throws RemoteException
     */
    public void leavePublisher() throws RemoteException {
        if (stub.leave(host, port))
            System.out.println("Subscriber has left the server successfully.");
        else
            System.out
                    .println("Subscriber might not have joined to publisher, so no need to leave.");
    }

    /**
     * Subscriber subscribes to the publisher.
     * 
     * @param topic
     *            event_topic
     * @throws RemoteException
     */
    public void subscribe(String topic) throws RemoteException {
        if (stub.subscribe(host, port, topic))
            System.out
                    .println("Subscriber subscribed to publisher successfully.");
        else
            System.out
                    .println("Subscriber has already subscribed to publisher or not joined the publisher.");
    }

    /**
     * Subscriber unsubscribes from the publisher.
     *
     * @param topic
     *            event_topic
     * @throws RemoteException
     */
    public void unsubscribe(String topic) throws RemoteException {
        if (stub.unsubscribe(host, port, topic))
            System.out
                    .println("Subscriber unsubscribed from the publisher successfully.");
        else
            System.out
                    .println("Subscriber might not have subscribed to event or not joined the publisher.");

    }

    /**
     * Subscriber send an event to the publisher and publisher publishes it to
     * all interested subscribers.
     * 
     * @param event
     *            event to publish
     * @throws RemoteException
     */
    public void publish(String event) throws RemoteException {
        if (stub.publish(event, host, port))
            System.out.println();
        else
            System.out
                    .println("Event may already exists or no subscriber subscribed to this topic.");

    }

}
