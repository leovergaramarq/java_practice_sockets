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

public class Chat {
    
    Chat(Client client){
        this.client=client;
    }

    Chat(String ip, int serverPort, int clientPort) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    void init(){
        int width=500, height=600;
        
        frame=new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        
        Color background=new Color(230, 230, 230), upper=Color.GRAY.brighter();
        panel=new JPanel(){
            @Override
            public void paintComponent(java.awt.Graphics g){
                super.paintComponent(g);
                setBackground(background);
                g.setColor(upper);
                g.fillRect(0, 0, width, height*2/10);
            }
        };
        
        labelName=new JLabel(client.getInfo().getName());
        labelName.setFont(new java.awt.Font("Nirmala UI Semilight", java.awt.Font.BOLD, 40));
        labelName.setHorizontalAlignment(JLabel.CENTER);
        
        labelIp=new JLabel("Ip: "+client.getInfo().getIp());
        labelIp.setFont(new java.awt.Font("Nirmala UI Semilight", java.awt.Font.BOLD, 15));
        labelIp.setHorizontalAlignment(JLabel.CENTER);
        
        labelPort=new JLabel("Port: "+String.valueOf(client.getInfo().getPort()));
        labelPort.setFont(new java.awt.Font("Nirmala UI Semilight", java.awt.Font.BOLD, 15));
        labelPort.setHorizontalAlignment(JLabel.CENTER);
        
        field=new JTextField();
        send=new JButton("Enviar");
        area=new JTextArea();
        scrollPane=new JScrollPane(area);
        
        field.addKeyListener(new java.awt.event.KeyAdapter(){
            @Override
            public void keyPressed(java.awt.event.KeyEvent e){
                if(e.getKeyCode()==KeyEvent.VK_ENTER){
                    send.doClick();
                }
            }
        });
        
        send.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String msg=field.getText().trim();
                if(msg.equals("")) return;
                area.append("You: "+msg+"\n\n");
                sendMessage(msg);
                field.setText("");
            }
        });
        
        panel.setLayout(null);
        
        panel.add(labelName);
        panel.add(labelIp);
        panel.add(labelPort);
        panel.add(send);
        panel.add(field);
        //panel.add(area);
        panel.add(scrollPane);
        frame.add(panel);
        
        int     fieldWidth=width*8/10, fieldHeight=26,
                labelNameWidth=200, labelNameHeight=50,
                labelIpWidth=200, labelIpHeight=50,
                labelPortWidth=200, labelPortHeight=50,
                sendWidth=(int)send.getPreferredSize().getWidth(),
                sendHeight=(int)send.getPreferredSize().getHeight(),
                scrollWidth=width*8/10,
                scrollHeight=height*5/10;
        
        labelName.setBounds(width/2-labelNameWidth/2, height*1/20-labelNameHeight/2, labelNameWidth, labelNameHeight);
        labelIp.setBounds(width/2-labelIpWidth/2, height*2/20-labelIpHeight/2, labelIpWidth, labelIpHeight);
        labelPort.setBounds(width/2-labelPortWidth/2, height*3/20-labelPortHeight/2, labelPortWidth, labelPortHeight);
        field.setBounds(width/2-fieldWidth/2, height*5/20-fieldHeight/2, fieldWidth, fieldHeight);
        send.setBounds(width/2-sendWidth/2, height*6/20-sendHeight/2, sendWidth, sendHeight);
        field.setHorizontalAlignment(JTextField.CENTER);
        scrollPane.setBounds(width/2-scrollWidth/2, height*12/20-scrollHeight/2, scrollWidth, scrollHeight);
        area.setEnabled(false);
        area.setDisabledTextColor(Color.BLACK);
        
        frame.addWindowListener(new java.awt.event.WindowAdapter(){
            @Override
            public void windowClosing(java.awt.event.WindowEvent e){
                client.leave();
            }
        });
        
        frame.setVisible(true);
    }
    
    void sendMessage(String msg){
        client.send(msg);
    }
    
    void receiveMessage(String name, String ip, String body){
        area.append(name+" ("+ip+") : "+body+"\n\n");
    }
    
    void userJoined(String name, String ip, String body){
        area.append("****** "+name+" ("+ip+") joined the party!!!"+"\n\n");
    }
    
    void userLeft(String name, String ip, String body){
        area.append("****** "+name+" ("+ip+") left the party!!!"+"\n\n");
    }
    
    void joinAproved(){
    }
    
    void joinRejected(Message msg){
        String s="Name\tIp\tPort\n";
        
        for (ClientInfo c: (java.util.ArrayList<ClientInfo>)msg.getInfo()){
            s+="\n"+c.getName()+"\t"+c.getIp()+"\t"+c.getPort();
        }
        JOptionPane.showMessageDialog(null, "Error. Check the list below and please join again with different value(s):\n\n"+s);
    }
    
    void appendArea(String str) {
        area.append(str);
        javax.swing.JScrollBar scrollVert = scrollPane.getVerticalScrollBar();
        scrollVert.setValue(scrollVert.getMaximum());
    }
    
    Client client;
    JFrame frame;
    JPanel panel;
    JButton send;
    JTextField field;
    JLabel labelName, labelIp, labelPort;
    JTextArea area;
    JScrollPane scrollPane;
    
}
