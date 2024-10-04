package co.edu.uniquindio.unieventos.service.service;

import co.edu.uniquindio.unieventos.dto.autenticacion.TokenDTO;
import co.edu.uniquindio.unieventos.dto.cuenta.*;
import co.edu.uniquindio.unieventos.exceptions.CarritoException;
import co.edu.uniquindio.unieventos.exceptions.CuentaException;
import co.edu.uniquindio.unieventos.model.documents.Cuenta;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface CuentaService {

    void crearCuenta(CrearCuentaDTO cuenta) throws CuentaException, CarritoException;

    ValidarCodigoDTO validarCodigo(ValidarCodigoDTO validarCodigoDTO) throws CuentaException;

    void editarCuenta(EditarCuentaDTO cuenta) throws CuentaException;

    void eliminarCuenta(String id) throws CuentaException;

    InformacionCuentaDTO obtenerInformacionCuenta(String id) throws CuentaException;

    void enviarCodigoRecuperacionPassword(CodigoContraseniaDTO codigoContraseniaDTO) throws CuentaException;

    void cambiarPassword(CambiarPasswordDTO cambiarPasswordDTO) throws CuentaException;

    TokenDTO iniciarSesion(LoginDTO loginDTO) throws CuentaException;

    Map<String, Object> construirClaims(Cuenta cuenta);
}
