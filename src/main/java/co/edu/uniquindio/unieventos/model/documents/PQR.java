package co.edu.uniquindio.unieventos.model.documents;

import co.edu.uniquindio.unieventos.model.enums.CategoriaPQR;
import co.edu.uniquindio.unieventos.model.enums.EstadoPQR;
import com.mongodb.internal.connection.LoadBalancedClusterableServerFactory;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("pqrs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PQR {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    private ObjectId idUsuario;
    private LocalDateTime fechaCreacion;
    private EstadoPQR estadoPQR;
    private CategoriaPQR categoriaPQR;
    private String descripcion;
    private String respuesta;
    private LocalDateTime fechaRespuesta;

}