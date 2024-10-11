package co.edu.uniquindio.unieventos.dto.orden;

import co.edu.uniquindio.unieventos.model.enums.TipoEvento;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record MostrarOrdenDTO(

        @NotBlank(message = "El ID del usuario no puede estar vacío.")
        String idUsuario,

        @NotBlank(message = "El nombre del usuario no puede estar vacío.")
        @Size(max = 100, message = "El nombre del usuario no puede exceder los 100 caracteres.")
        String nombreUsuario,

        @NotBlank(message = "El ID del evento no puede estar vacío.")
        String idEvento,

        @NotBlank(message = "El nombre del evento no puede estar vacío.")
        @Size(max = 100, message = "El nombre del evento no puede exceder los 100 caracteres.")
        String nombreEvento,

        @NotNull(message = "La fecha de compra es obligatoria.")
        LocalDate fechaCompra,

        @NotNull(message = "La fecha del evento es obligatoria.")
        LocalDate fechaEvento,

        @NotNull(message = "El tipo de evento es obligatorio.")
        TipoEvento tipoEvento,

        @NotBlank(message = "El nombre de la localidad no puede estar vacío.")
        String localidad,

        @NotNull(message = "El precio es obligatorio.")
        @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0.")
        Double precio,

        @Min(value = 1, message = "La cantidad debe ser al menos 1.")
        int cantidad,

        String cupon,

        @NotNull(message = "El total es obligatorio.")
        @DecimalMin(value = "0.0", inclusive = false, message = "El total debe ser mayor a 0.")
        Double total

) {}

