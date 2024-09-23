package co.edu.uniquindio.unieventos.service.service;

import co.edu.uniquindio.unieventos.dto.cupon.*;
import co.edu.uniquindio.unieventos.exceptions.CuponException;
import co.edu.uniquindio.unieventos.model.documents.Cupon;
import co.edu.uniquindio.unieventos.model.enums.EstadoCupon;
import co.edu.uniquindio.unieventos.model.enums.TipoCupon;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface CuponService {

    String crearCupon(CrearCuponDTO cupon) throws CuponException;

    String editarCupon(EditarCuponDTO cupon, String cuponId) throws CuponException;

    String eliminarCupon(String id) throws CuponException;

    InformacionCuponDTO obtenerInformacionCupon(String id) throws CuponException;

    List<Cupon> obtenerCuponesFiltrados(
        String nombre,
        LocalDateTime fechaVencimiento,
        LocalDateTime fechaApertura,
        Float descuento,
        TipoCupon tipo,
        EstadoCupon estado);

    String aplicarCupon(String codigoCupon, LocalDateTime fechaCompra) throws CuponException;

    String fechaAperturaCupon(LocalDateTime fechaApertura) throws CuponException;
    //Aun falta


}