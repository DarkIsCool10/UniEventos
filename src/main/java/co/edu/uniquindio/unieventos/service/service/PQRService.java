package co.edu.uniquindio.unieventos.service.service;

import co.edu.uniquindio.unieventos.dto.PQR.*;
import co.edu.uniquindio.unieventos.exceptions.PQRException;
import co.edu.uniquindio.unieventos.model.documents.PQR;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PQRService {

    void crearPQR(CrearPQRDTO pqrDTO) throws PQRException;

    void eliminarPQR(String id) throws PQRException;

    InformacionPQRDTO obtenerInformacionPQR(String id) throws PQRException;

    List<PQR> obtenerPQRsPorUsuario(String idUsuario) throws PQRException;

    void responderPQR(ResponderPQRDTO responderPqrDTO) throws PQRException;

    List<ItemPQRDTO> listarPQRs() throws PQRException;

}
