/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Jorge Aguirre
 */


public class EmailNodo {
    public long posicion;
    public String envia;
    public String asunto;
    public boolean leido;
    public EmailNodo siguiente;

    public EmailNodo(long posicion, String envia, String asunto, boolean leido) {
        this.posicion = posicion;
        this.envia = envia;
        this.asunto = asunto;
        this.leido = leido;
        this.siguiente = null;
    }
}
