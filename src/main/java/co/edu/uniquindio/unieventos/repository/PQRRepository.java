package co.edu.uniquindio.unieventos.repository;

import co.edu.uniquindio.unieventos.model.documents.PQR;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PQRRepository extends MongoRepository<PQR,String> {
}
