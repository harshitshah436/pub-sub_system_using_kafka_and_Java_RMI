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

public class Subscriber2 extends JPanel {

    private static JComboBox comboBox;
    private static JTextField textField1;
    public static JTextField textField2;
    public static JTextArea messageField;
    public static KafkaConsumer2 helloKafkaConsumer;
    

    // Create a form with the fields
    public Subscriber2() {
        super(new BorderLayout());
        // Panel for the labels
        JPanel labelPanel = new JPanel(new GridLayout(3, 1)); // 2 rows 1 column
        add(labelPanel, BorderLayout.WEST);

        // Panel for the fields
        JPanel fieldPanel = new JPanel(new GridLayout(3, 1)); // 2 rows 1 column
        add(fieldPanel, BorderLayout.CENTER);
        //setPreferredSize( new Dimension( 640, 480 ) );
        setBorder(BorderFactory.createTitledBorder("Subscriber"));

        // Combobox
        JLabel labelCombo = new JLabel("Topics");

        // Options in the combobox
        String[] options = { "Politics", "Sports", "Technology", "Business", "LifeStyle" };
        comboBox = new JComboBox(options);
        comboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(comboBox.getSelectedIndex() != 0){
                	textField2.setText("");
                }

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
        final Subscriber2 form = new Subscriber2();

        // Button submit
        JButton submit = new JButton("Publish Feed!");
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });

        // Frame for our test
        JFrame f = new JFrame("Subscriber2");
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
        
        //start the Kafka consumer instance
        helloKafkaConsumer = new KafkaConsumer2();
        helloKafkaConsumer.start();
    }
}

