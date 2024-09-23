package co.edu.uniquindio.unieventos.service.implement;

import co.edu.uniquindio.unieventos.dto.orden.CrearOrdenDTO;
import co.edu.uniquindio.unieventos.dto.orden.EditarOrdenDTO;
import co.edu.uniquindio.unieventos.exceptions.OrdenException;
import co.edu.uniquindio.unieventos.service.service.OrdenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrdenServiceImp implements OrdenService {
    @Override
    public String crearOrden(CrearOrdenDTO ordenDTO) throws OrdenException {
        return null;
    }

    @Override
    public String editarOrden(EditarOrdenDTO ordenDTO, String ordenId) throws OrdenException {
        return null;
    }

    @Override
    public String eliminarOrden(String id) throws OrdenException {
        return null;
    }
}
