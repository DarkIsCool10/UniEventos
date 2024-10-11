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

    ObtenerEventoDTO obtenerLocalidadPorNombre(String nombre) throws EventoException;

    List<ItemEventoDTO> listarEventos();

    public List<EventoFiltradoDTO> filtrarEventos(FiltroEventoDTO filtroEventoDTO);

}
