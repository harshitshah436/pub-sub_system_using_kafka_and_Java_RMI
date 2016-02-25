import java.rmi.*;

/**
 * PublisherI interface defines the remotely accessible part of Publisher and
 * the publisher itself.
 * 
 * Reference: Designing rmi application
 * (https://docs.oracle.com/javase/tutorial/rmi/designing.html)
 * 
 * @author Ajith, Harshit
 */
public interface PublisherI extends Remote {

    /**
     * Subscriber with given host and port joins the publisher.
     * 
     * @param host
     * @param port
     * @return
     * @throws RemoteException
     */
    boolean join(String host, int port) throws RemoteException;

    /**
     * Subscriber with given host and port leaves the publisher.
     * 
     * @param host
     * @param port
     * @return
     * @throws RemoteException
     */
    boolean leave(String host, int port) throws RemoteException;

    /**
     * Subscriber with given host and port subscribes to specific event to a
     * publisher.
     * 
     * @param host
     * @param port
     * @param event
     * @return
     * @throws RemoteException
     */
    boolean subscribe(String host, int port, String event)
            throws RemoteException;

    /**
     * Subscriber with given host and port unsubscribes to a specific event from
     * the publisher.
     * 
     * @param host
     * @param port
     * @param event
     * @return
     * @throws RemoteException
     */
    boolean unsubscribe(String host, int port, String event)
            throws RemoteException;

    /**
     * Subscriber with given host and port post an event and publisher publish
     * it to all subscribed subscribers.
     * 
     * @param event
     * @param host
     * @param port
     * @return
     * @throws RemoteException
     */
    boolean publish(String event, String host, int port) throws RemoteException;
}
