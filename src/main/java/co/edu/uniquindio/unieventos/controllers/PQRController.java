package co.edu.uniquindio.unieventos.controllers;

import co.edu.uniquindio.unieventos.model.documents.PQR;
import co.edu.uniquindio.unieventos.repository.PQRRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pqrs")
@RequiredArgsConstructor
public class PQRController {

    @Autowired
    private PQRRepository pqrRepository;



}
