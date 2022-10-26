package pl.gov.cmp.dictionary.exception;

import pl.gov.cmp.exception.AppRollbackException;

import static java.lang.String.format;
import static pl.gov.cmp.exception.ErrorCode.*;

public class DictionaryElementNotFoundException extends AppRollbackException {

    public DictionaryElementNotFoundException(Long id) {
        super(DICTIONARY_ELEMENT_NOT_FOUND, format("Dictionary element not found [dictionaryId: %s]", id));
    }

    public DictionaryElementNotFoundException(String code) {
        super(DICTIONARY_ELEMENT_NOT_FOUND, format("Dictionary element not found [code: %s]", code));
    }

}
