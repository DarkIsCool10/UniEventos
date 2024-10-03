package co.edu.uniquindio.unieventos.repository;

import co.edu.uniquindio.unieventos.model.documents.Evento;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EventoRepository extends MongoRepository<Evento, String> {

    //Metodo para encontrar un evento por nombre
    Optional<Evento> findByNombre(String nombre);

    //Metodo para contar eventos por ciudad
    long countByCiudad(String ciudad);
    // Método para encontrar un evento por su ID
    Optional<Evento> findById(String id);

}
