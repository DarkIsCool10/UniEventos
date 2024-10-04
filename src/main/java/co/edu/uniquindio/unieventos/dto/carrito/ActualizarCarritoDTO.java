package co.edu.uniquindio.unieventos.dto.carrito;

import java.time.LocalDateTime;
import java.util.List;
import co.edu.uniquindio.unieventos.model.vo.DetalleCarrito;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record ActualizarCarritoDTO(

        @NotBlank(message = "Los items del carrito no puede ser nula ni vacia.")
        List<DetalleCarrito> items,

        @NotNull(message = "La fecha de agregación es obligatoria")
        @PastOrPresent(message = "La fecha de agregación no puede ser en el futuro")
        LocalDateTime fecha
) {}
