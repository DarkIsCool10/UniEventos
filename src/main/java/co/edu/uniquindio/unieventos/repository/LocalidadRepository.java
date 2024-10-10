package co.edu.uniquindio.unieventos.repository;

import co.edu.uniquindio.unieventos.model.vo.Localidad;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalidadRepository extends MongoRepository<Localidad, String> {
}
