package co.edu.uniquindio.unieventos.service.service;

import co.edu.uniquindio.unieventos.dto.orden.CrearOrdenDTO;
import co.edu.uniquindio.unieventos.dto.orden.EditarOrdenDTO;
import co.edu.uniquindio.unieventos.dto.orden.MostrarOrdenDTO;
import co.edu.uniquindio.unieventos.exceptions.CarritoException;
import co.edu.uniquindio.unieventos.exceptions.CuentaException;
import co.edu.uniquindio.unieventos.exceptions.CuponException;
import co.edu.uniquindio.unieventos.exceptions.OrdenException;
import co.edu.uniquindio.unieventos.model.documents.Orden;

import java.util.Map;
import com.mercadopago.resources.preference.Preference;

public interface OrdenService {

    void crearOrdenDesdeCarrito(String idCliente, String codigoCupon, String codigoPasarela) throws OrdenException, CarritoException, CuponException;

    void crearOrden(CrearOrdenDTO ordenDTO, double totalOrden) throws OrdenException;

    MostrarOrdenDTO mostrarOrden(String idOrden) throws OrdenException, CuentaException;

    Orden obtenerOrdenPorId(String ordenId) throws OrdenException;

    void actualizarOrden (String id, EditarOrdenDTO ordenDTO) throws OrdenException;

    void eliminarOrden(String id) throws OrdenException;

    Preference realizarPago(String idOrden) throws Exception;

    void recibirNotificacionMercadoPago(Map<String, Object> request);
}
