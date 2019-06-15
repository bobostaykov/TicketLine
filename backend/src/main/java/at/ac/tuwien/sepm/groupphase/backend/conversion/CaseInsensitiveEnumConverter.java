package at.ac.tuwien.sepm.groupphase.backend.conversion;

import java.beans.PropertyEditorSupport;

public class CaseInsensitiveEnumConverter<T extends Enum<T>> extends PropertyEditorSupport {

    private final Class<T> typeParameter;

    public CaseInsensitiveEnumConverter(Class<T> typeParameter){
        super();
        this.typeParameter = typeParameter;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        String upper = text.toUpperCase();
        T value = T.valueOf(typeParameter, upper);
        setValue(value);
    }
}
