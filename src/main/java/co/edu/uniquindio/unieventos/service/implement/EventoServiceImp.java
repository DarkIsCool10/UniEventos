package co.edu.uniquindio.unieventos.service.implement;

import co.edu.uniquindio.unieventos.dto.evento.*;
import co.edu.uniquindio.unieventos.exceptions.EventoException;
import co.edu.uniquindio.unieventos.model.documents.Evento;
import co.edu.uniquindio.unieventos.model.enums.EstadoEvento;
import co.edu.uniquindio.unieventos.model.enums.TipoEvento;
import co.edu.uniquindio.unieventos.model.vo.Localidad;
import co.edu.uniquindio.unieventos.repository.EventoRepository;
import co.edu.uniquindio.unieventos.repository.LocalidadRepository;
import co.edu.uniquindio.unieventos.service.service.EventoService;
import co.edu.uniquindio.unieventos.service.service.ImagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class EventoServiceImp implements EventoService {

    private final EventoRepository eventoRepo;
    private final ImagesService imagesService;
    private final LocalidadRepository localidadRepository;

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
            nuevoEvento.setEstado(crearEventoDTO.estadoEvento());
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
    public void editarEvento(EditarEventoDTO editarEventoDTO) throws EventoException {

        Optional<Evento> optionalEvento = obtenerEventoPorId(editarEventoDTO.id());

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
        evento.setEstado(EstadoEvento.ELIMINADO);

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
    public List<EventoFiltradoDTO> filtrarEventos(EventoFiltradoDTO eventoFiltradoDTO) {
        // Realizamos la consulta en el repositorio según los filtros proporcionados
        List<Evento> eventos = eventoRepo.filtrarEventos(
                eventoFiltradoDTO.nombre(),
                eventoFiltradoDTO.tipoEvento() != null ? eventoFiltradoDTO.tipoEvento().name() : null,
                eventoFiltradoDTO.ciudad(),
                eventoFiltradoDTO.fecha()
        );

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

    /*
    @Override
    public List<EventoFiltradoDTO> filtrarPorCiudad(String ciudad) {
        List<Evento> eventos = eventoRepo.filtrarPorCiudad(ciudad);
        if (eventos.isEmpty()) {
            throw new EventoException("No se encontraron eventos para la ciudad: " + ciudad);
        }

        List<EventoFiltradoDTO> eventoFiltradoDTO = new ArrayList<>();

        for(Evento evento : eventos){
            eventoFiltradoDTO.add(new EventoFiltradoDTO(
                    evento.getImagenPortada(),
                    evento.getNombre(),
                    evento.getDireccion(),
                    evento.getCiudad(),
                    evento.getFecha(),
                    evento.getTipo(),
                    evento.getLocalidades()
            ));
        }

        return eventoFiltradoDTO;
    }

    */

    public List<EventoFiltradoDTO> filtrarPorRangoDeFechas(LocalDateTime desde, LocalDateTime hasta) {
        List<Evento> eventos = eventoRepo.filtrarPorRangoDeFechas(desde, hasta);
        if (eventos.isEmpty()) {
            throw new EventoException("No se encontraron eventos en el rango de fechas: " + desde + " a " + hasta);
        }

        List<EventoFiltradoDTO> eventoFiltradoDTO = new ArrayList<>();

        for(Evento evento : eventos){
            eventoFiltradoDTO.add(new EventoFiltradoDTO(
                    evento.getImagenPortada(),
                    evento.getNombre(),
                    evento.getDireccion(),
                    evento.getCiudad(),
                    evento.getFecha(),
                    evento.getTipo(),
                    evento.getLocalidades()
            ));
        }

        return eventoFiltradoDTO;
    }

    public Localidad obtenerLocalidadPorNombre(String nombre) throws EventoException {
        Optional<Localidad> optionalLocalidad = localidadRepository.findById(nombre);

        if (optionalLocalidad.isEmpty()) {
            throw new EventoException("No se encontró la localidad con el id: " + nombre);
        }

        return optionalLocalidad.get();
    }

//    @Override
//    public List<ItemEventoDTO> filtrarEventos(FiltroEventoDTO filtroEventoDTO) {
//
//        LocalDateTime fecha = LocalDateTime.now();
//
//        // Llamamos al metodo del repositorio
//        List<Evento> eventos = eventoRepo.filtrarEventosPorTipoCiudadYFecha(
//                filtroEventoDTO.nombre(),
//                filtroEventoDTO.tipoEvento(),
//                filtroEventoDTO.ciudad(),
//                filtroEventoDTO.fecha()
//        );
//
//        // Convertimos los eventos a DTOs
//        return eventos.stream()
//                .map(this::convertirAItemEventoDTO)
//                .collect(Collectors.toList());
//    }
//
//    private ItemEventoDTO convertirAItemEventoDTO(Evento evento) {
//        return new ItemEventoDTO(
//                evento.getImagenPortada(),
//                evento.getNombre(),
//                evento.getFecha(),
//                evento.getDireccion(),
//                evento.getCiudad()
//        );
//    }

}
