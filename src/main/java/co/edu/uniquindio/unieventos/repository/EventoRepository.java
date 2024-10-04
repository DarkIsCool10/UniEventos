package co.edu.uniquindio.unieventos.repository;

import co.edu.uniquindio.unieventos.dto.evento.EventoFiltradoDTO;
import co.edu.uniquindio.unieventos.dto.evento.ItemEventoDTO;
import co.edu.uniquindio.unieventos.model.documents.Evento;
import co.edu.uniquindio.unieventos.model.enums.TipoEvento;
import org.springframework.cglib.core.Local;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventoRepository extends MongoRepository<Evento, String> {

    //Metodo para encontrar un evento por nombre
    Optional<Evento> findByNombre(String nombre);

    //Metodo para contar eventos por ciudad
    long countByCiudad(String ciudad);

    //@Query("{ 'nombre' : ?0,'tipoEvento': ?1, 'ciudad': ?2, 'fecha': { $gte: ?3 } }")
    //List<Evento> filtrarEventosPorTipoCiudadYFecha(String nombre,TipoEvento tipoEvento, String ciudad, LocalDateTime fecha);

    @Query("{ 'tipo': ?0 }")
    List<Evento> filtrarPorTipo(String tipoEvento);

    @Query("{ 'ciudad': ?0 }")
    List<Evento> filtrarPorCiudad(String ciudad);

    @Query("{ 'fecha': { $gte: ?0, $lte: ?1 } }")
    List<Evento> filtrarPorRangoDeFechas(LocalDateTime desde, LocalDateTime hasta);

    // Método para encontrar un evento por su ID
    Optional<Evento> findById(String id);

}
