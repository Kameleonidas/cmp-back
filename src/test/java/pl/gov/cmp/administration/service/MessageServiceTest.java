package pl.gov.cmp.administration.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import pl.gov.cmp.administration.model.dto.CustomMessageDto;
import pl.gov.cmp.administration.model.dto.MessageCriteriaDto;
import pl.gov.cmp.administration.model.entity.MessageEntity;
import pl.gov.cmp.administration.model.mapper.MessageDtoMapper;
import pl.gov.cmp.administration.repository.MessageRepository;
import pl.gov.cmp.auth.model.entity.UserAccountToSubjectEntity;
import pl.gov.cmp.auth.repository.UserAccountToSubjectRepository;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    private static final long USER_ACCOUNT_TO_SUBJECT_ID = 2L;
    private static final String TEST_MC_GOV_PL = "test@mc.gov.pl";
    public static final long TOTAL_ELEMENTS = 10L;
    public static final int PAGE_INDEX = 0;
    public static final int PAGE_SIZE = 5;

    @Mock
    private MessageRepository repository;

    @Mock
    private MessageDtoMapper messageDtoMapper;

    @Mock
    private UserAccountToSubjectRepository userAccountToSubjectRepository;

    @Mock
    private Page<MessageEntity> messagePage;

    private MessageService userService;

    @BeforeEach
    void setUp() {
        userService = new MessageService(repository, messageDtoMapper, userAccountToSubjectRepository);
    }

    @Test
    void shouldFindPageByCriteria() {
        //given
        UserAccountToSubjectEntity userAccountToSubject = UserAccountToSubjectEntity.builder()
                .email(TEST_MC_GOV_PL)
                .build();
        given(userAccountToSubjectRepository.getById(USER_ACCOUNT_TO_SUBJECT_ID)).willReturn(userAccountToSubject);
        MessageCriteriaDto messageCriteria = MessageCriteriaDto.builder()
                .pageIndex(PAGE_INDEX)
                .pageSize(PAGE_SIZE)
                .userAccountToSubjectId(USER_ACCOUNT_TO_SUBJECT_ID).build();
        given(repository.findByEmail(eq(TEST_MC_GOV_PL), any())).willReturn(messagePage);
        ArrayList<MessageEntity> messageEntities = newArrayList();
        given(messagePage.getContent()).willReturn(messageEntities);
        given(messagePage.getPageable()).willReturn(PageRequest.of(PAGE_INDEX,PAGE_SIZE));
        given(messagePage.getTotalElements()).willReturn(TOTAL_ELEMENTS);
        given(this.messageDtoMapper.toMessageDtoList(messageEntities)).willReturn(newArrayList());

        //when
        Page<CustomMessageDto> result = userService.findByCriteria(messageCriteria);

        //then
        assertThat(result.getTotalElements()).isEqualTo(TOTAL_ELEMENTS);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(PAGE_INDEX);
        assertThat(result.getPageable().getPageSize()).isEqualTo(PAGE_SIZE);

    }
}
