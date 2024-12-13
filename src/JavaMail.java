    

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.NoSuchElementException;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Jorge Aguirre
 */

public class JavaMail {
    private RandomAccessFile archivoUsuarios;
    private CurrentUser currentUser;

    public JavaMail() throws IOException {
        this.archivoUsuarios = new RandomAccessFile("usuarios.bin", "rw");
        this.currentUser = null;
    }

    public boolean login(String username, String password) throws IOException {
        archivoUsuarios.seek(0);
        while (archivoUsuarios.getFilePointer() < archivoUsuarios.length()) {
            String user = archivoUsuarios.readUTF();
            String pass = archivoUsuarios.readUTF();
            if (user.equals(username) && pass.equals(password)) {
                currentUser = new CurrentUser(username);
                currentUser.loadFromFile();
                return true;
            }
        }
        return false;
    }

    public void crearAccount(String username, String password) throws IOException {
        archivoUsuarios.seek(0);
        while (archivoUsuarios.getFilePointer() < archivoUsuarios.length()) {
            String user = archivoUsuarios.readUTF();
            archivoUsuarios.readUTF();
            if (user.equals(username)) {
                JOptionPane.showMessageDialog(null, "El nombre de usuario ya existe. Por favor, elige otro.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        archivoUsuarios.seek(archivoUsuarios.length());
        archivoUsuarios.writeUTF(username);
        archivoUsuarios.writeUTF(password);
        RandomAccessFile nuevoArchivoEmails = new RandomAccessFile("emails_" + username + ".bin", "rw");
        nuevoArchivoEmails.close();
        JOptionPane.showMessageDialog(null, "Cuenta creada exitosamente para el usuario: " + username, "Exito", JOptionPane.INFORMATION_MESSAGE);
    }

    public void createEmail(String toUsername, String subject, String content) throws IOException {
        if (currentUser == null) {
            throw new NullPointerException("Login Primero");
        }
        boolean userExists = false;
        archivoUsuarios.seek(0);
        while (archivoUsuarios.getFilePointer() < archivoUsuarios.length()) {
            String user = archivoUsuarios.readUTF();
            archivoUsuarios.readUTF();
            if (user.equals(toUsername)) {
                userExists = true;
                break;
            }
        }
        if (!userExists) {
            JOptionPane.showMessageDialog(null, "El usuario destinatario no existe.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (toUsername.equals(currentUser.username)) {
            long posicion = currentUser.gotEmail(currentUser.username, subject, content);
            EmailNodo nuevoEmail = new EmailNodo(posicion, currentUser.username, subject, false);
            currentUser.add(nuevoEmail);
            // Recargar la lista de emails para asegurar que el inbox se actualice
            currentUser.loadFromFile();
        } else {
            CurrentUser destinatario = new CurrentUser(toUsername);
            destinatario.loadFromFile();
            long posicion = destinatario.gotEmail(currentUser.username, subject, content);
            EmailNodo nuevoEmail = new EmailNodo(posicion, currentUser.username, subject, false);
            destinatario.add(nuevoEmail);
            destinatario.archivoEmails.close();
        }
        JOptionPane.showMessageDialog(null, "Correo enviado exitosamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);
    }

    public void inboxGUI(JTextArea areaTexto) {
        if (currentUser == null) {
            JOptionPane.showMessageDialog(null, "Login Primero", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        StringBuilder inboxBuilder = new StringBuilder();
        EmailNodo actual = currentUser.primerEmail;
        int contador = 0;
        int posicion = 1;

        while (actual != null) {
            String estado = actual.leido ? "LEIDO" : "NO LEIDO";
            inboxBuilder.append(posicion)
                        .append(" - ")
                        .append(actual.envia)
                        .append(" - ")
                        .append(actual.asunto)
                        .append(" - ")
                        .append(estado)
                        .append("\n");
            contador++;
            posicion++;
            actual = actual.siguiente;
        }

        inboxBuilder.append("Total de emails recibidos: ").append(contador);
        areaTexto.setText(inboxBuilder.toString());
    }


    public void readGUI(int pos, JTextArea areaTexto) {
        if (currentUser == null) {
            JOptionPane.showMessageDialog(null, "Login Primero", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            String emailDetails = currentUser.readEmailReturnString(pos);
            areaTexto.setText(emailDetails);
        } catch (NoSuchElementException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al leer el email.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public CurrentUser getCurrentUser(){
        return this.currentUser;
    }
}
