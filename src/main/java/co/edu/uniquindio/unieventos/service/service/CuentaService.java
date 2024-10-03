package co.edu.uniquindio.unieventos.service.service;

import co.edu.uniquindio.unieventos.dto.autenticacion.TokenDTO;
import co.edu.uniquindio.unieventos.dto.cuenta.*;
import co.edu.uniquindio.unieventos.exceptions.CarritoException;
import co.edu.uniquindio.unieventos.exceptions.CuentaException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CuentaService {

    void crearCuenta(CrearCuentaDTO cuenta) throws CuentaException, CarritoException;

    String editarCuenta(EditarCuentaDTO cuenta) throws CuentaException;

    String eliminarCuenta(String id) throws CuentaException;

    InformacionCuentaDTO obtenerInformacionCuenta(String id) throws CuentaException;

    List<ItemCuentaDTO> listarCuentas();

    String enviarCodigoRecuperacionPassword(String correo) throws CuentaException;

    String cambiarPassword(CambiarPasswordDTO cambiarPasswordDTO) throws CuentaException;

    TokenDTO iniciarSesion(LoginDTO loginDTO) throws CuentaException;
}
