package co.edu.uniquindio.unieventos.dto.PQR;

import co.edu.uniquindio.unieventos.model.enums.CategoriaPQR;
import org.bson.types.ObjectId;

public record CrearPQRDTO(
        String idUsuario,
        CategoriaPQR categoriaPQR,
        String descripcion

) {}
