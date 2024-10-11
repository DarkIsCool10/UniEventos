package co.edu.uniquindio.unieventos.ServiceTest;

import co.edu.uniquindio.unieventos.dto.PQR.CrearPQRDTO;
import co.edu.uniquindio.unieventos.dto.PQR.InformacionPQRDTO;
import co.edu.uniquindio.unieventos.dto.PQR.ResponderPQRDTO;
import co.edu.uniquindio.unieventos.exceptions.PQRException;
import co.edu.uniquindio.unieventos.model.documents.PQR;
import co.edu.uniquindio.unieventos.model.enums.CategoriaPQR;
import co.edu.uniquindio.unieventos.model.enums.EstadoPQR;
import co.edu.uniquindio.unieventos.repository.PQRRepository;
import co.edu.uniquindio.unieventos.service.implement.PQRServiceImp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PQRServiceTest {

    @Autowired
    private PQRRepository pqrRepository;

    @Autowired
    private PQRServiceImp pqrService;

    @Test
    public void crearPQRTest() {
        CrearPQRDTO crearPQRDTO = new CrearPQRDTO(
                "64e3b77d5a64280022b58478", // ID del usuario
                CategoriaPQR.SERVICIO_CLIENTE,
                "Tengo problemas con mi pedido."
        );

        // Verificamos que no se lanza ninguna excepción al crear la PQR
        assertDoesNotThrow(() -> pqrService.crearPQR(crearPQRDTO));
    }

    @Test
    public void eliminarPQRTest() throws PQRException {
        String idPQR = "64e3b77d5a64280022b58479"; // ID de la PQR

        // Verificamos que no se lanza ninguna excepción al eliminar la PQR
        assertDoesNotThrow(() -> pqrService.eliminarPQR(idPQR));
    }

    @Test
    public void obtenerInformacionPQRTest() {
        String idPQR = "64e3b77d5a64280022b58479"; // ID de la PQR

        // Verificamos que no se lanza ninguna excepción al obtener la información de la PQR
        assertDoesNotThrow(() -> {
            var infoPQR = pqrService.obtenerInformacionPQR(idPQR);
            // Aseguramos que el idUsuario sea el correcto
            assertEquals("64e3b77d5a64280022b58478", infoPQR.id());
        });
    }

    @Test
    public void listarPQRsTest() {
        // Verificamos que no se lanza ninguna excepción al listar las PQRs
        assertDoesNotThrow(() -> {
            List<?> listaPQRs = pqrService.listarPQRs();
            // Aseguramos que la lista no es nula
            assertNotNull(listaPQRs);
            // Puedes agregar más aserciones aquí, como verificar el tamaño de la lista si es necesario
        });
    }

    @Test
    public void responderPQRTest() throws PQRException {
        ResponderPQRDTO responderPQRDTO = new ResponderPQRDTO(
                "64e3b77d5a64280022b58479", // ID de la PQR
                "Hemos resuelto tu problema.",
                LocalDateTime.now()
        );

        // Verificamos que no se lanza ninguna excepción al responder la PQR
        assertDoesNotThrow(() -> {
            pqrService.responderPQR(responderPQRDTO);
        });

        // Aseguramos que la PQR se haya respondido correctamente,
        // por ejemplo, verificando el estado de la PQR en el sistema.
        InformacionPQRDTO pqr = pqrService.obtenerInformacionPQR(responderPQRDTO.id());
        assertNotNull(pqr);
        assertEquals("Hemos resuelto tu problema.", pqr.respuesta());
        // Aquí puedes agregar más aserciones según lo que debas verificar.
    }
}