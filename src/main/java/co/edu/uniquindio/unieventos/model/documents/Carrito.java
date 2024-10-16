package co.edu.uniquindio.unieventos.model.documents;

import co.edu.uniquindio.unieventos.model.vo.DetalleCarrito;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document("carrito")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Carrito {

    @Id
    @EqualsAndHashCode.Include
    private String id;
    private String idUsuario;
    private List<DetalleCarrito> items;
    private LocalDateTime fecha;

    public Carrito(String idUsuario, List<DetalleCarrito> items, LocalDateTime fecha) {
        this.idUsuario = idUsuario;
        this.items = items;
        this.fecha = fecha;
    }
}
