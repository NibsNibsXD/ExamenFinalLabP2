
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JOptionPane;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Jorge Aguirre
 */

public class newEmailGUI extends JFrame {

    private JPanel mainPanel;
    private JTextField toField, subjectField;
    private JTextArea contentArea;
    private JButton sendButton;

    private JavaMail javaMail;
    private mainGUI parentGUI;

    public newEmailGUI(JavaMail javaMail, mainGUI parentGUI) {
        this.javaMail = javaMail;
        this.parentGUI = parentGUI;
        this.setSize(500, 500);
        setTitle("Nuevo Correo");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initializeComponents();
    }

    public void initializeComponents() {
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.WHITE);
        this.add(mainPanel);

        JLabel toLabel = new JLabel("Para (username):");
        toLabel.setBounds(20, 20, 120, 25);
        mainPanel.add(toLabel);

        toField = new JTextField();
        toField.setBounds(150, 20, 300, 25);
        mainPanel.add(toField);

        JLabel subjectLabel = new JLabel("Asunto:");
        subjectLabel.setBounds(20, 60, 100, 25);
        mainPanel.add(subjectLabel);

        subjectField = new JTextField();
        subjectField.setBounds(150, 60, 300, 25);
        mainPanel.add(subjectField);

        JLabel contentLabel = new JLabel("Contenido:");
        contentLabel.setBounds(20, 100, 100, 25);
        mainPanel.add(contentLabel);

        contentArea = new JTextArea();
        contentArea.setBounds(20, 130, 430, 200);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setBackground(Color.LIGHT_GRAY);
        mainPanel.add(contentArea);

        sendButton = new JButton("Enviar");
        sendButton.setBounds(180, 350, 120, 40);
        sendButton.setBackground(Color.GREEN);
        sendButton.setForeground(Color.WHITE);
        mainPanel.add(sendButton);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String to = toField.getText().trim();
                String subject = subjectField.getText().trim();
                String content = contentArea.getText().trim();

                if (to.isEmpty() || subject.isEmpty() || content.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Todos los campos deben estar llenos.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        javaMail.createEmail(to, subject, content);
                        JOptionPane.showMessageDialog(null, "Correo enviado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        mainGUI mg=new mainGUI(javaMail);
                        mg.setVisible(true);
                        dispose(); 
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error al enviar el correo.", "Error", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            }
        });

    }
}
