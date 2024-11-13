package co.edu.uniquindio.unieventos.dto.carrito;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record DetalleCarritoDTO(

        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        int cantidad,

        @NotBlank(message = "El nombre de la localidad no puede estar vacío")
        String nombreLocalidad,

        @NotNull(message = "La fecha de agregación es obligatoria")
        @PastOrPresent(message = "La fecha de agregación no puede ser en el futuro")
        LocalDate fechaAgregacion
) {
}