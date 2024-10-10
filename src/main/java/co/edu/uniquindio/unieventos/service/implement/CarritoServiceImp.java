package co.edu.uniquindio.unieventos.service.implement;

import co.edu.uniquindio.unieventos.dto.carrito.CrearCarritoDTO;
import co.edu.uniquindio.unieventos.dto.carrito.DetalleCarritoDTO;
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
            throw new CarritoException("La lista de items no puede ser vacía.");
        }

        // Obtener el carrito actual del usuario
        Carrito carrito = obtenerCarritoPorUsuario(idUsuario);

        // Convertir los nuevos items de DTO a DetalleCarrito
        List<DetalleCarrito> nuevosItems = convertirItemsDTOAItems(nuevosItemsDTO);

        // Iterar sobre los nuevos ítems y agregarlos o actualizar la cantidad si ya existen
        for (DetalleCarrito nuevoItem : nuevosItems) {
            boolean itemExistente = false;

            // Buscar si el ítem ya está en el carrito
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
    public List<DetalleCarrito> listarProductosEnCarrito(String idUsuario) throws CarritoException {
        // Obtener el carrito actual del usuario
        Carrito carrito = obtenerCarritoPorUsuario(idUsuario);

        // Retornar los ítems del carrito
        return carrito.getItems();
    }

    @Override
    public double calcularTotalCarrito(String idUsuario) throws CarritoException {
        // Obtener el carrito del usuario
        Carrito carrito = obtenerCarritoPorUsuario(idUsuario);

        double total = 0.0;

        // Iterar sobre los items del carrito
        for (DetalleCarrito item : carrito.getItems()) {
            // Obtener el evento asociado al detalle
            ObjectId idEvento = item.getIdEvento();
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
            ObjectId idEvento = item.getIdEvento();
            Evento evento = eventoRepository.findById(idEvento.toString())
                    .orElseThrow(() -> new CarritoException("Evento no encontrado para el ID: " + idEvento));

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
        }

        // Si todas las validaciones pasan, retornar true
        return true;
    }

    @Override
    public List<DetalleCarrito> convertirItemsDTOAItems(List<DetalleCarritoDTO> itemsCarritoDTO) {
        return itemsCarritoDTO.stream()
                .map(dto -> new DetalleCarrito(
                        dto.cantidad(),
                        dto.nombreLocalidad(),
                        new ObjectId(dto.idEvento()),
                        LocalDateTime.now() // Establecemos la fecha de agregación como el momento actual
                ))
                .collect(Collectors.toList());
    }
}