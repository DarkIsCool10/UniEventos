package co.edu.uniquindio.unieventos.service.service;

import co.edu.uniquindio.unieventos.dto.carrito.ActualizarCarritoDTO;
import co.edu.uniquindio.unieventos.dto.carrito.CrearCarritoDTO;
import co.edu.uniquindio.unieventos.dto.carrito.MostrarCarritoDTO;
import co.edu.uniquindio.unieventos.exceptions.CarritoException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CarritoService {

    String crearCarrito(CrearCarritoDTO carritoDTO) throws CarritoException;

    String actualizarCarrito(ActualizarCarritoDTO carritoDTO) throws CarritoException;

    String eliminarCarrito(String idCarrito) throws CarritoException;

    MostrarCarritoDTO obtenerCarritoPorId(String idCarrito) throws CarritoException;

    List<MostrarCarritoDTO> listarCarritosPorUsuario(String idUsuario) throws CarritoException;

    String vaciarCarrito(String idCarrito) throws CarritoException;

    double calcularTotalCarrito(String idCarrito) throws CarritoException;

    String finalizarCompra(String idCarrito) throws CarritoException;

    boolean verificarDisponibilidadItems(String idCarrito) throws CarritoException;

}
