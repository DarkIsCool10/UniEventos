package co.edu.uniquindio.unieventos.repository;

import co.edu.uniquindio.unieventos.model.documents.Cupon;
import co.edu.uniquindio.unieventos.model.enums.EstadoCupon;
import co.edu.uniquindio.unieventos.model.enums.TipoCupon;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CuponRepository extends MongoRepository<Cupon, String> {

    boolean existsByCodigo(String codigo);

    List<Cupon> findByNombreContainingIgnoreCase(String nombre);
    List<Cupon> findByFechaVencimientoAfter(LocalDate fechaVencimiento);
    List<Cupon> findByFechaAperturaAfter(LocalDate fechaApertura);
    List<Cupon> findByDescuento(Float descuento);
    List<Cupon> findByTipo(TipoCupon tipo);
    List<Cupon> findByEstado(EstadoCupon estado);
    Cupon findByCodigo(String codigo);
}
