package co.edu.uniquindio.unieventos.dto.PQR;

import co.edu.uniquindio.unieventos.model.enums.EstadoPQR;

import java.time.LocalDateTime;

public record ItemPQRDTO(
        String id,
        String idUsuario,
        EstadoPQR estadoPQR,
        LocalDateTime fechaCreacion
) {}
