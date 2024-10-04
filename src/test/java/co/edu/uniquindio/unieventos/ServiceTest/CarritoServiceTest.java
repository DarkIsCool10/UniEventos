package co.edu.uniquindio.unieventos.ServiceTest;

import co.edu.uniquindio.unieventos.dto.carrito.DetalleCarritoDTO;
import co.edu.uniquindio.unieventos.model.documents.Carrito;
import co.edu.uniquindio.unieventos.model.vo.DetalleCarrito;
import co.edu.uniquindio.unieventos.service.service.CarritoService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CarritoServiceTest {

    @Autowired
    private CarritoService carritoService;

    @Test
    public void agregarItemAlCarritoTest(){

        //Se define el id de la cuenta del usuario a actualizar, este id está en el dataset.js
        String idCuenta = "del nuevo test";

        // ID del evento ya existente
        String idEventoExistente = "66a2c476991cff088eb80aaf"; // ID del evento "Concierto de despedida del 2024"

        // Crear el DTO del nuevo item a agregar al carrito
        DetalleCarritoDTO nuevoItemDTO = new DetalleCarritoDTO(
                2,
                "VIP",
                idEventoExistente,
                LocalDateTime.now()
        );

        // Llamar al método para agregar el item al carrito
        List<DetalleCarritoDTO> nuevosItemsDTO = List.of(nuevoItemDTO);

        assertDoesNotThrow(() -> {
            Carrito carritoActualizado = carritoService.agregarItemsAlCarrito(idCuenta, nuevosItemsDTO);

            // Verificar que el carrito se haya actualizado correctamente
            assertEquals(1, carritoActualizado.getItems().size());
            DetalleCarrito itemAgregado = carritoActualizado.getItems().get(0);
            assertEquals("VIP", itemAgregado.getNombreLocalidad());
            assertEquals(2, itemAgregado.getCantidad());
            assertEquals(new ObjectId(idEventoExistente), itemAgregado.getIdEvento());
        });
    }
}