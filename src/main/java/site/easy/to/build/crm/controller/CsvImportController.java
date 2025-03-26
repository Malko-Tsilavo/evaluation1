package site.easy.to.build.crm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import site.easy.to.build.crm.service.csv.CsvImportService;

@Controller
@RequestMapping("/manager")
public class CsvImportController {

    @Autowired
    private CsvImportService csvImportService;

    @PostMapping("/import-tickets")
    public String importTickets(@RequestParam("file") MultipartFile file) {
        csvImportService.importCsv(file);
        return "manager/import-tickets";
    }

    @GetMapping("/import-tickets")
    public String formImportTickets () {
        return "manager/import-tickets";
    }

    @GetMapping("/complete-data-import")
    public String formCompleteData () {
        return "manager/complete-data-import";
    }

    @PostMapping("/complete-data-import")
    public String importCompleteData (Model model, @RequestParam MultipartFile customerFile, @RequestParam MultipartFile budgetFile, @RequestParam MultipartFile depenseFile) {
        List<String> errors = csvImportService.importCompleteData(customerFile, budgetFile, depenseFile);

        model.addAttribute("errors", errors);
        
        return "manager/complete-data-import";
    }
    
}