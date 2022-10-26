package pl.gov.cmp.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.gov.cmp.application.controller.protocol.request.ApplicationCemeteryDraftRequest;
import pl.gov.cmp.application.model.entity.ApplicationCemeteryDraftEntity;
import pl.gov.cmp.application.model.mapper.ApplicationDraftDtoMapper;
import pl.gov.cmp.application.repository.ApplicationCemeteryDraftRepository;
import pl.gov.cmp.application.repository.ApplicationCustomDraftRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ApplicationDraftServiceTest {

    @Mock
    ApplicationDraftDtoMapper applicationDraftDtoMapper;
    @Mock
    ApplicationCustomDraftRepository applicationCustomDraftRepository;
    @Mock
    ApplicationCemeteryDraftRepository applicationCemeteryDraftRepository;

    private ApplicationDraftService applicationDraftService;


    @BeforeEach
    void setUp() {
        applicationDraftService = new ApplicationDraftService(
                applicationDraftDtoMapper,
                applicationCustomDraftRepository,
                applicationCemeteryDraftRepository);
    }

    @Test
    void shouldSaveApplicationCemeteryDraft() {
        //given
        final var request = new ApplicationCemeteryDraftRequest();
        request.setRequest("request");
        request.setDraftName("draftName");
        final var userId = 365L;
        final var draftCaptor = ArgumentCaptor.forClass(ApplicationCemeteryDraftEntity.class);

        //when
        assertDoesNotThrow(() -> applicationDraftService.saveApplicationCemeteryDraft(request, userId));

        //then
        then(applicationCemeteryDraftRepository).should().save(draftCaptor.capture());
        final var draftToSave = draftCaptor.getValue();
        assertThat(draftToSave.getDraft()).isEqualTo(request.getRequest());
        assertThat(draftToSave.getUserAccountId()).isEqualTo(userId);
        assertThat(draftToSave.getDraftName()).isEqualTo(request.getDraftName());
    }

    @Test
    void shouldGetApplicationCemeteryDraftsByUserId() {
        //given
        final var userId = 365L;
        final var firstDraft = createDraft("firstDraft", userId);
        final var secondDraft = createDraft("secondDraft", userId);
        given(applicationCemeteryDraftRepository.findByUserAccountId(userId))
                .willReturn(List.of(firstDraft, secondDraft));

        //when
        final var result = applicationDraftService.getApplicationCemeteryDraft(userId);

        //then
        final var firstReturnedDraft = result.get(0);
        assertThat(firstReturnedDraft.getDraft()).isEqualTo(firstDraft.getDraft());
        assertThat(firstReturnedDraft.getUserAccountId()).isEqualTo(userId);

        final var secondReturnedDraft = result.get(1);
        assertThat(secondReturnedDraft.getDraft()).isEqualTo(secondDraft.getDraft());
        assertThat(secondReturnedDraft.getUserAccountId()).isEqualTo(userId);
    }

    private ApplicationCemeteryDraftEntity createDraft(String draft, long userId) {
        final var draftEntity = new ApplicationCemeteryDraftEntity();
        draftEntity.setDraft(draft);
        draftEntity.setUserAccountId(userId);
        return draftEntity;
    }
}
