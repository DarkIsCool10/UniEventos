package co.edu.uniquindio.unieventos.dto.PQR;

import co.edu.uniquindio.unieventos.model.enums.EstadoPQR;

import java.time.LocalDateTime;

public record ActualizarPQRDTO(
        String id,
        EstadoPQR estadoPQR,
        String respuesta,
        LocalDateTime fechaRespuesta
) {}