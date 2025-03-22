package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import ch.qos.logback.core.model.Model;
import site.easy.to.build.crm.util.ImportalisationUtils;

@Controller
@RequestMapping("/import")
public class ImportController {

    @Autowired
    private ImportalisationUtils importalisationUtils;

    @PostMapping("/csv")
    public String importerCSV(@RequestParam("fichier") MultipartFile fichier, 
                              @RequestParam("classe") String nomClasse) {
        if (fichier.isEmpty()) {
            return "Erreur : Aucun fichier sélectionné.";
        }

        try {
            // Recherche dynamique de la classe à partir du nom fourni
            Class<?> classeTable = Class.forName("site.easy.to.build.crm.model." + nomClasse);
            
            // Création d'un fichier temporaire pour faciliter la lecture
            String cheminTemporaire = System.getProperty("java.io.tmpdir") + fichier.getOriginalFilename();
            fichier.transferTo(new java.io.File(cheminTemporaire));

            // Appel de la méthode d'importation
            importalisationUtils.importerCSV(cheminTemporaire, classeTable);
            return "Importation réussie dans la table : " + nomClasse;

        } catch (ClassNotFoundException e) {
            return "Erreur : La classe '" + nomClasse + "' n'existe pas.";
        } catch (Exception e) {
            return "Erreur lors de l'importation : " + e.getMessage();
        }
    }

    @GetMapping("/csv")
    public String goToCSV(Model model) {
        return "/importation/csv";
    }
}
