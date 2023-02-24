package mai.parts;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;

import java.util.function.UnaryOperator;


/*
    Creates a number field from a textfield if a correct regex is used
    apparently can't just create a class and extend textfield because javafx is garbage
 */

public class NumberField {

    public static TextField makeNumberField(TextField textField, String regex) {
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches(regex)) {
                return change;
            }
            return null;
        };

        textField.setTextFormatter(
                new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));

        return textField;
    }

}


