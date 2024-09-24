package co.edu.uniquindio.unieventos.dto.carrito;

import java.time.LocalDateTime;
import java.util.List;
import co.edu.uniquindio.unieventos.model.vo.DetalleCarrito;

public record ActualizarCarritoDTO(
        List<DetalleCarrito> items,
        LocalDateTime fecha
) {}
