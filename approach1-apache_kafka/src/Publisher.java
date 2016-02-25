import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class Publisher extends JPanel {

    private static JComboBox comboBox;
    private static JTextField textField1;
    private static JTextField textField2;
    private static JTextArea messageField;

    // Create a form with the fields
    public Publisher() {
        super(new BorderLayout());
        // Panel for the labels
        JPanel labelPanel = new JPanel(new GridLayout(3, 1)); 
        add(labelPanel, BorderLayout.WEST);

        // Panel for the fields
        JPanel fieldPanel = new JPanel(new GridLayout(3, 1)); 
        add(fieldPanel, BorderLayout.CENTER);
        //setPreferredSize( new Dimension( 640, 480 ) );
        setBorder(BorderFactory.createTitledBorder("Publisher"));

        // Combobox
        JLabel labelCombo = new JLabel("Topics");

        // Options in the combobox
        String[] options = { "Politics", "Sports", "Technology", "Business", "LifeStyle" };
        comboBox = new JComboBox(options);
        comboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // Do something when you select a value

            }
        });

        // Textfield
        JLabel labelTextField1 = new JLabel("Message");
        textField1 = new JTextField();
        
        JLabel labelTextField2 = new JLabel("Current Feeds");
        textField2 = new JTextField();

        // Add labels
        labelPanel.add(labelCombo);
        labelPanel.add(labelTextField1);
        labelPanel.add(labelTextField2);

        // Add fields
        fieldPanel.add(comboBox);
        fieldPanel.add(textField1);
        fieldPanel.add(textField2);
    }

    public static void main(String[] args) {
        final Publisher form = new Publisher();

        // Button submit
        JButton submit = new JButton("Publish Feed!");
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //createIban((String) comboBox.getSelectedItem(), textField1.getText());
            	sendMessage(textField1.getText());
            }
        });

        // Frame for our test
        JFrame f = new JFrame("Publisher");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(form, BorderLayout.NORTH);
        
      //Panel with message box
        messageField = new JTextArea();
        messageField.setText("\n\n\n");
        JPanel m = new JPanel();
        m.add(messageField);
        f.getContentPane().add(m, BorderLayout.CENTER);

        // Panel with the button
        JPanel p = new JPanel();
        p.add(submit);
        f.getContentPane().add(p, BorderLayout.SOUTH);
        
        

        // Show the frame
        f.pack();
        f.setVisible(true);
    }
    
    final static String TOPIC = "javatest";

    
    /**
     * This function sends the message obtained from the Publisher User interface to the kafka broker cluster
     * 
     * @param msg
     */
    public static void sendMessage(String msg){
        Properties properties = new Properties();
        properties.put("metadata.broker.list","localhost:9092");
        properties.put("serializer.class","kafka.serializer.StringEncoder");
        ProducerConfig producerConfig = new ProducerConfig(properties);
        kafka.javaapi.producer.Producer<String,String> producer = new kafka.javaapi.producer.Producer<String, String>(producerConfig);
        SimpleDateFormat sdf = new SimpleDateFormat();
        KeyedMessage<String, String> message =new KeyedMessage<String, String>(TOPIC,msg);
        producer.send(message);
        producer.close();
    }

}