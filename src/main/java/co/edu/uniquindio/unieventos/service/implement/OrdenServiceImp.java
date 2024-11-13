package co.edu.uniquindio.unieventos.service.implement;

import co.edu.uniquindio.unieventos.dto.cuenta.InformacionCuentaDTO;
import co.edu.uniquindio.unieventos.dto.evento.ObtenerEventoDTO;
import co.edu.uniquindio.unieventos.dto.orden.CrearOrdenDTO;
import co.edu.uniquindio.unieventos.dto.orden.DetalleOrdenDTO;
import co.edu.uniquindio.unieventos.dto.orden.EditarOrdenDTO;
import co.edu.uniquindio.unieventos.dto.orden.MostrarOrdenDTO;
import co.edu.uniquindio.unieventos.exceptions.*;
import co.edu.uniquindio.unieventos.model.documents.*;
import co.edu.uniquindio.unieventos.model.vo.DetalleCarrito;
import co.edu.uniquindio.unieventos.model.vo.DetalleOrden;
import co.edu.uniquindio.unieventos.model.vo.Localidad;
import co.edu.uniquindio.unieventos.model.vo.Pago;
import co.edu.uniquindio.unieventos.repository.CarritoRepository;
import co.edu.uniquindio.unieventos.repository.EventoRepository;
import co.edu.uniquindio.unieventos.repository.OrdenRepository;
import co.edu.uniquindio.unieventos.service.service.*;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class OrdenServiceImp implements OrdenService {

    private final OrdenRepository ordenRepository;
    private final EventoService eventoService;
    private final CarritoService carritoService;
    private final CarritoRepository carritoRepository;
    private final CuentaService cuentaService;
    private final CuponService cuponService;
    private final EventoRepository eventoRepository;

    @Override
    public void crearOrdenDesdeCarrito(String idCliente, String codigoCupon, String codigoPasarela) throws OrdenException, CarritoException, CuponException {

        // Obtener el carrito del cliente, utilizando Optional
        Carrito carrito = carritoRepository.findByIdUsuario(idCliente)
                .orElseThrow(() -> new CarritoException("No se encontró el carrito para el usuario con ID: " + idCliente));

        // Verificar que el carrito tenga ítems
        if (carrito.getItems().isEmpty()) {
            throw new OrdenException("El carrito debe tener al menos un detalle.");
        }

        // Convertir los detalles del carrito a detalles de la orden
        List<DetalleOrdenDTO> detallesOrdenDTO = convertirCarritoADetalleOrdenDTO(carrito.getItems());

        // Obtener el evento y la fecha del evento (en este caso, tomamos el primer ítem)
        DetalleOrdenDTO detalleOrdenDTO = detallesOrdenDTO.get(0);
        Evento evento = eventoService.obtenerInformacionEvento(detalleOrdenDTO.idEvento());

        if (evento == null) {
            throw new EventoException("No se encontró el evento con el ID: " + detalleOrdenDTO.idEvento());
        }

        // Verificar si la compra se realiza al menos dos días antes del evento
        LocalDate fechaEvento = evento.getFecha(); // Fecha del evento
        LocalDate fechaLimiteCompra = fechaEvento.minusDays(2); // Fecha límite para comprar entradas

        if (LocalDate.now().isAfter(fechaLimiteCompra)) {
            throw new OrdenException("Las entradas solo pueden ser compradas hasta dos días antes del evento.");
        }

        // Calcular el total de la orden
        double totalOrden = calcularTotalOrden(detallesOrdenDTO, codigoCupon, LocalDateTime.now());

        // Crear el DTO de la orden usando los datos del usuario
        CrearOrdenDTO ordenDTO = new CrearOrdenDTO(
                idCliente,
                codigoCupon,  // El cupón ingresado por el usuario
                codigoPasarela,  // El código de pasarela ingresado por el usuario
                LocalDate.now(),
                detallesOrdenDTO
        );

        // Llamar al método que crea la orden
        crearOrden(ordenDTO, totalOrden);
    }

    // Método para reducir las entradas disponibles en las localidades
    private void reducirEntradasEnLocalidades(List<DetalleOrdenDTO> detallesOrdenDTO) throws OrdenException {
        for (DetalleOrdenDTO detalle : detallesOrdenDTO) {
            // Obtener el evento
            Evento evento = eventoService.obtenerInformacionEvento(detalle.idEvento());

            // Encontrar la localidad dentro del evento
            Localidad localidad = evento.getLocalidades().stream()
                    .filter(loc -> loc.getNombre().equals(detalle.nombreLocalidad()))
                    .findFirst()
                    .orElseThrow(() -> new EventoException("No se encontró la localidad: " + detalle.nombreLocalidad()));

            // Verificar si hay suficientes entradas en la localidad
            if (localidad.getCapacidadMaxima() < detalle.cantidad()) {
                throw new OrdenException("No hay suficientes entradas disponibles para la localidad: " + detalle.nombreLocalidad());
            }

            // Restar las entradas
            localidad.setCapacidadMaxima(localidad.getCapacidadMaxima() - detalle.cantidad());

            // Guardar el evento actualizado
            eventoRepository.save(evento);
        }
    }



    // Convierte los ítems del carrito a DetalleOrdenDTO
    private List<DetalleOrdenDTO> convertirCarritoADetalleOrdenDTO(List<DetalleCarrito> itemsCarrito) {
        return itemsCarrito.stream().map(item -> {
            // Obtener el evento
            Evento evento = eventoService.obtenerInformacionEvento(item.getIdEvento().toString());
            if (evento == null) {
                throw new EventoException("No se encontró el evento con el ID: " + item.getIdEvento());
            }

            // Buscar la localidad por nombre
            Localidad localidad = evento.getLocalidades().stream()
                    .filter(loc -> loc.getNombre().equals(item.getNombreLocalidad()))
                    .findFirst()
                    .orElseThrow(() -> new EventoException("No se encontró la localidad: " + item.getNombreLocalidad()));

            // Retornar DetalleOrdenDTO con el precio de la localidad
            return new DetalleOrdenDTO(
                    item.getIdEvento().toString(),
                    item.getNombreLocalidad(),
                    item.getCantidad(),
                    localidad.getPrecio() // Aquí obtenemos el precio de la localidad
            );
        }).collect(Collectors.toList());
    }

    @Override
    public void crearOrden(CrearOrdenDTO ordenDTO, double totalOrden) throws OrdenException {

        if (ordenDTO.detalleOrden().isEmpty()) {
            throw new OrdenException("La orden debe tener al menos un detalle.");
        }

        // Crear la instancia de Orden usando el DTO
        Orden orden = new Orden();
        orden.setIdCliente(new ObjectId(ordenDTO.idCliente()));
        orden.setCodigoCupon(ordenDTO.codigoCupon() != null ? (ordenDTO.codigoCupon()) : null);
        orden.setFecha(LocalDate.now());
        orden.setCodigoPasarela(ordenDTO.codigoPasarela());

        // Convertir DetalleOrdenDTO a DetalleOrden y asignarlos a la orden
        List<DetalleOrden> detallesOrden = convertirDetalleDTOADetalle(ordenDTO.detalleOrden());
        orden.setDetalle(detallesOrden);

        // Asignar el total calculado a la orden
        orden.setTotal(totalOrden);

        // Guardar la orden en el repositorio
        ordenRepository.save(orden);
    }


    // Convierte los DetalleOrdenDTO a DetalleOrden
    private List<DetalleOrden> convertirDetalleDTOADetalle(List<DetalleOrdenDTO> detalleOrdenDTO) {
        return detalleOrdenDTO.stream()
                .map(dto -> new DetalleOrden(
                        new ObjectId(dto.idEvento()),
                        dto.nombreLocalidad(),
                        dto.cantidad(),
                        dto.precio()
                        ))
                .collect(Collectors.toList());
    }

    @Override
    public MostrarOrdenDTO mostrarOrden(String idOrden) throws OrdenException, CuentaException {
        // Obtener la orden de la base de datos
        Orden orden = obtenerOrdenPorId(idOrden);

        // Obtener información del usuario
        InformacionCuentaDTO usuario = cuentaService.obtenerInformacionCuenta(orden.getIdCliente().toString());

        // Obtener información del evento
        DetalleOrden detalle = orden.getDetalle().get(0); // Asumiendo que hay al menos un detalle en la orden
        Evento evento = eventoService.obtenerInformacionEvento(detalle.getIdEvento().toString());


        // Crear el DTO MostrarOrdenDTO
        return new MostrarOrdenDTO(
                usuario.id(),
                usuario.nombre(),
                evento.getId(),
                evento.getNombre(),
                orden.getFecha(),
                evento.getFecha(),
                evento.getTipo(),
                detalle.getNombreLocalidad(),
                detalle.getPrecio(),
                detalle.getCantidad(),
                orden.getCodigoCupon() != null ? orden.getCodigoCupon() : null,
                orden.getTotal()
        );
    }

    private double calcularTotalOrden(List<DetalleOrdenDTO> detallesOrdenDTO, String codigoCupon, LocalDateTime fechaCompra) throws CuponException {
        // Calcular el total de los detalles
        double total = detallesOrdenDTO.stream()
                .mapToDouble(detalle -> detalle.precio() * detalle.cantidad())
                .sum();

        // Aplicar el cupón si existe
        if (codigoCupon != null && !codigoCupon.isEmpty()) {
            // Llama a cuponService para validar y aplicar el cupón
            Cupon cupon = cuponService.aplicarCupon(codigoCupon, fechaCompra);

            // Restar el descuento del cupón al total
            double descuento = total * (cupon.getDescuento() / 100.0);
            total -= descuento;
        }

        return total;
    }

    // Obtener una orden por ID
    @Override
    public Orden obtenerOrdenPorId(String id) throws OrdenException {
        return ordenRepository.findById(id).orElseThrow(() -> new OrdenException("Orden no encontrada"));
    }

    // Actualizar una orden existente
    public void actualizarOrden(String id, EditarOrdenDTO ordenDTO) throws OrdenException {

        //Buscamos la orden del usuario que se quiere actualizar
        Orden orden = obtenerOrdenPorId(id);

        //Si no se encontró la cuenta del usuario, lanzamos una excepción
        if(orden == null){
            throw new OrdenException("No existe una orden con el id dado");
        }

        orden.setCodigoCupon(ordenDTO.idCupon());
        orden.setTotal(ordenDTO.total());

        ordenRepository.save(orden);
    }

    // Eliminar una orden
    @Override
    public void eliminarOrden(String id) throws OrdenException {

        //Buscamos la cuenta del usuario que se quiere eliminar
        Orden orden = obtenerOrdenPorId(id);

        //Si no se encontró la cuenta, lanzamos una excepción
        if(orden == null){
            throw new OrdenException("No se encontró la orden con el id "+id);
        }

        ordenRepository.delete(orden);
    }

    @Override
    public Preference realizarPago(String idOrden) throws Exception {

        // Obtener la orden guardada en la base de datos y los ítems de la orden
        Orden ordenGuardada = obtenerOrdenPorId(idOrden);
        List<PreferenceItemRequest> itemsPasarela = new ArrayList<>();

        // Recorrer los items de la orden y crea los ítems de la pasarela
        for(DetalleOrden item : ordenGuardada.getDetalle()){

            // Obtener el evento y la localidad del ítem
            Evento evento = eventoService.obtenerInformacionEvento(item.getIdEvento().toString());

            // Usar directamente el precio y el nombre de localidad desde el detalle de la orden
            Float precioLocalidad = item.getPrecio();  // Aquí asumimos que el precio está en el detalle
            String nombreLocalidad = item.getNombreLocalidad();

            // Crear el item de la pasarela
            PreferenceItemRequest itemRequest =
                    PreferenceItemRequest.builder()
                            .id(evento.getId())
                            .title(evento.getNombre())
                            .pictureUrl(evento.getImagenPortada())
                            .categoryId(evento.getTipo().name())
                            .quantity(item.getCantidad())
                            .currencyId("COP")
                            .unitPrice(BigDecimal.valueOf(precioLocalidad))
                            .build();


            itemsPasarela.add(itemRequest);
        }


        // Configurar las credenciales de MercadoPago
        MercadoPagoConfig.setAccessToken("APP_USR-4123262776745313-100812-965fcccac0cb0fd91a2368b9d8c3689f-2027268786");


        // Configurar las urls de retorno de la pasarela (Frontend)
        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success("URL PAGO EXITOSO")
                .failure("URL PAGO FALLIDO")
                .pending("URL PAGO PENDIENTE")
                .build();


        // Construir la preferencia de la pasarela con los ítems, metadatos y urls de retorno
        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .backUrls(backUrls)
                .items(itemsPasarela)
                .metadata(Map.of("id_orden", ordenGuardada.getId()))
                .notificationUrl("https://de93-2800-e2-9880-a18-6405-c7c4-bed9-4dde.ngrok-free.app")
                .build();


        // Crear la preferencia en la pasarela de MercadoPago
        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(preferenceRequest);


        // Guardar el código de la pasarela en la orden
        ordenGuardada.setCodigoPasarela( preference.getId() );
        ordenRepository.save(ordenGuardada);


        return preference;
    }

    @Override
    public void recibirNotificacionMercadoPago(Map<String, Object> request) {
        try {

            // Obtener el tipo de notificación
            Object tipo = request.get("type");


            // Si la notificación es de un pago entonces obtener el pago y la orden asociada
            if ("payment".equals(tipo)) {


                // Capturamos el JSON que viene en el request y lo convertimos a un String
                String input = request.get("data").toString();


                // Extraemos los números de la cadena, es decir, el id del pago
                String idPago = input.replaceAll("\\D+", "");


                // Se crea el cliente de MercadoPago y se obtiene el pago con el id
                PaymentClient client = new PaymentClient();
                Payment payment = client.get( Long.parseLong(idPago) );


                // Obtener el id de la orden asociada al pago que viene en los metadatos
                String idOrden = payment.getMetadata().get("id_orden").toString();


                // Se obtiene la orden guardada en la base de datos y se le asigna el pago
                Orden orden = obtenerOrdenPorId(idOrden);
                Pago pago = crearPago(payment);
                orden.setPago(pago);
                ordenRepository.save(orden);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private Pago crearPago(Payment payment) {
        Pago pago = new Pago();
        pago.setId(payment.getId().toString());
        pago.setFecha( payment.getDateCreated().toLocalDateTime() );
        pago.setEstado(payment.getStatus());
        pago.setDetalleEstado(payment.getStatusDetail());
        pago.setTipoPago(payment.getPaymentTypeId());
        pago.setMoneda(payment.getCurrencyId());
        pago.setCodigoAutorizacion(payment.getAuthorizationCode());
        pago.setValorTransaccion(payment.getTransactionAmount().floatValue());
        return pago;
    }
}