

//MIEMBROS DEL GRUPO
//JORGE, JAFET Y FERNANDO
//EXAMEN FINAL LAB P2


import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.NoSuchElementException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Jorge Aguirre
 */

public class CurrentUser {
    public String username;
    public RandomAccessFile archivoEmails;
    public EmailNodo primerEmail;

    public CurrentUser(String username) throws IOException {
        this.username = username;
        this.archivoEmails = new RandomAccessFile("emails_" + username + ".bin", "rw");
        this.primerEmail = null;
    }

    public void add(EmailNodo email) {
        if (primerEmail == null) {
            primerEmail = email;
        } else {
            EmailNodo actual = primerEmail;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = email;
        }
    }

    public void loadFromFile() throws IOException {
        archivoEmails.seek(0);
        primerEmail = null;
        while (archivoEmails.getFilePointer() < archivoEmails.length()) {
            long posicion = archivoEmails.getFilePointer();
            boolean leido = archivoEmails.readBoolean();
            String envia = archivoEmails.readUTF();
            String asunto = archivoEmails.readUTF();
            String contenido = archivoEmails.readUTF();
            long fecha = archivoEmails.readLong();
            EmailNodo email = new EmailNodo(posicion, envia, asunto, leido);
            add(email);
        }
    }

    public long gotEmail(String envia, String asunto, String contenido) throws IOException {
        archivoEmails.seek(archivoEmails.length());
        long posicion = archivoEmails.getFilePointer();
        archivoEmails.writeBoolean(false);
        archivoEmails.writeUTF(envia);
        archivoEmails.writeUTF(asunto);
        archivoEmails.writeUTF(contenido);
        archivoEmails.writeLong(System.currentTimeMillis());
        return posicion;
    }

    public String readEmailReturnString(int posicionPos) throws IOException, NoSuchElementException {
        EmailNodo actual = primerEmail;
        int contador = 1;
        while (actual != null && contador < posicionPos) {
            actual = actual.siguiente;
            contador++;
        }
        if (actual == null) {
            throw new NoSuchElementException("La posicion " + posicionPos + " no existe.");
        }
        archivoEmails.seek(actual.posicion);
        boolean leido = archivoEmails.readBoolean();
        String envia = archivoEmails.readUTF();
        String asunto = archivoEmails.readUTF();
        String contenido = archivoEmails.readUTF();
        long fecha = archivoEmails.readLong();

        StringBuilder emailDetails = new StringBuilder();
        emailDetails.append("De: ").append(envia).append("\n")
                    .append("Asunto: ").append(asunto).append("\n")
                    .append("Contenido: ").append(contenido).append("\n")
                    .append("Fecha: ").append(new java.util.Date(fecha)).append("\n");

        if (!leido) {
            archivoEmails.seek(actual.posicion);
            archivoEmails.writeBoolean(true);
            actual.leido = true;
        }

        return emailDetails.toString();
    }
}
