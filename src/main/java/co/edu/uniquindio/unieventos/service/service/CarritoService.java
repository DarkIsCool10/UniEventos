package co.edu.uniquindio.unieventos.service.service;

import co.edu.uniquindio.unieventos.dto.carrito.CrearCarritoDTO;
import co.edu.uniquindio.unieventos.dto.carrito.DetalleCarritoDTO;
import co.edu.uniquindio.unieventos.dto.carrito.InformacionEventoCarritoDTO;
import co.edu.uniquindio.unieventos.exceptions.CarritoException;
import co.edu.uniquindio.unieventos.model.documents.Carrito;
import co.edu.uniquindio.unieventos.model.vo.DetalleCarrito;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CarritoService {

    void crearCarrito(CrearCarritoDTO carritoDTO) throws CarritoException;

    public Carrito obtenerCarritoPorUsuario(String idUsuario) throws CarritoException;

    Carrito agregarItemsAlCarrito(String idUsuario, List<DetalleCarritoDTO> nuevosItemsDTO) throws CarritoException;

    Carrito eliminarItemDelCarrito(String idUsuario, String nombreLocalidad) throws CarritoException;

    Carrito vaciarCarrito(String idUsuario) throws CarritoException;

    public List<InformacionEventoCarritoDTO> listarProductosEnCarrito(String idUsuario) throws CarritoException;

    double calcularTotalCarrito(String idUsuario) throws CarritoException;

    boolean validarDisponibilidadEntradas(String idCarrito) throws CarritoException;

}
