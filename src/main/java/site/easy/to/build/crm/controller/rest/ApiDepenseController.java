package site.easy.to.build.crm.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.service.depense.DepenseService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/depenses")
public class ApiDepenseController {

    @Autowired
    private DepenseService depenseService;

    // Endpoint pour récupérer les dépenses des tickets par customer
    @GetMapping("/par-ticket")
    public List<Map<String, Object>> getDepensesByTicket() {
        return depenseService.getDepensesByTicket();
    }

    // Endpoint pour récupérer les dépenses des leads par customer
    @GetMapping("/par-lead")
    public List<Map<String, Object>> getDepenseByLead() {
        return depenseService.getDepensesByLead();
    }
}
