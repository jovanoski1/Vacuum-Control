package rs.raf.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.raf.demo.model.ErrorMessage;
import rs.raf.demo.services.ErrorMessageService;

import java.util.List;

@RestController
@RequestMapping("/errors")
@CrossOrigin
public class ErrorMessageController {

    private final ErrorMessageService errorMessageService;

    @Autowired
    public ErrorMessageController(ErrorMessageService errorMessageService) {
        this.errorMessageService = errorMessageService;
    }


    @GetMapping()
    public ResponseEntity<List<ErrorMessage>> getAll(){
        return ResponseEntity.ok(errorMessageService.getAllByOwner());
    }
}
