package co.edu.uniquindio.unieventos.repository;

import co.edu.uniquindio.unieventos.model.documents.Orden;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdenRepository extends MongoRepository<Orden, String> {

    Optional<Orden> findById(String idOrden);

    List<Orden> findByIdCliente(String idCliente);
    List<Orden> findByIdOrden(String idOrden);

}
