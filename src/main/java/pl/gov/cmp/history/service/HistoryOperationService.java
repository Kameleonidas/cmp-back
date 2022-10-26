package pl.gov.cmp.history.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.gov.cmp.history.model.dto.HistoryOperationDto;
import pl.gov.cmp.history.model.entity.HistoryOperationEntity;
import pl.gov.cmp.history.model.maper.HistoryOperationMapper;
import pl.gov.cmp.history.repository.HistoryOperationRepository;

import java.util.Collections;
import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Transactional
@Service
public class HistoryOperationService {

    private final HistoryOperationRepository historyOperationRepository;
    private final HistoryOperationMapper historyOperationMapper;
    private final MessageSource messageSource;

    public void saveHistoryOperation(HistoryOperationDto historyOperation) {
        HistoryOperationEntity historyOperationEntity = this.historyOperationMapper.toHistoryOperationEntity(historyOperation);
        String messageCode = String.join(".", "enum", historyOperation.getType().getClass().getSimpleName(), historyOperation.getType().name());
        List<String> messageParams = isEmpty(historyOperation.getParams()) ? Collections.emptyList() : historyOperation.getParams();
        String message = this.messageSource.getMessage(messageCode, messageParams.toArray(String[]::new), LocaleContextHolder.getLocale());
        historyOperationEntity.setDescription(message);
        this.historyOperationRepository.save(historyOperationEntity);
    }
}
