/**
 * Event class defines the structure of events. Also defines the topics of the
 * events.
 * 
 * @author Ajith, Harshit
 */
public class Event {
    // Define instance variables
    public String topic, contents;

    // All topics should be present here.
    public static final String[] topics = new String[] { "Business",
            "Entertainment", "Health", "Lifestyle", "Politics", "Science",
            "Sports", "Technology" };

    // To keep a track of which subscriber published new event.
    public Broker sub;

    /**
     * Constructor to create an event object with subscriber host and port
     * details.
     * 
     * @param event
     * @param contents
     * @param host
     * @param port
     */
    public Event(String event, String contents, String host, int port) {
        topic = event;
        this.contents = contents;
        sub = new Broker(host, port);
    }

    /**
     * Constructor to create an event object.
     * 
     * @param event
     * @param contents
     */
    public Event(String event, String contents) {
        topic = event;
        this.contents = contents;
    }

    /**
     * Check if the topic is exist in the topics available.
     * 
     * @param topic
     * @return true if topic is already subscribed for the subscriber.
     */
    public static boolean topicExists(String topic) {
        for (String s : topics) {
            if (s.equals(topic))
                return true;
        }
        return false;
    }

    /**
     * Check if published event details is in proper format: (topic; contents)
     * 
     * @param eventDetails
     * @return true if the event details are entered in an expected format.
     */
    public static boolean isInProperFormat(String eventDetails) {
        if (eventDetails.contains(";")) {
            String[] temp = eventDetails.split(";");
            String topic = temp[0];
            if (topicExists(topic) && temp.length > 1)
                if (!temp[1].isEmpty() || temp[1] != null)
                    return true;
        }
        return false;
    }

    /**
     * Create Event object from event details with subscriber host and port
     * information.
     * 
     * @param eventDetails
     * @param host
     * @param port
     * @return event object created from event details with subscriber details.
     */
    public static Event createEvent(String eventDetails, String host, int port) {
        String[] temp = eventDetails.split(";");
        return new Event(temp[0].trim(), temp[1].trim(), host, port);
    }

    /**
     * Create Event object from event details.
     * 
     * @param eventDetails
     * @return event object created from event details
     */
    public static Event createEvent(String eventDetails) {
        String[] temp = eventDetails.split(";");
        return new Event(temp[0].trim(), temp[1].trim());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "<" + topic + ";" + contents + ">";
    }

}