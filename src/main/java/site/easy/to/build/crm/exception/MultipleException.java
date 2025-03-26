package site.easy.to.build.crm.exception;

import java.util.ArrayList;
import java.util.List;

public class MultipleException extends Exception {
    List<String> errors = new ArrayList<>();

    public List<String> getErrors() {
        return errors;
    }
}
