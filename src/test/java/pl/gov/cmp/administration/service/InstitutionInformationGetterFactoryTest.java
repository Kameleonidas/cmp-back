package pl.gov.cmp.administration.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.gov.cmp.administration.model.enums.InstitutionType;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith({MockitoExtension.class})
class InstitutionInformationGetterFactoryTest {

    @Mock
    private InstitutionInformationGetter cemeteryInformationGetterMock;

    @Mock
    private InstitutionInformationGetter crematoriumInformationGetterMock;

    @Mock
    private InstitutionInformationGetter ipnInformationGetterMock;

    private InstitutionInformationGetterFactory institutionInformationGetterFactory;

    @BeforeEach
    void setUp() {
        given(cemeteryInformationGetterMock.forType()).willReturn(InstitutionType.CEMETERY);
        given(crematoriumInformationGetterMock.forType()).willReturn(InstitutionType.CREMATORIUM);
        given(ipnInformationGetterMock.forType()).willReturn(InstitutionType.IPN);
        institutionInformationGetterFactory = new InstitutionInformationGetterFactory(
                asList(cemeteryInformationGetterMock,
                        crematoriumInformationGetterMock,
                        ipnInformationGetterMock));
    }

    @Test
    void shouldChooseCorrectCemeteryStrategy() {
        // given

        // when
        var strategy = institutionInformationGetterFactory.findStrategy(InstitutionType.CEMETERY);

        // then
        assertThat(strategy).isEqualTo(cemeteryInformationGetterMock);
    }

    @Test
    void shouldChooseCorrectCrematoriumStrategy() {
        // given

        // when
        var strategy = institutionInformationGetterFactory.findStrategy(InstitutionType.CREMATORIUM);

        // then
        assertThat(strategy).isEqualTo(crematoriumInformationGetterMock);
    }

    @Test
    void shouldChooseCorrectIpnStrategy() {
        // given

        // when
        var strategy = institutionInformationGetterFactory.findStrategy(InstitutionType.IPN);

        // then
        assertThat(strategy).isEqualTo(ipnInformationGetterMock);
    }

    @Test
    void shouldThrowExceptionWhenStrategyNotFound() {
        // given
        institutionInformationGetterFactory = new InstitutionInformationGetterFactory(
                asList(cemeteryInformationGetterMock,
                        ipnInformationGetterMock));

        // when then
        assertThrows(
                IllegalStateException.class,
                () -> institutionInformationGetterFactory.findStrategy(InstitutionType.CREMATORIUM));
    }
}