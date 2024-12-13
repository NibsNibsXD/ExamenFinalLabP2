
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JOptionPane;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Jorge Aguirre
 */

public class mainGUI extends JFrame {

    private JPanel mainPanel;
    private JButton sendButton, leerButton, logoutButton;
    private JTextArea emailText;

    private JavaMail javaMail;

    public mainGUI(JavaMail javaMail) {
        this.javaMail = javaMail;
        this.setSize(1500, 1000);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initializeComponents();
        initializeButtons();
        loadInbox();
    }

    private void initializeComponents() {
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.WHITE);
        this.add(mainPanel);

        emailText = new JTextArea();
        emailText.setBounds(300, 100, 1000, 850);
        emailText.setBackground(Color.LIGHT_GRAY);
        emailText.setEditable(false);
        mainPanel.add(emailText);

        sendButton = new JButton("Enviar");
        sendButton.setBounds(50, 300, 150, 50);
        sendButton.setBackground(Color.GREEN);
        sendButton.setForeground(Color.WHITE);
        mainPanel.add(sendButton);

        leerButton = new JButton("Leer Email");
        leerButton.setBounds(50, 400, 150, 50);
        leerButton.setBackground(Color.BLUE);
        leerButton.setForeground(Color.WHITE);
        mainPanel.add(leerButton);

        logoutButton = new JButton("Logout");
        logoutButton.setBounds(50, 880, 150, 50);
        logoutButton.setBackground(Color.GRAY);
        logoutButton.setForeground(Color.WHITE);
        mainPanel.add(logoutButton);


    }

    private void initializeButtons() {
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newEmailGUI ne = new newEmailGUI(javaMail, mainGUI.this);
                ne.setVisible(true);
                dispose();
            }
        });

        leerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String posStr = JOptionPane.showInputDialog(mainPanel, "Ingrese la posicion del email a leer:", "Leer Email", JOptionPane.PLAIN_MESSAGE);
                if(posStr != null){
                    try {
                        int pos = Integer.parseInt(posStr.trim());
                        javaMail.readGUI(pos, emailText);
                        loadInbox();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(mainPanel, "Por favor, ingrese un numero valido.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LoginGUI log = new LoginGUI();
                log.setVisible(true);
                dispose();
            }
        });
    }

    public void loadInbox(){
        javaMail.inboxGUI(emailText);
    }
}
