package co.edu.uniquindio.unieventos.dto.orden;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.List;

public record CrearOrdenDTO(

        @NotBlank(message = "El ID del cliente no puede estar vacío.")
        String idCliente,

        String idCupon,  // Este campo puede ser nulo si no se usa un cupón

        @NotBlank(message = "El código de la pasarela de pago es obligatorio.")
        @Size(max = 50, message = "El código de la pasarela de pago no puede tener más de 50 caracteres.")
        String codigoPasarela,

        @NotNull(message = "La fecha de la orden es obligatoria.")
        @PastOrPresent(message = "La fecha de la orden no puede ser futura.")
        LocalDateTime fecha,

        @NotNull(message = "La lista de detalles de la orden no puede estar vacía.")
        @Size(min = 1, message = "La orden debe tener al menos un detalle.")
        List<DetalleOrdenDTO> detalleOrden,

        @NotNull(message = "El total de la orden es obligatorio.")
        @Size(min = 1, message = "El total debe ser mayor a 0.")
        float total

) {}
