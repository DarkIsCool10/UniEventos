package co.edu.uniquindio.unieventos.service.service;

import co.edu.uniquindio.unieventos.dto.evento.*;
import co.edu.uniquindio.unieventos.exceptions.EventoException;
import co.edu.uniquindio.unieventos.model.documents.Evento;
import co.edu.uniquindio.unieventos.model.enums.TipoEvento;
import co.edu.uniquindio.unieventos.model.vo.Localidad;
import java.time.LocalDate;

import java.time.LocalDateTime;
import java.util.List;

public interface EventoService {

    void crearEvento(CrearEventoDTO crearEventoDTO) throws EventoException;

    void editarEvento(EditarEventoDTO editarEventoDTO) throws EventoException;

    void eliminarEvento(String id) throws EventoException;

    Evento obtenerInformacionEvento(String id) throws EventoException;

    Localidad obtenerLocalidadPorNombre(String nombre) throws EventoException;

    List<ItemEventoDTO> listarEventos();

    List<EventoFiltradoDTO> filtrarPorTipo(TipoEvento tipoEvento);

    List<EventoFiltradoDTO> filtrarPorFecha(LocalDate fecha);

    List<EventoFiltradoDTO> filtrarPorCiudad(String ciudad);

    List<EventoFiltradoDTO> filtrarPorRangoDeFechas(LocalDateTime desde, LocalDateTime hasta);

    //List<ItemEventoDTO> filtrarEventos(FiltroEventoDTO filtroEventoDTO);

}
