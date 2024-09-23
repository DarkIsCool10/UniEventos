package co.edu.uniquindio.unieventos.service.service;

import co.edu.uniquindio.unieventos.dto.orden.CrearOrdenDTO;
import co.edu.uniquindio.unieventos.dto.orden.EditarOrdenDTO;
import co.edu.uniquindio.unieventos.exceptions.OrdenException;

public interface OrdenService {

    String crearOrden(CrearOrdenDTO orden) throws OrdenException;

    String editarOrden(EditarOrdenDTO orden, String ordenId) throws OrdenException;

    String eliminarOrden(String id) throws OrdenException;
}
