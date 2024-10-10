package co.edu.uniquindio.unieventos.service.implement;

import co.edu.uniquindio.unieventos.dto.PQR.*;
import co.edu.uniquindio.unieventos.exceptions.PQRException;
import co.edu.uniquindio.unieventos.model.documents.Cuenta;
import co.edu.uniquindio.unieventos.model.documents.PQR;
import co.edu.uniquindio.unieventos.model.enums.EstadoPQR;
import co.edu.uniquindio.unieventos.model.vo.Usuario;
import co.edu.uniquindio.unieventos.repository.CuentaRepository;
import co.edu.uniquindio.unieventos.repository.PQRRepository;
import co.edu.uniquindio.unieventos.service.service.CuentaService;
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
    private final CuentaRepository cuentaRepository;

    @Override
    public void crearPQR(CrearPQRDTO pqrDTO) throws PQRException {
        try {
            PQR pqr = new PQR();
            pqr.setIdUsuario(pqrDTO.idUsuario());
            pqr.setFechaCreacion(LocalDateTime.now());
            pqr.setEstadoPQR(EstadoPQR.PENDIENTE);
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
    public void eliminarPQR(String id) throws PQRException {
        Optional<PQR> pqrOpt = pqrRepository.findById(id);
        if (pqrOpt.isEmpty()) {
            throw new PQRException("PQR no encontrada");
        }
        pqrRepository.deleteById(id);
    }

    @Override
    public InformacionPQRDTO obtenerInformacionPQR(String id) throws PQRException {
        Optional<PQR> pqrOpt = pqrRepository.findById(id);
        if (pqrOpt.isEmpty()) {
            throw new PQRException("PQR no encontrada");
        }

        PQR pqr = pqrOpt.get();

        Cuenta cuenta = cuentaRepository.findById(pqr.getIdUsuario())
                .orElseThrow(() -> new PQRException("Usuario no encontrado"));

        return new InformacionPQRDTO(
                pqr.getId(),
                cuenta.getUsuario().getNombre(),
                cuenta.getUsuario().getTelefono(),
                cuenta.getEmail(),
                cuenta.getUsuario().getDireccion(),
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
    public void responderPQR(ResponderPQRDTO responderPqrDTO) throws PQRException {
        Optional<PQR> pqrOpt = pqrRepository.findById(responderPqrDTO.id());
        if (pqrOpt.isEmpty()) {
            throw new PQRException("PQR no encontrada");
        }

        PQR pqr = pqrOpt.get();

        if (pqr.getEstadoPQR() == EstadoPQR.RESUELTO) {
            throw new PQRException("La PQR ya ha sido resuelta");
        }

        pqr.setRespuesta(responderPqrDTO.respuesta());
        pqr.setEstadoPQR(EstadoPQR.RESUELTO);
        pqr.setFechaRespuesta(LocalDateTime.now());

        pqrRepository.save(pqr);
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