package co.edu.uniquindio.unieventos.service.implement;

import co.edu.uniquindio.unieventos.dto.orden.CrearOrdenDTO;
import co.edu.uniquindio.unieventos.dto.orden.DetalleOrdenDTO;
import co.edu.uniquindio.unieventos.dto.orden.EditarOrdenDTO;
import co.edu.uniquindio.unieventos.exceptions.OrdenException;
import co.edu.uniquindio.unieventos.model.documents.Evento;
import co.edu.uniquindio.unieventos.model.documents.Orden;
import co.edu.uniquindio.unieventos.model.vo.DetalleOrden;
import co.edu.uniquindio.unieventos.model.vo.Localidad;
import co.edu.uniquindio.unieventos.model.vo.Pago;
import co.edu.uniquindio.unieventos.repository.OrdenRepository;
import co.edu.uniquindio.unieventos.service.service.EventoService;
import co.edu.uniquindio.unieventos.service.service.OrdenService;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdenServiceImp implements OrdenService {

    private final OrdenRepository ordenRepository;
    private final EventoService eventoServicio;

    @Override
    public String crearOrden(CrearOrdenDTO ordenDTO) throws OrdenException {

        if (ordenDTO.detalleOrden().isEmpty()) {
            throw new OrdenException("La orden debe tener al menos un detalle.");
        }

        // Crear la instancia de Orden usando el DTO
        Orden orden = new Orden();
        orden.setIdCliente(new ObjectId(ordenDTO.idCliente()));
        orden.setIdCupon(ordenDTO.idCupon() != null ? new ObjectId(ordenDTO.idCupon()) : null);
        orden.setFecha(LocalDateTime.now());
        orden.setCodigoPasarela(ordenDTO.codigoPasarela());
        orden.setTotal(ordenDTO.total());

        // Convertir DetalleOrdenDTO a DetalleOrden y asignarlos a la orden
        List<DetalleOrden> detallesOrden = convertirDetalleDTOADetalle(ordenDTO.detalleOrden());


        orden.setDetalle(detallesOrden);

        // Guardar la orden
        Orden ordenGuardada = ordenRepository.save(orden);

        return ordenGuardada.getId();
    }

    private List<DetalleOrden> convertirDetalleDTOADetalle(List<DetalleOrdenDTO> detalleOrdenDTO) {
        return detalleOrdenDTO.stream()
                .map(dto -> new DetalleOrden(
                        new ObjectId(dto.idEvento()),
                        dto.precio(),
                        dto.nombreLocalidad(),
                        dto.cantidad()
                ))
                .collect(Collectors.toList());
    }

    // Obtener una orden por ID
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

        orden.setIdCupon(new ObjectId(ordenDTO.idCupon()));
        orden.setTotal(ordenDTO.total());

        ordenRepository.save(orden);
    }

    // Eliminar una orden
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
            Evento evento = eventoServicio.obtenerInformacionEvento(item.getIdEvento().toString());
            Localidad localidad = eventoServicio.obtenerLocalidadPorNombre(item.getNombreLocalidad());


            // Crear el item de la pasarela
            PreferenceItemRequest itemRequest =
                    PreferenceItemRequest.builder()
                            .id(evento.getId())
                            .title(evento.getNombre())
                            .pictureUrl(evento.getImagenPortada())
                            .categoryId(evento.getTipo().name())
                            .quantity(item.getCantidad())
                            .currencyId("COP")
                            .unitPrice(BigDecimal.valueOf(localidad.getPrecio()))
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