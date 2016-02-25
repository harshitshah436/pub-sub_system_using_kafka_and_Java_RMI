import java.util.ArrayList;

/**
 * Broker class stores subscriber information with all subscribed event topics.
 * It is a middleware in Publisher/Subscriber pattern.
 * 
 * @author Ajith, Harshit
 */
public class Broker {

    // Define instance varibles to store subscriber host and port.
    String host;
    int port;

    public ArrayList<String> subscribedTopics = new ArrayList<String>();

    /**
     * Constructor creates a new subscriber with given host and port.
     * 
     * @param host
     * @param port
     */
    public Broker(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return host + ":" + port;
    }

    /**
     * Returns all subscribed events for the current subscriber.
     */
    public String allSubscribedEvents() {
        String topics = "";
        for (String s : subscribedTopics) {
            topics += s + ":";
        }
        if (topics.length() > 0)
            topics = topics.substring(0, topics.lastIndexOf(":"));
        return topics;
    }

    /**
     * If subscriber has not already subscribed to given topic the add topic to
     * the subscribed topics list and return true; otherwise return false.
     * 
     * @param topic
     * @return true of false based on topic added successfully or not.
     */
    public boolean addTopic(String topic) {
        if (!subscribedTopics.contains(topic))
            return subscribedTopics.add(topic);
        return false;
    }

    /**
     * Remove topic from the subscribed topics list and return true.
     * 
     * @param topic
     * @return true of false based on topic removed successfully or not.
     */
    public boolean removeTopic(String topic) {
        if (subscribedTopics.contains(topic))
            return subscribedTopics.remove(topic);
        return false;
    }
}
