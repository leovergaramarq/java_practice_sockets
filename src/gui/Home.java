package gui;

import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import javax.swing.*;
import sockets.*;

public class Home {
    
    public static void main(String[] args) {
        new Home().init();
    }
    
    Home() {}
    
    void init(){
        int width=600, height=500;
        
        Color backgroundLeft = new Color(230, 230, 230),
                backgroundRight = new Color(210, 210, 210),
                backgroundUp = Color.GRAY.brighter();
        
        panelLeft = new JPanel(){
            @Override
            public void paintComponent(java.awt.Graphics g){
                super.paintComponent(g);
                setBackground(backgroundLeft);
                g.setColor(backgroundUp);
                g.fillRect(0, 0, this.getWidth(), height / 5);
            }
        };
        
        labelTitle = new JLabel("Just a Chat");
        labelTitle.setFont(new Font("Nirmala UI Semilight", Font.BOLD, 40));
        labelTitle.setHorizontalAlignment(JLabel.CENTER);
        
        labelIp = new JLabel("Dirección IP");
        labelIp.setFont(new Font("Nirmala UI Semilight", Font.BOLD, 10));
        labelIp.setHorizontalAlignment(JLabel.CENTER);
        
        fieldIp = new JTextField();
        fieldIp.setFont(new Font("Nirmala UI Semilight", Font.BOLD, 10));
        fieldIp.setHorizontalAlignment(JLabel.CENTER);
        
        btnCheck = new JButton("Consultar");
        
        btnCheck.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                
            }
        });
        
        fieldIp.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    btnCheck.doClick();
                }
            }
        });
        
        panelRight = new JPanel(){
            @Override
            public void paintComponent(java.awt.Graphics g){
                super.paintComponent(g);
                setBackground(backgroundRight);
            }
        };
        
        labelIpFound = new JLabel("");
        labelIpFound.setFont(new Font("Nirmala UI Semilight", Font.BOLD, 10));
        labelIpFound.setHorizontalAlignment(JLabel.CENTER);
        
        labelPortFound = new JLabel("");
        labelPortFound.setFont(new Font("Nirmala UI Semilight", Font.BOLD, 10));
        labelPortFound.setHorizontalAlignment(JLabel.CENTER);
        
        areaUsers = new JTextArea();
        scrollUsers = new JScrollPane(areaUsers);
        
        fieldName = new JTextField();
        fieldName.setFont(new Font("Nirmala UI Semilight", Font.BOLD, 10));
        fieldName.setHorizontalAlignment(JLabel.CENTER);
                
        fieldPort = new JTextField();
        fieldPort.setFont(new Font("Nirmala UI Semilight", Font.BOLD, 10));
        fieldPort.setHorizontalAlignment(JLabel.CENTER);
        
        btnEnter = new JButton("Entrar");
        
        btnEnter.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                
            }
        });
        
        KeyAdapter adapterEnter = new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                    btnEnter.doClick();
            }
        };
        
        fieldName.addKeyListener(adapterEnter);
        fieldPort.addKeyListener(adapterEnter);
        
//        panelLeft.setMinimumSize(new Dimension(width / 2, height));
//        panelLeft.setMaximumSize(new Dimension(width / 2, height));
        panelLeft.setLayout(new BoxLayout(panelLeft, BoxLayout.Y_AXIS));
        panelLeft.add(labelTitle);
        panelLeft.add(labelIp);
        panelLeft.add(fieldIp);
        panelLeft.add(btnCheck);
        
//        panelRight.setMinimumSize(new Dimension(width / 2, height));
//        panelRight.setMaximumSize(new Dimension(width / 2, height));
        panelRight.setLayout(new BoxLayout(panelRight, BoxLayout.Y_AXIS));
        panelRight.add(labelIpFound);
        panelRight.add(labelPortFound);
        panelRight.add(areaUsers);
        panelRight.add(btnEnter);
        
        panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        panel.setLayout(new GridLayout(0, 2));
        panel.add(panelLeft);
        panel.add(panelRight);
        
        labelTitle.setMinimumSize(new Dimension(width / 2 , 50));
        labelTitle.setMaximumSize(new Dimension(width / 2, 50));
        labelTitle.setHorizontalAlignment(JLabel.CENTER);
        
        fieldIp.setMinimumSize(new Dimension(width / 2 , 20));
        fieldIp.setMaximumSize(new Dimension(width / 2, 20));
        
        areaUsers.setEnabled(false);
        areaUsers.setDisabledTextColor(Color.BLACK);
        
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.add(panel);
        frame.setVisible(true);
    }
    
    private JFrame frame;
    private JPanel panel, panelLeft, panelRight;
    private JLabel labelTitle, labelIp, labelIpFound, labelPortFound;
    private JTextField fieldIp, fieldName, fieldPort;
    private JButton btnCheck, btnEnter;
    private JTextArea areaUsers;
    private JScrollPane scrollUsers;
}
