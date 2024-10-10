package co.edu.uniquindio.unieventos.service.service;

import co.edu.uniquindio.unieventos.dto.orden.CrearOrdenDTO;
import co.edu.uniquindio.unieventos.dto.orden.EditarOrdenDTO;
import co.edu.uniquindio.unieventos.exceptions.OrdenException;
import co.edu.uniquindio.unieventos.model.documents.Orden;

import java.util.Map;
import com.mercadopago.resources.preference.Preference;

public interface OrdenService {

    void crearOrden(CrearOrdenDTO ordenDTO) throws OrdenException;

    Orden obtenerOrdenPorId(String ordenId) throws OrdenException;

    void actualizarOrden (String id, EditarOrdenDTO ordenDTO) throws OrdenException;

    void eliminarOrden(String id) throws OrdenException;

    Preference realizarPago(String idOrden) throws Exception;

    void recibirNotificacionMercadoPago(Map<String, Object> request);
}
