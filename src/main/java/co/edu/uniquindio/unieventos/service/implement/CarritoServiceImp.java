package co.edu.uniquindio.unieventos.service.implement;

import co.edu.uniquindio.unieventos.dto.carrito.CrearCarritoDTO;
import co.edu.uniquindio.unieventos.dto.carrito.DetalleCarritoDTO;
import co.edu.uniquindio.unieventos.dto.carrito.InformacionEventoCarritoDTO;
import co.edu.uniquindio.unieventos.exceptions.CarritoException;
import co.edu.uniquindio.unieventos.model.documents.Carrito;
import co.edu.uniquindio.unieventos.model.documents.Evento;
import co.edu.uniquindio.unieventos.model.vo.DetalleCarrito;
import co.edu.uniquindio.unieventos.model.vo.Localidad;
import co.edu.uniquindio.unieventos.repository.CarritoRepository;
import co.edu.uniquindio.unieventos.repository.EventoRepository;
import co.edu.uniquindio.unieventos.service.service.CarritoService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor

public class CarritoServiceImp implements CarritoService {

    private final CarritoRepository carritoRepository;
    private final EventoRepository eventoRepository;

    @Override
    public void crearCarrito(CrearCarritoDTO carritoDTO) throws CarritoException {
        // Validar el idUsuario
        if (carritoDTO.idUsuario() == null) {
            throw new CarritoException("El ID del usuario no puede ser nulo.");
        }

        // Crear la instancia de Carrito usando el DTO
        Carrito carrito = new Carrito();
        carrito.setIdUsuario(carritoDTO.idUsuario());
        carrito.setItems(new ArrayList<>());  // Inicialmente el carrito estará vacío
        carrito.setFecha(carritoDTO.fecha());

        // Guardar el carrito en la base de datos
        Carrito carritoGuardado = carritoRepository.save(carrito);
    }

