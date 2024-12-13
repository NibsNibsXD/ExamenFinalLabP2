
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Jorge Aguirre
 */

public class LoginGUI extends JFrame {
    
    private JPanel panel;
    private JLabel Titulo, userText, userPass;
    private JButton botonSubmit, botonAccount;
    private JTextField Username, Password;
    
    private JavaMail javaMail;
    
    public LoginGUI(){
        this.setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            javaMail = new JavaMail();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al inicializar el sistema de correo.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        initializeComponents();
        initializeButtons();
    }
    
    private void initializeComponents(){
        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        this.getContentPane().add(panel);

        Titulo = new JLabel();
        Titulo.setText("Login");
        Titulo.setBounds(430, 50, 200, 50);
        Titulo.setFont(new Font("arial", Font.BOLD, 40));
        panel.add(Titulo);

        userText = new JLabel();
        userText.setText("Username");
        userText.setBounds(430, 200, 200, 50);
        userText.setFont(new Font("arial", Font.BOLD, 20));
        panel.add(userText);

        userPass = new JLabel();
        userPass.setText("Password");
        userPass.setBounds(430, 350, 200, 50);
        userPass.setFont(new Font("arial", Font.BOLD, 20));
        panel.add(userPass);

        Username = new JTextField();
        Username.setBounds(340, 250, 300, 40);
        panel.add(Username);

        Password = new JTextField();
        Password.setBounds(340, 400, 300, 40);
        panel.add(Password);
    }
    
    private void initializeButtons() {
        botonSubmit = new JButton("Submit");
        botonSubmit.setBounds(410, 500, 150, 40);
        botonSubmit.setFont(new Font("Arial", Font.PLAIN, 20));
        botonSubmit.setBackground(Color.LIGHT_GRAY);

        botonSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = Username.getText().trim();
                String password = Password.getText().trim();
                
                if(username.isEmpty() || password.isEmpty()){
                    JOptionPane.showMessageDialog(panel, "Por favor, completa ambos campos.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                try {
                    boolean success = javaMail.login(username, password);
                    if(success){
                        JOptionPane.showMessageDialog(panel, "Login exitoso.", "Exito", JOptionPane.INFORMATION_MESSAGE);
                        mainGUI mg = new mainGUI(javaMail);
                        mg.setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(panel, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(panel, "Error al intentar iniciar sesion.", "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        panel.add(botonSubmit);

        botonAccount = new JButton("Create Account");
        botonAccount.setBounds(10, 610, 200, 40);
        botonAccount.setFont(new Font("Arial", Font.PLAIN, 15));
        botonAccount.setBackground(Color.LIGHT_GRAY);

        botonAccount.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JTextField newUsername = new JTextField();
                JTextField newPassword = new JTextField();
                Object[] message = {
                    "Username:", newUsername,
                    "Password:", newPassword
                };

                int option = JOptionPane.showConfirmDialog(panel, message, "Crear Nueva Cuenta", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    String username = newUsername.getText().trim();
                    String password = newPassword.getText().trim();
                    
                    if(username.isEmpty() || password.isEmpty()){
                        JOptionPane.showMessageDialog(panel, "Ambos campos son requeridos.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    try {
                        javaMail.crearAccount(username, password);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(panel, "Error al crear la cuenta.", "Error", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            }
        });

        panel.add(botonAccount);
    }
}
