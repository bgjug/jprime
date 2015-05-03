package site.model;

import java.beans.PropertyEditorSupport;
import java.util.Arrays;

/**
 * @author Ivan St. Ivanov
 */
public class PaymentTypeEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(Arrays.stream(Registrant.PaymentType.values())
                .filter(paymentType -> paymentType.toString().equals(text))
                .findAny()
                .get());
    }
}
