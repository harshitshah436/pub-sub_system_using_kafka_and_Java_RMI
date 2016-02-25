Team Project – Distributed Publisher Subscriber System
Team Members: Ajith Paul (ap8207) & Harshit Shah (hrs8207)

Goal: To develop a distributed publisher-subscriber system using Kafka and Java RMI.

-----Using Apache Kafka-----
In the first approach to implement a publisher subscriber system, we have used Apache

Kafka as the message broker.Apache Kafka is an open source message broker project developed by the Apache Software Foundation written in Scala. Kafka aims to provide a unified, high throughput, low latency platform for handling realtime data feeds.

Apache Kafka has to be downloaded and setup if you wanted to run this implementation. 

Steps :
● Download Kafka kafka.apache.org
Download the latest release and untar it
tar -xzf kafka_2.11-0.9.0.0.tgz
cd kafka_2.11-0.9.0.0
● Start Zookeeper server
Since Kafka uses Zookeeper, it has to be started before proceeding.
bin/zookeeper-server-start.sh config/zookeeper.properties
● Start Kafka Server
bin/kafka-server-start.sh config/server.properties
Note : Before compiling the java file, the libraries have to added to the classpath.
● Run the Publisher
$ java Publisher
The topics can be chosen from the Topics dropdown menu at the Publisher UI. The message text box is the box where messages under a certain topic to be published are input by the user.
Once the subscribers are started (instructions listed in the next step), messages can be published by clicking the ‘Publish Feed’ button on the Publisher UI
● Run the Subscribers
$ java Subscriber
The subscribers can view topics by choosing them from the dropdown menu in the UI.
Eg: If the publisher has published a message under the topic Politics, the subscribers who are subscribing to those topics will be able to view them in their message window in the Subscriber UI.


-----Using Java RMI-----

Usage: 
java Publisher <host> <port>
java SubscriberMain <publisher_host> <publisher_port> <subscriber_host> <subscriber_port>

-> To compile and run source files
1)	Unzip hrs8207.zip then go to directory \hrs8207\approach2-java_rmi\src\.
2)	Run command: 
javac Publisher.java
javac SubscriberMain.java
Note: all source files should be present in the directory. Also place the input file in the same directory.
3)	Run command: 
java Publisher <host> <port>
java SubscriberMain <publisher_host> <publisher_port> <subscriber_host> <subscriber_port>

Event Format:
---------------------|----------------------------------|
    Event Type     |         Event Contents         |
---------------------------------------------------------
This is our event format. First any connected subscriber will publish this event to connected subscriber and connected subscriber will publish it to all interested subscribers.
Solution Design:
 

Error Codes:
401: Usage: java Publisher <host> <port>. 
Usage: java SubscriberMain <publisher_host> <publisher_port> <subscriber_host> <subscriber_port>
404: Cannot connect to publisher.

Description about all source files:
•	PublisherI.java: PublisherI interface defines the remotely accessible part of Publisher and the publisher itself.
•	Publisher.java: Publisher class implements PublisherI interface and will publish events to all subscribed subscribers.
•	Subscriber.java: Subscriber class communicate to publisher by RMI and also extends Thread class to listen incoming requests.
•	SubscriberMain.java: SubscriberMain class is to run a subscriber class object with different functions provided from this class. This class also gives all option to publisher/subscriber pattern.
•	Broker.java: Broker class stores subscriber information with all subscribed event topics. It is a middleware in Publisher/Subscriber pattern.
•	Event.java: Event class defines the structure of events. Also defines the topics of the events. 