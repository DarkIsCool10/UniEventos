package co.edu.uniquindio.unieventos.dto.PQR;

import java.time.LocalDateTime;

public record ResponderPQRDTO(
        String id,
        String respuesta,
        LocalDateTime fechaRespuesta
) {}
