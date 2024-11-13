package co.edu.uniquindio.unieventos.dto.orden;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DetalleOrdenDTO(

        @NotBlank(message = "El ID del evento no puede estar vacío.")
        String idEvento,

        @NotBlank(message = "El nombre de la localidad no puede estar vacío.")
        @Size(max = 50, message = "El nombre de la localidad no puede tener más de 50 caracteres.")
        String nombreLocalidad,

        @NotNull(message = "La cantidad de entradas es obligatoria.")
        @Min(value = 1, message = "La cantidad de entradas debe ser al menos 1.")
        int cantidad,

        @NotNull(message = "El precio es obligatorio.")
        double precio
) {}
