package gui;

import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import sockets.*;

public class UserInterface {

    public UserInterface() {}
    
    public UserInterface(Client client) {
        this.client = client;
    }
    
    public void init() {
        home = new Home();
        home.init();
    }
    
    public void join(String ip, int serverPort, int clientPort) {
        Chat chat = new Chat(ip, serverPort, clientPort);
        chat.init();
    }
    
    Client client;
    Home home;
}