    @Override
    public Carrito obtenerCarritoPorUsuario(String idUsuario) throws CarritoException {
        return carritoRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new CarritoException("No se encontró un carrito para este usuario."));
    }

    @Override
    public Carrito agregarItemsAlCarrito(String idUsuario, List<DetalleCarritoDTO> nuevosItemsDTO) throws CarritoException {

        if (nuevosItemsDTO == null || nuevosItemsDTO.isEmpty()) {
            throw new CarritoException("La lista de ítems no puede estar vacía.");
        }

        // Obtener el carrito actual del usuario
        Carrito carrito = obtenerCarritoPorUsuario(idUsuario);

        // Convertir los nuevos items de DTO a DetalleCarrito
        List<DetalleCarrito> nuevosItems = convertirItemsDTOAItems(nuevosItemsDTO);

        // Añadir temporalmente los nuevos ítems al carrito para validar disponibilidad
        List<DetalleCarrito> itemsTemporales = new ArrayList<>(carrito.getItems());
        itemsTemporales.addAll(nuevosItems);

        // Crear un carrito temporal con los nuevos ítems para validar la disponibilidad
        Carrito carritoTemporal = new Carrito(idUsuario, itemsTemporales, LocalDateTime.now());

        // Validar la disponibilidad de entradas antes de agregar los ítems al carrito
        boolean disponibilidad = validarDisponibilidadEntradas(idUsuario);  // Método que ya tienes implementado

        // Si no hay suficiente disponibilidad, lanzar una excepción
        if (!disponibilidad) {
            throw new CarritoException("No hay suficientes entradas disponibles para uno o más ítems en el carrito.");
        }

        // Agregar los nuevos ítems al carrito existente
        for (DetalleCarrito nuevoItem : nuevosItems) {
            // Obtener el evento para validar las localidades, ahora utilizando ObjectId directamente
            Evento evento = eventoRepository.findById(nuevoItem.getIdEvento())
                    .orElseThrow(() -> new CarritoException("El evento con ID " + nuevoItem.getIdEvento() + " no existe."));

            // Validar que la localidad exista en el evento
            boolean localidadValida = evento.getLocalidades().stream()
                    .anyMatch(localidad -> localidad.getNombre().equals(nuevoItem.getNombreLocalidad()));

            if (!localidadValida) {
                throw new CarritoException("La localidad " + nuevoItem.getNombreLocalidad() + " no es válida para el evento " + evento.getNombre());
            }

            // Lógica para agregar o actualizar los ítems en el carrito
            boolean itemExistente = false;

            for (DetalleCarrito itemActual : carrito.getItems()) {
                if (itemActual.getNombreLocalidad().equals(nuevoItem.getNombreLocalidad()) &&
                        itemActual.getIdEvento().equals(nuevoItem.getIdEvento())) {

                    // Si el ítem ya está, actualizar su cantidad
                    itemActual.setCantidad(itemActual.getCantidad() + nuevoItem.getCantidad());
                    itemExistente = true;
                    break;
                }
            }

            // Si el ítem no existe en el carrito, agregarlo
            if (!itemExistente) {
                carrito.getItems().add(nuevoItem);
            }
        }

        // Guardar y retornar el carrito actualizado
        return carritoRepository.save(carrito);
    }

    @Override
    public Carrito eliminarItemDelCarrito(String idUsuario, String nombreLocalidad) throws CarritoException {
        Carrito carrito = obtenerCarritoPorUsuario(idUsuario);

        // RemoveIf elimina el ítem de manera más directa
        carrito.getItems().removeIf(item -> item.getNombreLocalidad().equals(nombreLocalidad));

        // Guardar el carrito actualizado
        return carritoRepository.save(carrito);
    }

    @Override
    public Carrito vaciarCarrito(String idUsuario) throws CarritoException {
        // Obtener el carrito del usuario
        Carrito carrito = obtenerCarritoPorUsuario(idUsuario);

        // Vaciar la lista de ítems
        carrito.setItems(new ArrayList<>());

        // Guardar el carrito vacío
        return carritoRepository.save(carrito);
    }

    @Override
    public List<InformacionEventoCarritoDTO> listarProductosEnCarrito(String idUsuario) throws CarritoException {
        // Obtener el carrito actual del usuario
        Carrito carrito = obtenerCarritoPorUsuario(idUsuario);

        // Lista para almacenar los detalles del carrito junto con la información del evento
        List<InformacionEventoCarritoDTO> detallesConEventos = new ArrayList<>();

        // Iterar sobre los ítems del carrito
        for (DetalleCarrito item : carrito.getItems()) {
            // Obtener el evento asociado al ítem
            Evento evento = eventoRepository.findById(String.valueOf(item.getIdEvento()))
                    .orElseThrow(() -> new CarritoException("El evento con ID " + item.getIdEvento() + " no existe."));

            // Convertir DetalleCarrito en DetalleCarritoDTO
            DetalleCarritoDTO detalle = new DetalleCarritoDTO(
                    item.getIdEvento(),
                    item.getCantidad(),
                    item.getNombreLocalidad()
            );

            item.getFechaAgregacion().toLocalDate();

            // Crear un DTO con la información del evento
            InformacionEventoCarritoDTO informacionEvento = new InformacionEventoCarritoDTO(
                    detalle,
                    evento.getImagenPortada(),  // Asegúrate de que esta imagen sea del tipo correcto
                    evento.getNombre(),
                    evento.getDireccion(),
                    evento.getFecha()
            );

            // Agregarlo a la lista
            detallesConEventos.add(informacionEvento);
        }

        // Retornar la lista de ítems con la información de sus eventos
        return detallesConEventos;
    }


    @Override
    public double calcularTotalCarrito(String idUsuario) throws CarritoException {
        // Obtener el carrito del usuario
        Carrito carrito = obtenerCarritoPorUsuario(idUsuario);

        double total = 0.0;

        // Iterar sobre los items del carrito
        for (DetalleCarrito item : carrito.getItems()) {
            // Obtener el evento asociado al detalle
            String idEvento = item.getIdEvento();
            Evento evento = eventoRepository.findById(idEvento.toString())
                    .orElseThrow(() -> new CarritoException("Evento no encontrado para el ID: " + idEvento));


            // Buscar la localidad correspondiente en el evento
            Localidad localidad = evento.getLocalidades().stream()
                    .filter(l -> l.getNombre().equals(item.getNombreLocalidad()))
                    .findFirst()
                    .orElseThrow(() -> new CarritoException("Localidad no encontrada: " + item.getNombreLocalidad()));

            // Sumar al total (precio de la localidad * cantidad)
            total += localidad.getPrecio() * item.getCantidad();
        }

        return total;
    }

    @Override
    public boolean validarDisponibilidadEntradas(String idUsuario) throws CarritoException {
        // Obtener el carrito del usuario
        Carrito carrito = obtenerCarritoPorUsuario(idUsuario);

        // Iterar sobre los ítems del carrito
        for (DetalleCarrito item : carrito.getItems()) {
            // Obtener el evento asociado al detalle
            Evento evento = eventoRepository.findById(item.getIdEvento())
                    .orElseThrow(() -> new CarritoException("Evento no encontrado para el ID: " + item.getIdEvento()));

            // Buscar la localidad correspondiente en el evento
            Localidad localidad = evento.getLocalidades().stream()
                    .filter(l -> l.getNombre().equals(item.getNombreLocalidad()))
                    .findFirst()
                    .orElseThrow(() -> new CarritoException("Localidad no encontrada: " + item.getNombreLocalidad()));

            // Calcular entradas disponibles
            int entradasDisponibles = localidad.getCapacidadMaxima() - localidad.getEntradasVendidas();

            // Validar que haya suficientes entradas disponibles
            if (entradasDisponibles < item.getCantidad()) {
                throw new CarritoException("No hay suficientes entradas disponibles para la localidad: " + item.getNombreLocalidad());
            }
            // Si hay suficientes entradas, sumar la cantidad del carrito a las entradas vendidas
            localidad.setEntradasVendidas(localidad.getEntradasVendidas() + item.getCantidad());
            // Guardar el evento con las entradas actualizadas
            eventoRepository.save(evento);
        }
        // Si todas las validaciones pasan, retornar true
        return true;
    }

    private List<DetalleCarrito> convertirItemsDTOAItems(List<DetalleCarritoDTO> itemsCarritoDTO) {
        return itemsCarritoDTO.stream()
                .map(dto -> new DetalleCarrito(
                        dto.idEvento(),
                        dto.cantidad(),
                        dto.nombreLocalidad(),
                        LocalDateTime.now() // Establecemos la fecha de agregación como el momento actual
                ))
                .collect(Collectors.toList());
    }
}