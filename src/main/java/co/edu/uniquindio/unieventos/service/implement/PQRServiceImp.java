package co.edu.uniquindio.unieventos.service.implement;

import co.edu.uniquindio.unieventos.dto.PQR.*;
import co.edu.uniquindio.unieventos.exceptions.PQRException;
import co.edu.uniquindio.unieventos.model.documents.PQR;
import co.edu.uniquindio.unieventos.model.enums.EstadoPQR;
import co.edu.uniquindio.unieventos.repository.PQRRepository;
import co.edu.uniquindio.unieventos.service.service.PQRService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PQRServiceImp implements PQRService {

    private final PQRRepository pqrRepository;

    @Override
    public void crearPQR(CrearPQRDTO pqrDTO) throws PQRException {
        try {
            PQR pqr = new PQR();
            pqr.setIdUsuario(pqrDTO.idUsuario());
            pqr.setFechaCreacion(LocalDateTime.now());
            pqr.setCategoriaPQR(pqrDTO.categoriaPQR());
            pqr.setDescripcion(pqrDTO.descripcion());
            pqr.setRespuesta(null);
            pqr.setFechaRespuesta(null);

            pqrRepository.save(pqr);
        } catch (Exception e) {
            throw new PQRException("Error creando la PQR: " + e.getMessage());
        }
    }

    @Override
    public String eliminarPQR(String id) throws PQRException {
        Optional<PQR> pqrOpt = pqrRepository.findById(id);
        if (pqrOpt.isEmpty()) {
            throw new PQRException("PQR no encontrada");
        }
        pqrRepository.deleteById(id);
        return "PQR eliminada con éxito";
    }

    @Override
    public InformacionPQRDTO obtenerInformacionPQR(String id) throws PQRException {
        Optional<PQR> pqrOpt = pqrRepository.findById(id);
        if (pqrOpt.isEmpty()) {
            throw new PQRException("PQR no encontrada");
        }
        PQR pqr = pqrOpt.get();
        return new InformacionPQRDTO(
                pqr.getId(),
                pqr.getIdUsuario().toString(),
                pqr.getFechaCreacion(),
                pqr.getEstadoPQR(),
                pqr.getCategoriaPQR(),
                pqr.getDescripcion(),
                pqr.getRespuesta(),
                pqr.getFechaRespuesta()
        );
    }

    @Override
    public List<PQR> obtenerPQRsPorUsuario(String idUsuario) throws PQRException {
        try {
            List<PQR> pqrs = pqrRepository.findByIdUsuario(idUsuario);
            if (pqrs.isEmpty()) {
                throw new PQRException("No se encontraron PQRs para este usuario");
            }
            return pqrs;
        } catch (Exception e) {
            throw new PQRException("Error obteniendo las PQRs: " + e.getMessage());
        }
    }

    @Override
    public String responderPQR(ResponderPQRDTO responderPqrDTO) throws PQRException {
        Optional<PQR> pqrOpt = pqrRepository.findById(responderPqrDTO.id());
        if (pqrOpt.isEmpty()) {
            throw new PQRException("PQR no encontrada");
        }

        PQR pqr = pqrOpt.get();
        // Cambiar comparación a enum en vez de String
        if (pqr.getEstadoPQR() == EstadoPQR.CERRADO) {
            throw new PQRException("La PQR ya ha sido resuelta");
        }

        pqr.setRespuesta(responderPqrDTO.respuesta());
        pqr.setFechaRespuesta(LocalDateTime.now());

        pqrRepository.save(pqr);
        return "PQR respondida con éxito";
    }

    @Override
    public List<ItemPQRDTO> listarPQRs() throws PQRException {
        try {
            List<PQR> pqrs = pqrRepository.findAll();
            return pqrs.stream()
                    .map(pqr -> new ItemPQRDTO(
                            pqr.getId(),
                            pqr.getIdUsuario().toString(),
                            pqr.getEstadoPQR(),
                            pqr.getFechaCreacion()
                    )).collect(Collectors.toList());
        } catch (Exception e) {
            throw new PQRException("Error listando las PQRs: " + e.getMessage());
        }
    }
}