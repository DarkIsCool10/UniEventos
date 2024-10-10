package co.edu.uniquindio.unieventos.dto.orden;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record EditarOrdenDTO (

        @NotNull(message = "Usuario no encontrado")
        String id,

        String idCupon,  // Este campo puede ser nulo si no se usa un cupón

        @NotNull(message = "El total de la orden es obligatorio.")
        @Size(min = 1, message = "El total debe ser mayor a 0.")
        float total

    ){
}