package co.edu.uniquindio.unieventos.dto.carrito;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record CrearCarritoDTO(

        @NotBlank (message = "El id del usuario no puede ser nulo o vacio")
        String idUsuario,

        @NotBlank(message = "Los items del carrito no puede ser nula ni vacia.")
        List<DetalleCarritoDTO> itemsCarrito,

        @NotNull(message = "La fecha del evento es obligatoria.")
        @FutureOrPresent (message = "La fecha del carrito no puede ser anterior a la actual")
        LocalDateTime fecha

        ) {
}
