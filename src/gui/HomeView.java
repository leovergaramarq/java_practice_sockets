package gui;

import info.Info;
import info.Pack;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.util.ArrayList;
import javax.swing.*;
import justachat.Client;
import justachat.Utils;

public class HomeView {
    
    public static void main(String[] args) {
        new HomeView().init();
    }
    
    public HomeView() {}
    
    public HomeView(Client client) {
        this.client = client;
    }
    
    public void init() {
        int width=600, height=600;
        
        Color backgroundLeft = new Color(230, 230, 230),
                backgroundRight = new Color(210, 210, 210),
                backgroundUp = Color.GRAY.brighter();
        
        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnCheck.doClick();
                }else if(e.getKeyCode() == KeyEvent.VK_SPACE) {
                    System.out.println("Space");
                    client.setListening(false);
                }
            }
        };
        
        labelTitle = new JLabel("Just a Chat");
        labelTitle.setFont(new Font("Nirmala UI Semilight", Font.BOLD, 20));
        labelTitle.setBounds(width / 4 - labelTitle.getPreferredSize().width / 2, height / 10 - labelTitle.getPreferredSize().height / 2, labelTitle.getPreferredSize().width, labelTitle.getPreferredSize().height);
        
        labelIp = new JLabel("Dirección IP");
        labelIp.setFont(new Font("Nirmala UI Semilight", Font.BOLD, 12));
        labelIp.setBounds(width / 4 - labelIp.getPreferredSize().width / 2, height * 6 / 20 - labelIp.getPreferredSize().height / 2, labelIp.getPreferredSize().width, labelIp.getPreferredSize().height);
        
        fieldIp = new JTextField();
        fieldIp.setFont(new Font("Nirmala UI Semilight", Font.PLAIN, 12));
        fieldIp.setPreferredSize(new Dimension(width / 2 * 6 / 10, fieldIp.getPreferredSize().height));
        fieldIp.setBounds(width / 4 - fieldIp.getPreferredSize().width / 2, height * 7 / 20 - fieldIp.getPreferredSize().height / 2, fieldIp.getPreferredSize().width, fieldIp.getPreferredSize().height);
        fieldIp.setHorizontalAlignment(JTextField.CENTER);
        fieldIp.addKeyListener(keyAdapter);
        
        labelServerPort = new JLabel("Puerto");
        labelServerPort.setFont(new Font("Nirmala UI Semilight", Font.BOLD, 12));
        labelServerPort.setBounds(width / 4 - labelServerPort.getPreferredSize().width / 2, height * 9 / 20 - labelServerPort.getPreferredSize().height / 2, labelServerPort.getPreferredSize().width, labelServerPort.getPreferredSize().height);
        
        fieldServerPort = new JTextField();
        fieldServerPort.setFont(new Font("Nirmala UI Semilight", Font.PLAIN, 12));
        fieldServerPort.setPreferredSize(new Dimension(width / 2 * 6 / 10, fieldServerPort.getPreferredSize().height));
        fieldServerPort.setBounds(width / 4 - fieldServerPort.getPreferredSize().width / 2, height * 10 / 20 - fieldServerPort.getPreferredSize().height / 2, fieldServerPort.getPreferredSize().width, fieldServerPort.getPreferredSize().height);
        fieldServerPort.setHorizontalAlignment(JTextField.CENTER);
        fieldServerPort.addKeyListener(keyAdapter);
        
        btnCheck = new JButton("Consultar Servidor");
        btnCheck.setFont(new Font("Nirmala UI Semilight", Font.BOLD, 12));
        btnCheck.setBounds(width / 4 - btnCheck.getPreferredSize().width / 2, height * 7 / 10 - btnCheck.getPreferredSize().height / 2, btnCheck.getPreferredSize().width, btnCheck.getPreferredSize().height);
        btnCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkServer();
            }
        });
        
        keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                    btnEnter.doClick();
            }
        };
        
        labelIpFound = new JLabel();
        labelIpFound.setFont(new Font("Nirmala UI Semilight", Font.BOLD, 12));
        labelIpFound.setBounds(width / 2 / 10, height * 1 / 20 - labelIpFound.getPreferredSize().height / 2, labelIpFound.getPreferredSize().width, labelIpFound.getPreferredSize().height);
        
        labelPortFound = new JLabel();
        labelPortFound.setFont(new Font("Nirmala UI Semilight", Font.BOLD, 12));
        labelPortFound.setBounds(width / 2 / 10, height * 2 / 20 - labelPortFound.getPreferredSize().height / 2, labelPortFound.getPreferredSize().width, labelPortFound.getPreferredSize().height);
        
        areaUsers = new JTextArea();
        areaUsers.setFont(new Font("Nirmala UI Semilight", Font.PLAIN, 10));
        areaUsers.setEnabled(false);
        areaUsers.setDisabledTextColor(Color.BLACK);
        
        scrollUsers = new JScrollPane(areaUsers);
        scrollUsers.setPreferredSize(new Dimension(width / 2 * 8 / 10, height * 8 / 20));
        scrollUsers.setBounds(width / 4 - scrollUsers.getPreferredSize().width / 2, height * 7 / 20 - scrollUsers.getPreferredSize().height / 2, scrollUsers.getPreferredSize().width, scrollUsers.getPreferredSize().height);
        
        labelPort = new JLabel("Puerto");
        labelPort.setFont(new Font("Nirmala UI Semilight", Font.BOLD, 12));
        labelPort.setBounds(width / 4 - labelPort.getPreferredSize().width / 2, height * 12 / 20 - labelPort.getPreferredSize().height / 2, labelPort.getPreferredSize().width, labelPort.getPreferredSize().height);
        
        fieldPort = new JTextField();
        fieldPort.setFont(new Font("Nirmala UI Semilight", Font.PLAIN, 12));
        fieldPort.setPreferredSize(new Dimension(width / 2 * 6 / 10, fieldPort.getPreferredSize().height));
        fieldPort.setBounds(width / 4 - fieldPort.getPreferredSize().width / 2, height * 13 / 20 - fieldPort.getPreferredSize().height / 2, fieldPort.getPreferredSize().width, fieldPort.getPreferredSize().height);
        fieldPort.setHorizontalAlignment(JTextField.CENTER);
        fieldPort.addKeyListener(keyAdapter);
        
        labelName = new JLabel("Nombre");
        labelName.setFont(new Font("Nirmala UI Semilight", Font.BOLD, 12));
        labelName.setBounds(width / 4 - labelName.getPreferredSize().width / 2, height * 14 / 20 - labelName.getPreferredSize().height / 2, labelName.getPreferredSize().width, labelName.getPreferredSize().height);
        
        fieldName = new JTextField();
        fieldName.setFont(new Font("Nirmala UI Semilight", Font.PLAIN, 12));
        fieldName.setPreferredSize(new Dimension(width / 2 * 6 / 10, fieldName.getPreferredSize().height));
        fieldName.setBounds(width / 4 - fieldName.getPreferredSize().width / 2, height * 15 / 20 - fieldName.getPreferredSize().height / 2, fieldName.getPreferredSize().width, fieldName.getPreferredSize().height);
        fieldName.setHorizontalAlignment(JTextField.CENTER);
        fieldName.addKeyListener(keyAdapter);
        
        btnEnter = new JButton("Entrar");
        btnEnter.setBounds(width / 4 - btnEnter.getPreferredSize().width / 2, height * 17 / 20 - btnEnter.getPreferredSize().height / 2, btnEnter.getPreferredSize().width, btnEnter.getPreferredSize().height);
        btnEnter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                joinServer();
            }
        });
        
        panelLeft = new JPanel() {
            @Override
            public void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                setBackground(backgroundLeft);
                g.setColor(backgroundUp);
                g.fillRect(0, 0, this.getWidth(), height / 5);
            }
        };
        panelLeft.setLayout(null);
        panelLeft.add(labelTitle);
        panelLeft.add(labelIp);
        panelLeft.add(fieldIp);
        panelLeft.add(labelServerPort);
        panelLeft.add(fieldServerPort);
        panelLeft.add(btnCheck);
        
        panelRight = new JPanel() {
            @Override
            public void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                setBackground(backgroundRight);
            }
        };
        panelRight.setLayout(null);
        panelRight.add(labelIpFound);
        panelRight.add(labelPortFound);
        panelRight.add(scrollUsers);
        panelRight.add(labelPort);
        panelRight.add(fieldPort);
        panelRight.add(labelName);
        panelRight.add(fieldName);
        panelRight.add(btnEnter);
        setRightComponentsEnabled(false);
        
        panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2));
        panel.add(panelLeft);
        panel.add(panelRight);
        
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.add(panel);
        frame.setVisible(true);
    }
    
    private void checkServer() {
        try {
            serverPort = Integer.parseInt(fieldServerPort.getText().trim());
        }catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Port must be numeric");
            return;
        }
        ip = fieldIp.getText().trim();
        while((port = Utils.randomPort()) == serverPort);
        
        if(Utils.validIp(ip) && Utils.validPort(serverPort)) {
            
            client.setPort(port);
            client.listen();
            
            if(!client.checkServer(ip, serverPort)) {
                checkServerRejected();
                JOptionPane.showMessageDialog(null, "Server not found");
            }
        }
        else {
            checkServerRejected();
            JOptionPane.showMessageDialog(null, "Invalid input");
        }
    }
    
    private void joinServer() {
        if(!serverChecked) return;
        
        try {
            port = Integer.parseInt(fieldPort.getText().trim());
        }catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Port must be numeric");
            return;
        }
        name = fieldName.getText().trim();
        
        if(Utils.validPort(port) && port != serverPort && !name.equals("") 
                && !name.equals(serverName)) {
            
            client.join(ip, serverPort, serverName, port, name);
        }else {
            JOptionPane.showMessageDialog(null, "Invalid input");
        }
    }
    
    private void checkServerRejected() {
        cleanRightComponents();
        setRightComponentsEnabled(false);
        ip = "";
        serverPort = -1;
        serverName = "";
        serverChecked = false;
    }
    
    private void setRightComponentsEnabled(boolean en) {
        fieldName.setEnabled(en);
        fieldPort.setEnabled(en);
        btnEnter.setEnabled(en);
    }
    
    private void cleanRightComponents() {
        labelIpFound.setText("");
        labelPortFound.setText("");
        fieldName.setText("");
        fieldPort.setText("");
        areaUsers.setText("");
    }
    
    public void checkServerAproved(Pack pkg) {
        System.out.println(pkg);
        ArrayList<Info> infoUsers = (ArrayList<Info>)pkg.getInfo();
        
        Info infoServer = infoUsers.get(0);
        String body = "Name | Port\n"
                + "(server) " + infoServer.getName() + " | " + infoServer.getPort() + "\n";
        
        for(int i = 1; i < infoUsers.size(); i++) {
            Info info = infoUsers.get(i);
            body += info.getName() + " | " + info.getPort() + "\n";
        }
        
        labelIpFound.setText(infoServer.getIp());
        labelIpFound.setSize(labelIpFound.getPreferredSize().width, labelIpFound.getPreferredSize().height);
        
        labelPortFound.setText(String.valueOf(infoServer.getPort()));
        labelPortFound.setSize(labelPortFound.getPreferredSize().width, labelPortFound.getPreferredSize().height);
        
        areaUsers.setText(body);
        setRightComponentsEnabled(true);
        
        ip = infoServer.getIp();
        serverPort = infoServer.getPort();
        serverName = infoServer.getName();
    }
    
    public void joinServerAproved() {
        
    }
    
    private String ip, serverName, name;
    private int port, serverPort;
    private boolean serverChecked;
    
    private Client client;
    private JFrame frame;
    private JPanel panel, panelLeft, panelRight;
    private JLabel labelTitle, labelIp, labelServerPort, labelIpFound, labelPortFound, labelPort, labelName;
    private JTextField fieldIp, fieldServerPort, fieldName, fieldPort;
    private JButton btnCheck, btnEnter;
    private JTextArea areaUsers;
    private JScrollPane scrollUsers;
}
