package co.edu.uniquindio.unieventos.service.service;

import co.edu.uniquindio.unieventos.dto.evento.*;
import co.edu.uniquindio.unieventos.exceptions.EventoException;
import co.edu.uniquindio.unieventos.model.documents.Evento;
import co.edu.uniquindio.unieventos.model.enums.TipoEvento;
import co.edu.uniquindio.unieventos.model.vo.Localidad;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

import java.time.LocalDateTime;
import java.util.List;

public interface EventoService {

    void crearEvento(CrearEventoDTO crearEventoDTO) throws EventoException;

    void editarEvento(String id,EditarEventoDTO editarEventoDTO) throws EventoException;

    void eliminarEvento(String id) throws EventoException;

    Evento obtenerInformacionEvento(String id) throws EventoException;

    //Localidad obtenerLocalidadPorNombre(String nombre) throws EventoException;

    List<ItemEventoDTO> listarEventos();

    Page<ItemEventoDTO> getEventoActivos(Pageable pageable);

    Page<ItemEventoDTO> getEventosInactivos(Pageable pageable);

    public List<EventoFiltradoDTO> filtrarEventos(FiltroEventoDTO filtroEventoDTO);

}
