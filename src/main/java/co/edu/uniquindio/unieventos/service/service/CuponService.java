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

    void crearCupon(CrearCuponDTO cupon) throws CuponException;

    void editarCupon(EditarCuponDTO cupon, String cuponId) throws CuponException;

    void eliminarCupon(String id) throws CuponException;

    InformacionCuponDTO obtenerInformacionCupon(String id) throws CuponException;

    List<InformacionCuponDTO> obtenerTodosLosCupones();

    List<ItemsCuponDTO> obtenerCuponesFiltrados(ItemsCuponDTO itemCuponDTO);

    Cupon aplicarCupon(String codigoCupon, LocalDateTime fechaCompra) throws CuponException;
    //Aun falta

}