package pl.gov.cmp.administration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.gov.cmp.administration.model.dto.CustomMessageDto;
import pl.gov.cmp.administration.model.dto.MessageCriteriaDto;
import pl.gov.cmp.administration.model.entity.MessageEntity;
import pl.gov.cmp.administration.model.mapper.MessageDtoMapper;
import pl.gov.cmp.administration.repository.MessageRepository;
import pl.gov.cmp.auth.model.entity.UserAccountToSubjectEntity;
import pl.gov.cmp.auth.repository.UserAccountToSubjectRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final MessageRepository repository;
    private final MessageDtoMapper messageDtoMapper;
    private final UserAccountToSubjectRepository userAccountToSubjectRepository;

    public Page<CustomMessageDto> findByCriteria(MessageCriteriaDto criteria) {
        UserAccountToSubjectEntity userAccountToSubject = userAccountToSubjectRepository.getById(criteria.getUserAccountToSubjectId());
        Pageable pageable =
                PageRequest.of(criteria.getPageIndex(), criteria.getPageSize());
        Page<MessageEntity> messagePage = this.repository.findByEmail(userAccountToSubject.getEmail(), pageable);
        List<CustomMessageDto> elements = this.messageDtoMapper.toMessageDtoList(messagePage.getContent());
        return new PageImpl<>(elements, messagePage.getPageable(), messagePage.getTotalElements());
    }

}
