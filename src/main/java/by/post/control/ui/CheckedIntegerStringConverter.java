package by.post.control.ui;

import javafx.scene.control.Alert;
import javafx.util.converter.IntegerStringConverter;

/**
 * Custom implementation of IntegerStringConverter
 *
 * @author Dmitriy V.Yefremov
 */
public class CheckedIntegerStringConverter extends IntegerStringConverter {

    private int defaultValue;

    public CheckedIntegerStringConverter() {

    }

    public CheckedIntegerStringConverter(int defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public Integer fromString(String value) {

        if (value == null) {
            return defaultValue > 0 ? defaultValue : null;
        }

        value = value.trim();

        if (value.length() < 1) {
            return defaultValue > 0 ? defaultValue : null;
        }

        if (!value.matches("\\d+")) {
            new Alert(Alert.AlertType.ERROR, "Please specify correct value!").showAndWait();
            return defaultValue > 0 ? defaultValue : null;
        }

        return Integer.valueOf(value);
    }
}
