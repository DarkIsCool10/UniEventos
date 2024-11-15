package co.edu.uniquindio.unieventos.dto.evento;

import co.edu.uniquindio.unieventos.model.enums.EstadoEvento;
import co.edu.uniquindio.unieventos.model.enums.TipoEvento;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record CrearEventoDTO(
        @NotEmpty(message = "La imagen de portada es obligatoria.")
        String imagenPortada,

        @NotEmpty(message = "La imagen de las localidades es obligatoria.")
        String imagenLocalidad,

        @NotEmpty(message = "El nombre del evento es obligatorio.")
        String nombre,

        @NotEmpty(message = "La descripción del evento es obligatoria.")
        String descripcion,

        @NotEmpty(message = "La dirección del evento es obligatoria.")
        String direccion,

        @NotNull(message = "El tipo de evento es obligatorio.")
        TipoEvento tipoEvento,

        @NotNull(message = "La fecha del evento es obligatoria.")
        LocalDate fecha,

        @NotEmpty(message = "La ciudad es obligatoria.")
        String ciudad,

        @NotNull(message = "La lista de localidades no puede ser nula.")
        List<CrearLocalidadDTO> listaLocalidades
) {
}
