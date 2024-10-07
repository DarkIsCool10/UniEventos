package co.edu.uniquindio.unieventos.model.vo;

import lombok.*;
import org.bson.types.ObjectId;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DetalleOrden {

    private ObjectId idEvento;
    private float precio;
    private String nombreLocalidad;
    private int cantidad;

}
