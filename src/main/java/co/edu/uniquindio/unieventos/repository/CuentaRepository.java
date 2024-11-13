package co.edu.uniquindio.unieventos.repository;

import co.edu.uniquindio.unieventos.model.documents.Cuenta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CuentaRepository extends MongoRepository<Cuenta, String>{

    boolean existsByEmail(String email);
    Optional<Cuenta> findByEmail(String correo);

    @Query("{'cedula': ?0}")
    Optional<Cuenta> findByCedula(String cedula);

    @Query("{'codigoVerificacionContrasenia': ?0}")
    Optional<Cuenta> existBycodigoVerificacionContrasenia(String codigoVerificacionContrasenia);

    Optional<Cuenta> findByCodigoVerificacionContrasenia(String codigoVerificacionContrasenia);

}
