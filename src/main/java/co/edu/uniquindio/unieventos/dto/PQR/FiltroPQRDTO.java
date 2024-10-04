package co.edu.uniquindio.unieventos.dto.PQR;

import co.edu.uniquindio.unieventos.model.enums.CategoriaPQR;
import co.edu.uniquindio.unieventos.model.enums.EstadoPQR;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

public record FiltroPQRDTO(
        EstadoPQR estadoPQR,
        CategoriaPQR categoriaPQR,
        ObjectId idUsuario,
        LocalDateTime fechaDesde,
        LocalDateTime fechaHasta
) {}