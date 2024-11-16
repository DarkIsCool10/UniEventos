package co.edu.uniquindio.unieventos.service.implement;

import co.edu.uniquindio.unieventos.dto.evento.*;
import co.edu.uniquindio.unieventos.exceptions.EventoException;
import co.edu.uniquindio.unieventos.model.documents.Evento;
import co.edu.uniquindio.unieventos.model.enums.EstadoEvento;
import co.edu.uniquindio.unieventos.model.vo.Localidad;
import co.edu.uniquindio.unieventos.repository.EventoRepository;
import co.edu.uniquindio.unieventos.service.service.EventoService;
import co.edu.uniquindio.unieventos.service.service.ImagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class EventoServiceImp implements EventoService {

    private final EventoRepository eventoRepo;
    private final ImagesService imagesService;
    private final EventoRepository eventoRepository;

    //Metodo para obtener un evento mediante el id
    private Optional<Evento> obtenerEventoPorId(String idEvento) throws EventoException {
        try {
            return eventoRepo.findById(idEvento);
        } catch (Exception e) {
            throw new EventoException("Error al obtener el evento con ID: " + idEvento);
        }
    }

    @Override
    public void crearEvento(CrearEventoDTO crearEventoDTO) throws EventoException {

            Evento nuevoEvento = new Evento();
            nuevoEvento.setImagenPortada(crearEventoDTO.imagenPortada());
            nuevoEvento.setImagenLocalidad(crearEventoDTO.imagenLocalidad());
            nuevoEvento.setNombre(crearEventoDTO.nombre());
            nuevoEvento.setDescripcion(crearEventoDTO.descripcion());
            nuevoEvento.setDireccion(crearEventoDTO.direccion());
            nuevoEvento.setTipo(crearEventoDTO.tipoEvento());
            nuevoEvento.setEstado(EstadoEvento.ACTIVO);
            nuevoEvento.setFecha(crearEventoDTO.fecha());
            nuevoEvento.setCiudad(crearEventoDTO.ciudad());

            // Crear localidades
            List<Localidad> localidades = crearLocalidades(crearEventoDTO.listaLocalidades());
            nuevoEvento.setLocalidades(localidades);

            // Guardar el nuevo evento en la base de datos
            eventoRepo.save(nuevoEvento);
    }

    /**
     * Metodo para crear localidades
     * @param listaLocalidadesDTO
     * @return lista de localidades
     */
    private List<Localidad> crearLocalidades(List<CrearLocalidadDTO> listaLocalidadesDTO) {
        List<Localidad> localidades = new ArrayList<>(listaLocalidadesDTO.size());
        for (CrearLocalidadDTO crearLocalidadDTO : listaLocalidadesDTO) {
                // Crear una nueva Localidad con la URL de la imagen
                Localidad localidad = new Localidad(
                        crearLocalidadDTO.nombre(),
                        crearLocalidadDTO.capacidadMaxima(),
                        crearLocalidadDTO.precio()
                );
                localidades.add(localidad);
        }
        return localidades;
    }

    /**
     * Metodo para editar los atributos de un evento a partir de el DTO
     *
     * @param editarEventoDTO
     * @return evento editado
     * @throws EventoException
     */
    @Override
    public void editarEvento(String id,EditarEventoDTO editarEventoDTO) throws EventoException {

        Optional<Evento> optionalEvento = obtenerEventoPorId(id);

        if (optionalEvento.isEmpty()) {
            throw new EventoException("No existe este evento");
        }

        Evento eventoModificado = optionalEvento.get();
        eventoModificado.setImagenPortada(editarEventoDTO.imagenPortada());
        eventoModificado.setNombre(editarEventoDTO.nombre());
        eventoModificado.setDescripcion(editarEventoDTO.descripcion());
        eventoModificado.setDireccion(editarEventoDTO.direccion());
        eventoModificado.setImagenLocalidad(editarEventoDTO.imagenLocalidad());
        eventoModificado.setTipo(editarEventoDTO.tipoEvento());
        eventoModificado.setEstado(editarEventoDTO.estadoEvento());
        eventoModificado.setFecha(editarEventoDTO.fecha());
        eventoModificado.setCiudad(editarEventoDTO.ciudad());

        List<Localidad> localidadesActualizadas = modificarLocalidades(eventoModificado.getLocalidades(),editarEventoDTO.listaLocalidades());
        eventoModificado.setLocalidades(localidadesActualizadas);

        eventoRepo.save(eventoModificado);
    }

    /**
     * Metodo para modificar la lista de localidades creadas para un evento
     * @param localidadesActuales
     * @param listaLocalidadesDTO
     * @return lista de localidades modificadas
     */
    private List<Localidad> modificarLocalidades(List<Localidad> localidadesActuales ,List<CrearLocalidadDTO> listaLocalidadesDTO) throws EventoException {

        List<Localidad> localidadesActualizadas = new ArrayList<>(localidadesActuales);

       if(!localidadesActuales.isEmpty()) {
           for (CrearLocalidadDTO crearLocalidadDTO : listaLocalidadesDTO) {
               for (Localidad localidad : localidadesActuales) {
                   if (localidad.getNombre().equals(crearLocalidadDTO.nombre())) {
                       localidad.setNombre(crearLocalidadDTO.nombre());
                       localidad.setCapacidadMaxima(crearLocalidadDTO.capacidadMaxima());
                       localidad.setPrecio(crearLocalidadDTO.precio());
                   }
               }
           }
       }else{
           throw new EventoException("La localidad que intentas editar no existe");
       }
        return localidadesActualizadas;
    }

    /**
     * Metodo para eliminar un evento cambiando su estado a Eliminado
     * @param id
     * @return id del evento eliminado
     * @throws EventoException
     */
    @Override
    public void eliminarEvento(String id) throws EventoException {

        Optional<Evento> optionalEvento = obtenerEventoPorId(id);

        if(optionalEvento.isEmpty()){
            throw new EventoException("No se encontro el evento");
        }

        Evento evento = optionalEvento.get();
        evento.setEstado(EstadoEvento.INACTIVO);

        eventoRepo.save(evento);
    }

    @Override
    public Evento obtenerInformacionEvento(String id) throws EventoException {

        Optional<Evento> optionalEvento = obtenerEventoPorId(id);

        if(optionalEvento.isEmpty()){
            throw new EventoException("No se encontro el evento con el id: " +id);
        }

        return optionalEvento.get();
    }

    @Override
    public List<ItemEventoDTO> listarEventos() {

        List<Evento> eventos = eventoRepo.findAll();

        List<ItemEventoDTO> itemEventoDTO = new ArrayList<>();

        for (Evento evento : eventos) {
            itemEventoDTO.add(new ItemEventoDTO(
                    evento.getId(),
                    evento.getImagenPortada(),
                    evento.getNombre(),
                    evento.getTipo(),
                    evento.getFecha(),
                    evento.getDireccion(),
                    evento.getCiudad()
            ));
        }
        return itemEventoDTO;
    }

    @Override
    public Page<ItemEventoDTO> getEventosInactivos(Pageable pageable){
        Page<Evento> eventos = eventoRepo.getEventosByEstado(EstadoEvento.INACTIVO, pageable);

        // Transformar cada Evento en un ItemEventoDTO

        return eventos.map(evento -> new ItemEventoDTO(
                evento.getId(),
                evento.getImagenPortada(),
                evento.getNombre(),
                evento.getTipo(),
                evento.getFecha(),
                evento.getDireccion(),
                evento.getCiudad()
        ));
    }

    /**
     *  Metodo para obtener solamente los eventos que se encuentran activo ahora mismo para el index de los usuarios
     * @return
     */
    @Override
    public Page<ItemEventoDTO> getEventoActivos(Pageable pageable){
        Page<Evento> eventos = eventoRepo.getEventosByEstado(EstadoEvento.ACTIVO, pageable);

        // Transformar cada Evento en un ItemEventoDTO

        return eventos.map(evento -> new ItemEventoDTO(
                evento.getId(),
                evento.getImagenPortada(),
                evento.getNombre(),
                evento.getTipo(),
                evento.getFecha(),
                evento.getDireccion(),
                evento.getCiudad()
        ));
    }

    @Override
    public List<EventoFiltradoDTO> filtrarEventos(FiltroEventoDTO filtroEventoDTO) {
        // Inicializa la lista para almacenar los eventos
        List<Evento> eventos;

        // Realiza la consulta en el repositorio según los filtros proporcionados
        if (filtroEventoDTO.nombre() != null || filtroEventoDTO.tipo() != null || filtroEventoDTO.ciudad() != null || filtroEventoDTO.fecha() != null) {
            eventos = eventoRepo.filtrarEventos(
                    filtroEventoDTO.nombre(),
                    filtroEventoDTO.tipo() != null ? filtroEventoDTO.tipo().name() : null, // Solo pasa el tipo si no es nulo
                    filtroEventoDTO.ciudad(),
                    filtroEventoDTO.fecha()
            );
        } else {
            // Si todos los filtros son nulos, devuelve todos los eventos (o lanza una excepción según tu lógica)
            eventos = eventoRepo.findAll(); // Método que deberías tener en tu repositorio para obtener todos los eventos
        }

        // Verificamos si no hay eventos
        if (eventos.isEmpty()) {
            throw new EventoException("No se encontraron eventos para los filtros proporcionados.");
        }

        // Convertimos los eventos a DTOs usando un for
        List<EventoFiltradoDTO> eventoFiltradoDTOs = new ArrayList<>();

        for (Evento evento : eventos) {
            EventoFiltradoDTO dto = convertirAEventoFiltradoDTO(evento);
            eventoFiltradoDTOs.add(dto);
        }

        return eventoFiltradoDTOs;
    }

    // Metodo privado para convertir un Evento en EventoFiltradoDTO
    private EventoFiltradoDTO convertirAEventoFiltradoDTO(Evento evento) {
        return new EventoFiltradoDTO(
                evento.getImagenPortada(),
                evento.getNombre(),
                evento.getDireccion(),
                evento.getCiudad(),
                evento.getFecha(),
                evento.getTipo(),
                evento.getLocalidades()
        );
    }


//    public Localidad obtenerLocalidadPorNombre(String nombre) throws EventoException {
//        Optional<Localidad> optionalLocalidad = eventoRepository.findFirstByLocalidadesNombre(nombre);
//
//        if (optionalLocalidad.isEmpty()) {
//            throw new EventoException("No se encontró la localidad con el nombre: " + nombre);
//        }
//
//        Localidad localidad = optionalLocalidad.get();
//        if (localidad.getPrecio() == null) {
//            throw new EventoException("La localidad " + nombre + " no tiene un precio definido.");
//        }
//
//        return localidad;
//    }



}
