package site.easy.to.build.crm.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

@Component
public class ImportalisationUtils {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void importerCSV(String cheminFichier, Class<?> classeTable) {
        String nomTable = classeTable.getSimpleName();

        try (BufferedReader br = new BufferedReader(new FileReader(cheminFichier))) {
            String ligne;
            String[] colonnes = br.readLine().split(",");

            String sql = construireRequeteSQL(nomTable, colonnes);

            while ((ligne = br.readLine()) != null) {
                String[] valeurs = ligne.split(",");
                jdbcTemplate.update(sql, (Object[]) valeurs);
            }

            System.out.println("Importation réussie dans la table : " + nomTable);

        } catch (IOException e) {
            System.err.println("Erreur de lecture du fichier CSV : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur lors de l'importation : " + e.getMessage());
        }
    }

    private String construireRequeteSQL(String nomTable, String[] colonnes) {
        String colonnesConcat = String.join(", ", colonnes);
        String placeholders = Arrays.stream(colonnes).map(col -> "?").reduce((a, b) -> a + ", " + b).get();

        return "INSERT INTO " + nomTable + " (" + colonnesConcat + ") VALUES (" + placeholders + ")";
    }
}
