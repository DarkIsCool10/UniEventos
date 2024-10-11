package co.edu.uniquindio.unieventos.dto.carrito;

import co.edu.uniquindio.unieventos.model.vo.DetalleCarrito;

import java.time.LocalDate;

public record InformacionEventoCarritoDTO(
        DetalleCarritoDTO detalleCarritoDTO,
        String imagenPortada,
        String nombre,
        String direccion,
        LocalDate fecha
) {
}
