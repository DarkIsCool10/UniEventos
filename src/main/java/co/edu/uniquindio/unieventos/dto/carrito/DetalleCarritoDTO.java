package co.edu.uniquindio.unieventos.dto.carrito;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record DetalleCarritoDTO(
        @NotNull(message = "El ID del evento no puede ser nulo")
        String idEvento,  // Añadir este campo

        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        int cantidad,

        @NotBlank(message = "El nombre de la localidad no puede estar vacío")
        String nombreLocalidad
) {
}
