package co.edu.uniquindio.unieventos.dto.carrito;

import co.edu.uniquindio.unieventos.model.vo.DetalleCarrito;
import java.util.List;

public record MostrarCarritoDTO(
        List<DetalleCarrito> detalleCarrito
) {
}
