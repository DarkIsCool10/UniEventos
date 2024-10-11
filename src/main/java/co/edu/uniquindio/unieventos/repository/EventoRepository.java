package co.edu.uniquindio.unieventos.repository;

import co.edu.uniquindio.unieventos.dto.evento.ObtenerEventoDTO;
import co.edu.uniquindio.unieventos.model.documents.Evento;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventoRepository extends MongoRepository<Evento, String> {

    //Metodo para encontrar un evento por nombre
    Optional<Evento> findByNombre(String nombre);

    //Metodo para contar eventos por ciudad
    long countByCiudad(String ciudad);

    @Query("{ 'fecha': { $gte: ?0, $lte: ?1 } }")
    List<Evento> filtrarPorRangoDeFechas(LocalDateTime desde, LocalDateTime hasta);

    @Query("{"
            + "'$or': ["
            + "   { 'nombre': { $regex: ?0, $options: 'i' } },"
            + "   { 'tipo': { $eq: ?1 } },"
            + "   { 'ciudad': { $regex: ?2, $options: 'i' } },"
            + "   { 'fecha': { $eq: ?3 } }"
            + "]"
            + "}")
    List<Evento> filtrarEventos(String nombre, String tipoEvento, String ciudad, LocalDate fecha);

    Optional<ObtenerEventoDTO> findByLocalidadesNombre(String nombre);

}
