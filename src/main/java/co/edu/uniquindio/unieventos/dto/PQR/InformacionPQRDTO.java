package co.edu.uniquindio.unieventos.dto.PQR;

import co.edu.uniquindio.unieventos.model.enums.CategoriaPQR;
import co.edu.uniquindio.unieventos.model.enums.EstadoPQR;

import java.time.LocalDateTime;

public record InformacionPQRDTO(
        String id,
        String idUsuario,
        LocalDateTime fechaCreacion,
        EstadoPQR estadoPQR,
        CategoriaPQR categoriaPQR,
        String descripcion,
        String respuesta,
        LocalDateTime fechaRespuesta
) {}