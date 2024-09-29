package co.edu.uniquindio.unieventos.service.implement;

import co.edu.uniquindio.unieventos.dto.evento.*;
import co.edu.uniquindio.unieventos.exceptions.EventoException;
import co.edu.uniquindio.unieventos.model.documents.Evento;
import co.edu.uniquindio.unieventos.model.enums.EstadoEvento;
import co.edu.uniquindio.unieventos.model.enums.TipoEvento;
import co.edu.uniquindio.unieventos.model.vo.Localidad;
import co.edu.uniquindio.unieventos.repository.EventoRepository;
import co.edu.uniquindio.unieventos.service.service.EventoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class EventoServiceImp implements EventoService {

    private final EventoRepository eventoRepo;

    //Metodo para obtener un evento mediante el id
    private Optional<Evento> obtenerEventoPorId(String idEvento) throws EventoException {
        try {
            return eventoRepo.findById(idEvento);
        } catch (Exception e) {
            throw new EventoException("Error al obtener el evento con ID: " + idEvento);
        }
    }

    @Override
    public String crearEvento(CrearEventoDTO crearEventoDTO) throws EventoException {
        // Crear una nueva instancia de Evento
        Evento nuevoEvento = new Evento();
        nuevoEvento.setImagenPortada(crearEventoDTO.imagenPortada());
        nuevoEvento.setNombre(crearEventoDTO.nombre());
        nuevoEvento.setDescripcion(crearEventoDTO.descripcion());
        nuevoEvento.setDireccion(crearEventoDTO.direccion());
        nuevoEvento.setImagenesLocalidades(crearEventoDTO.imagenesLocalidades());
        nuevoEvento.setTipo(crearEventoDTO.tipoEvento());
        nuevoEvento.setEstado(crearEventoDTO.estadoEvento());
        nuevoEvento.setFecha(crearEventoDTO.fecha());
        nuevoEvento.setCiudad(crearEventoDTO.ciudad());

        // Crear localidades
        List<Localidad> localidades = crearLocalidades(crearEventoDTO.listaLocalidades());
        nuevoEvento.setLocalidades(localidades);

        // Guardar el nuevo evento en la base de datos
        Evento eventoGuardado = eventoRepo.save(nuevoEvento);

        // Retornar algún identificador del evento o mensaje de éxito
        return "Evento creado con éxito. ID: " + eventoGuardado.getId();
    }

    /**
     * Metodo para crear localidades
     *
     * @param listaLocalidadesDTO
     * @return lista de localidades
     */
    @Override
    public List<Localidad> crearLocalidades(List<LocalidadDTO> listaLocalidadesDTO) {
        // Inicializar la lista con la capacidad correcta
        List<Localidad> localidades = new ArrayList<>(listaLocalidadesDTO.size());
        // Iterar sobre cada LocalidadDTO y crear una nueva Localidad
        for (LocalidadDTO localidadDTO : listaLocalidadesDTO) {
            Localidad localidad = new Localidad(
                    localidadDTO.nombre(),
                    localidadDTO.capacidadMaxima(),
                    localidadDTO.precio()
            );
            // Agregar la localidad a la lista
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
    public String editarEvento(EditarEventoDTO editarEventoDTO) throws EventoException {

        Optional<Evento> optionalEvento = obtenerEventoPorId(editarEventoDTO.id());

        if (optionalEvento.isEmpty()) {
            throw new EventoException("No existe un evento con el id dado");
        }

        Evento eventoModificado = optionalEvento.get();
        eventoModificado.setImagenPortada(editarEventoDTO.imagenPortada());
        eventoModificado.setNombre(editarEventoDTO.nombre());
        eventoModificado.setDescripcion(editarEventoDTO.descripcion());
        eventoModificado.setDireccion(editarEventoDTO.direccion());
        eventoModificado.setImagenesLocalidades(editarEventoDTO.imagenesLocalidades());
        eventoModificado.setTipo(editarEventoDTO.tipoEvento());
        eventoModificado.setEstado(editarEventoDTO.estadoEvento());
        eventoModificado.setFecha(editarEventoDTO.fecha());
        eventoModificado.setCiudad(editarEventoDTO.ciudad());

        List<Localidad> localidadesActualizadas = modificarLocalidades(eventoModificado.getLocalidades(),editarEventoDTO.listaLocalidades());
        eventoModificado.setLocalidades(localidadesActualizadas);

        eventoRepo.save(eventoModificado);
        return eventoModificado.getId();
    }

    /**
     * Metodo para modificar la lista de localidades creadas para un evento
     * @param localidadesActuales
     * @param listaLocalidadesDTO
     * @return lista de localidades modificadas
     */
    @Override
    public List<Localidad> modificarLocalidades(List<Localidad> localidadesActuales ,List<LocalidadDTO> listaLocalidadesDTO) throws EventoException {

        List<Localidad> localidadesActualizadas = new ArrayList<>(localidadesActuales);

       if(!localidadesActuales.isEmpty()) {
           for (LocalidadDTO localidadDTO : listaLocalidadesDTO) {
               for (Localidad localidad : localidadesActuales) {
                   if (localidad.getNombre().equals(localidadDTO.nombre())) {
                       localidad.setNombre(localidadDTO.nombre());
                       localidad.setCapacidadMaxima(localidadDTO.capacidadMaxima());
                       localidad.setPrecio(localidadDTO.precio());
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
    public String eliminarEvento(String id) throws EventoException {

        Optional<Evento> optionalEvento = obtenerEventoPorId(id);

        if(optionalEvento.isEmpty()){
            throw new EventoException("No se encontro el evento con el id: " +id);
        }

        Evento evento = optionalEvento.get();
        evento.setEstado(EstadoEvento.ELIMINADO);

        eventoRepo.save(evento);
        return evento.getId();
    }

    @Override
    public InformacionEventoDTO obtenerInformacionEvento(String id) throws EventoException {

        Optional<Evento> optionalEvento = obtenerEventoPorId(id);

        if(optionalEvento.isEmpty()){
            throw new EventoException("No se encontro el evento con el id: " +id);
        }

        Evento evento = optionalEvento.get();
        return new InformacionEventoDTO(
                evento.getId(),
                evento.getImagenPortada(),
                evento.getNombre(),
                evento.getDescripcion(),
                evento.getDireccion(),
                evento.getImagenesLocalidades(),
                evento.getTipo(),
                evento.getEstado(),
                evento.getFecha(),
                evento.getCiudad(),
                evento.getLocalidades()
        );
    }

    @Override
    public List<ItemEventoDTO> listarEventos() {

        List<Evento> eventos = eventoRepo.findAll();

        List<ItemEventoDTO> itemEventoDTO = new ArrayList<>();

        for (Evento evento : eventos) {
            itemEventoDTO.add(new ItemEventoDTO(
                    evento.getImagenPortada(),
                    evento.getNombre(),
                    evento.getFecha(),
                    evento.getDireccion(),
                    evento.getCiudad()
            ));
        }

        return itemEventoDTO;
    }

    @Override
    public List<Evento> filtrarPorTipo(TipoEvento tipoEvento) {
        return eventoRepo.filtrarPorTipo(tipoEvento);
    }

    @Override
    public List<Evento> filtrarPorFecha(LocalDateTime fecha) {
        return eventoRepo.filtrarPorFecha(fecha);
    }

    @Override
    public List<Evento> filtrarPorCiudad(String ciudad) {
        return eventoRepo.filtrarPorCiudad(ciudad);
    }

    @Override
    public List<Evento> filtrarPorRangoDeFechas(LocalDateTime desde, LocalDateTime hasta) {
        return eventoRepo.filtrarPorRangoDeFechas(desde, hasta);
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
