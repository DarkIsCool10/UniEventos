package co.edu.uniquindio.unieventos.exceptions;

public class OrdenException extends Exception{

    //Metodo que permite enviar mensaje de error mediante una excepcion
    public OrdenException(String message) {
        super(message);
    }
}