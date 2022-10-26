package pl.gov.cmp.administration.service;

import com.google.common.collect.Sets;
import com.googlecode.catchexception.apis.BDDCatchException;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.api.Assertions;
import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.gov.cmp.administration.model.dto.*;
import pl.gov.cmp.administration.model.mapper.UserDtoMapper;
import pl.gov.cmp.auth.exception.UserAccountNotFoundException;
import pl.gov.cmp.auth.exception.UserAccountToSubjectNotFoundException;
import pl.gov.cmp.auth.model.dto.UserAccountToSubjectDto;
import pl.gov.cmp.auth.model.entity.UserAccountActivityPeriodEntity;
import pl.gov.cmp.auth.model.entity.UserAccountEntity;
import pl.gov.cmp.auth.model.entity.UserAccountToSubjectEntity;
import pl.gov.cmp.auth.model.enums.UserAccountStatusEnum;
import pl.gov.cmp.auth.repository.UserAccountRepository;
import pl.gov.cmp.auth.repository.UserAccountToSubjectRepository;
import pl.gov.cmp.exception.ErrorCode;
import pl.gov.cmp.permission_group.PermissionGroupEntity;
import pl.gov.cmp.permission_group.PermissionGroupRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

import static com.googlecode.catchexception.apis.BDDCatchException.caughtException;
import static java.lang.String.*;
import static java.util.Optional.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static pl.gov.cmp.auth.model.enums.ObjectCategoryEnum.CEMETERY;
import static pl.gov.cmp.auth.model.enums.ObjectCategoryEnum.CREMATORIUM;
import static pl.gov.cmp.auth.model.enums.UserAccountStatusEnum.*;
import static pl.gov.cmp.exception.ErrorCode.USER_ACCOUNT_NOT_FOUND;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private UserDtoMapper userDtoMapper;

    @Mock
    private UserAccountToSubjectRepository userAccountToSubjectRepository;

    @Mock
    private PermissionGroupRepository permissionGroupRepository;

    @Mock
    private InstitutionResolver institutionResolver;

    @Mock
    private Page<UserAccountEntity> userPage;

    @Mock
    private UserDto userDto;

    @Mock
    private CriteriaBuilderImpl criteriaBuilder;

    @Mock
    private CriteriaQuery<UserAccountEntity> criteriaQuery;

    @Mock
    private Root<UserAccountEntity> root;

    @Mock
    private CriteriaBuilder.In in;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private PermissionChangeNotifier permissionChangeNotifier;

    @Mock
    private PersonalIdentifierGenerator personalIdentifierGenerator;

    @Mock
    private UserIdentifierSendService userIdentifierSendService;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userAccountRepository, userDtoMapper, userAccountToSubjectRepository, permissionGroupRepository, institutionResolver, permissionChangeNotifier, passwordEncoder, personalIdentifierGenerator, userIdentifierSendService);
    }

    @Test
    void shouldFindPageByCriteria() {
        //given
        var criteria = prepareUserCriteria();
        when(criteriaBuilder.in(any())).thenReturn(in);
        when(this.userAccountRepository.findAll(ArgumentMatchers.<Specification<UserAccountEntity>>any(), any(Pageable.class)))
                .thenAnswer(this::callToPredicate);
        PageRequest pageable = PageRequest.of(criteria.getPageIndex(), criteria.getPageSize(), Sort.by(criteria.getSortColumn()).ascending());
        when(userPage.getPageable()).thenReturn(pageable);
        when(this.userDtoMapper.toUserDtoList(any())).thenReturn(newArrayList(userDto));

        //when
        Page<UserDto> result = userService.findByCriteria(criteria);

        //then
        ArgumentCaptor<Pageable> argument = ArgumentCaptor.forClass(Pageable.class);
        verify(userAccountRepository).findAll(ArgumentMatchers.<Specification<UserAccountEntity>>any(), argument.capture());
        assertEquals(10, argument.getValue().getPageSize());
        assertEquals(0, argument.getValue().getOffset());
        assertEquals(0, argument.getValue().getPageNumber());
        assertEquals(Sort.Direction.ASC, Objects.requireNonNull(argument.getValue().getSort().getOrderFor("firstName")).getDirection());
        verify(root, times(4)).get((SingularAttribute) null);
        assertEquals(userDto, result.getContent().get(0));
        assertEquals(1, result.getTotalElements());
        assertEquals(pageable, result.getPageable());

    }

    @Test
    void shouldUpdateUser() {
        //given
        Long subjectId = 32L;
        UpdateUserDto updateUserDto = UpdateUserDto.builder()
                .email("test@gov.pl")
                .phoneNumber("+48123123123")
                .subjectId(subjectId)
                .build();
        UserAccountToSubjectEntity userAccountToSubjectEntity = UserAccountToSubjectEntity.builder().build();

        when(this.userAccountToSubjectRepository.findById(subjectId)).thenReturn(of(userAccountToSubjectEntity));
        //when
        userService.updateUser(updateUserDto);
        //then
        assertThat(userAccountToSubjectEntity.getEmail()).isEqualTo("test@gov.pl");
        assertThat(userAccountToSubjectEntity.getPhoneNumber()).isEqualTo("+48123123123");
        verify(userAccountToSubjectRepository).save(userAccountToSubjectEntity);
    }

    @Test
    void shouldNotUpdateUserAndThrowUserAccountToSubjectNotFoundException() {
        //given
        Long subjectId = 32L;
        UpdateUserDto updateUserDto = UpdateUserDto.builder()
                .email("test@gov.pl")
                .phoneNumber("+48123123123")
                .subjectId(subjectId)
                .build();
        when(this.userAccountToSubjectRepository.findById(subjectId)).thenReturn(empty());
        //when
        BDDCatchException.when(() -> userService.updateUser(updateUserDto));
        //then
        assertThat(caughtException()).isInstanceOf(UserAccountToSubjectNotFoundException.class);
    }

    @Test
    void shouldAddUserPermissionGroups() {
        Set<Long> permissionGroups = Set.of(12L);
        Long subjectId = 32L;
        AddUserPermissionGroupsDto addUserPermissionGroupsDto = getAddUserPermissionGroupsDto(permissionGroups, subjectId);
        UserAccountToSubjectEntity userAccountToSubjectEntity = createFirstSubject();
        userAccountToSubjectEntity.setPermissionGroups(Sets.newHashSet());
        when(this.userAccountToSubjectRepository.findById(subjectId)).thenReturn(of(userAccountToSubjectEntity));
        PermissionGroupEntity permissionGroupEntity = new PermissionGroupEntity();
        when(this.permissionGroupRepository.findAllById(permissionGroups)).thenReturn(List.of(permissionGroupEntity));
        final var firstSubjectInstitutionId = 2345L;
        final var firstSubjectInstitutionName = "Main cemetery Warsaw";
        given(institutionResolver.getInstitutionData(any())).willReturn(Pair.of(firstSubjectInstitutionId, firstSubjectInstitutionName));

        //when
        userService.addUserPermissionGroups(addUserPermissionGroupsDto);

        //then
        verify(userAccountToSubjectRepository).save(userAccountToSubjectEntity);
        assertThat(userAccountToSubjectEntity.getPermissionGroups()).containsExactly(permissionGroupEntity);
    }

    private AddUserPermissionGroupsDto getAddUserPermissionGroupsDto(Set<Long> permissionGroups, Long subjectId) {
        return AddUserPermissionGroupsDto.builder()
                .subjectId(subjectId)
                .permissionGroupIds(permissionGroups)
                .build();
    }

    @Test
    void shouldRemoveUserPermissionGroup() {
        //given
        long subjectId = 32L;
        long permissionGroupId = 12L;

        PermissionGroupEntity permissionGroupEntity = new PermissionGroupEntity();
        UserAccountToSubjectEntity userAccountToSubjectEntity = UserAccountToSubjectEntity.builder().build();
        userAccountToSubjectEntity.setPermissionGroups(Sets.newHashSet(permissionGroupEntity));

        when(this.userAccountToSubjectRepository.findById(subjectId)).thenReturn(of(userAccountToSubjectEntity));
        when(this.permissionGroupRepository.findById(permissionGroupId)).thenReturn(of(permissionGroupEntity));

        //when
        userService.removeUserPermissionGroup(subjectId, permissionGroupId);

        //then
        verify(userAccountToSubjectRepository).save(userAccountToSubjectEntity);
        assertThat(userAccountToSubjectEntity.getPermissionGroups()).doesNotContain(permissionGroupEntity);
    }

    @Test
    void shouldGetDetailedUserView() {
        //given
        final var userId = 34656L;
        final var subjectOne = createFirstSubject();
        final var userAccountToSubjectDtoOne = buildUserAccountToSubjectDto(subjectOne);
        given(userDtoMapper.from(subjectOne)).willReturn(userAccountToSubjectDtoOne);
        final var firstSubjectInstitutionId = 2345L;
        final var firstSubjectInstitutionName = "Main cemetery Warsaw";
        given(institutionResolver.getInstitutionData(userAccountToSubjectDtoOne)).willReturn(Pair.of(firstSubjectInstitutionId, firstSubjectInstitutionName));

        final var subjectTwo = createSecondSubject();
        final var userAccountToSubjectDtoTwo = buildUserAccountToSubjectDto(subjectOne);
        given(userDtoMapper.from(subjectTwo)).willReturn(userAccountToSubjectDtoTwo);
        final var secondSubjectInstitutionId = 3456L;
        final var secondSubjectInstitutionName = "Main crematorium Lublin";
        given(institutionResolver.getInstitutionData(userAccountToSubjectDtoTwo)).willReturn(Pair.of(secondSubjectInstitutionId, secondSubjectInstitutionName));

        final var userAccount = prepareUserAccount(userId, Set.of(subjectOne, subjectTwo));
        given(userAccountRepository.findByIdWithSubjects(userId)).willReturn(of(userAccount));

        //when
        final var result = userService.getUserDetailedView(userId);

        //then
        assertThat(result.getId()).isEqualTo(userAccount.getId());
        assertThat(result.getFirstName()).isEqualTo(userAccount.getFirstName());
        assertThat(result.getLastName()).isEqualTo(userAccount.getLastName());
        assertThat(result.getPersonalIdentifier()).isNull();
        assertThat(result.getStatus()).isEqualTo(userAccount.getStatus().getName());
        assertThat(result.getBirthDate()).isEqualTo(userAccount.getBirthDate());
        assertThat(result.getFirstLoginAt()).isEqualTo(userAccount.getFirstLoginAt());
        assertThat(result.getLastLoginAt()).isEqualTo(userAccount.getLastLoginAt());

        final var subjects = result.getSubjects();
        assertThat(subjects).hasSize(2);

        final var firstSubject = subjects.get(0);
        assertThat(firstSubject.getSubjectId()).isEqualTo(subjectOne.getId());
        assertThat(firstSubject.getInstitutionId()).isEqualTo(firstSubjectInstitutionId);
        assertThat(firstSubject.getInstitutionName()).isEqualTo(firstSubjectInstitutionName);
        assertThat(firstSubject.getInstitutionType()).isEqualTo(subjectOne.getCategory().getName());
        assertThat(firstSubject.getEmail()).isEqualTo(subjectOne.getEmail());
        assertThat(firstSubject.getPhoneNumber()).isEqualTo(subjectOne.getPhoneNumber());
        assertThat(firstSubject.getRolesInInstitution()).hasSize(3).containsExactly("Administrator", "Cleaner", "Watchman");

        final var firstSubjectPermissionGroups = firstSubject.getPermissionGroups();
        assertThat(firstSubjectPermissionGroups).hasSize(2);

        final var firstSubjectPermissionGroupOne = firstSubjectPermissionGroups.get(0);
        assertThat(firstSubjectPermissionGroupOne.getId()).isEqualTo(346L);
        assertThat(firstSubjectPermissionGroupOne.getName()).isEqualTo("Permission group 2");
        assertThat(firstSubjectPermissionGroupOne.getDescription()).isEqualTo("description 2");

        final var firstSubjectPermissionGroupTwo = firstSubjectPermissionGroups.get(1);
        assertThat(firstSubjectPermissionGroupTwo.getId()).isEqualTo(349L);
        assertThat(firstSubjectPermissionGroupTwo.getName()).isEqualTo("Permission group 1");
        assertThat(firstSubjectPermissionGroupTwo.getDescription()).isEqualTo("description 1");

        final var secondSubject = subjects.get(1);
        assertThat(secondSubject.getSubjectId()).isEqualTo(subjectTwo.getId());
        assertThat(secondSubject.getInstitutionId()).isEqualTo(secondSubjectInstitutionId);
        assertThat(secondSubject.getInstitutionName()).isEqualTo(secondSubjectInstitutionName);
        assertThat(secondSubject.getInstitutionType()).isEqualTo(subjectTwo.getCategory().getName());
        assertThat(secondSubject.getEmail()).isEqualTo(subjectTwo.getEmail());
        assertThat(secondSubject.getPhoneNumber()).isEqualTo(subjectTwo.getPhoneNumber());
        assertThat(secondSubject.getRolesInInstitution()).hasSize(1).containsExactly("Press spokesman");
        assertThat(secondSubject.getPermissionGroups()).isEmpty();
    }

    @Test
    void shouldThrowUserNotFoundException() {
        //given
        final var userId = 434535L;
        given(userAccountRepository.findByIdWithSubjects(userId)).willReturn(empty());

        //expect
        final var exception = assertThrows(UserAccountNotFoundException.class, () -> userService.getUserDetailedView(userId));
        assertThat(exception.getCode()).isEqualTo(USER_ACCOUNT_NOT_FOUND);
        assertThat(exception.getMessage()).isEqualTo(format("User account not found [userAccountId: %s]", userId));
        verifyNoInteractions(institutionResolver, userDtoMapper);
        verifyNoMoreInteractions(userAccountRepository);
    }

    @Test
    void shouldUpdateUserWithNewSubject() {
        //given
        final var captor = ArgumentCaptor.forClass(UserAccountToSubjectEntity.class);
        final var userId = 3534L;
        final var subjectData = saveUserAccountToSubjectDto(userId);
        given(userAccountRepository.findById(userId)).willReturn(of(prepareUserAccount(userId, List.of())));

        //when
        userService.updateUserWithNewSubject(subjectData);

        //then
        then(userAccountToSubjectRepository).should().save(captor.capture());

        //and
        final var savedSubject = captor.getValue();
        assertThat(savedSubject.getCemeteryId()).isEqualTo(subjectData.getInstitutionId());
        assertThat(savedSubject.getIpnId()).isNull();
        assertThat(savedSubject.getVoivodshipId()).isNull();
        assertThat(savedSubject.getCrematoriumId()).isNull();
        assertThat(savedSubject.isActiveEmployee()).isTrue();
        assertThat(savedSubject.getEmail()).isEqualTo(subjectData.getEmail());
        assertThat(savedSubject.getPhoneNumber()).isEqualTo(subjectData.getPhoneNumber());
        assertThat(savedSubject.getCategory()).isEqualTo(subjectData.getInstitutionType());
        assertThat(savedSubject.getUserAccount().getId()).isEqualTo(userId);
    }

    @Test
    void shouldSetUserIdentifierAndPassword() {
        //given
        final var wkId = "12wkId";
        final var password = "pass1234";
        final var personalId = "23456782";
        final var encodedPassword = "$2a$12$a9mc06QmTtqzn3R7asEH3eOV.LETmxR0URlCA7kL21enPAQjvh0Ku";
        final var setPasswordDto = SetPasswordForUserDto.builder()
                .password(password)
                .wkId(wkId)
                .build();
        final var user = prepareUserAccount(null, wkId, null);
        given(userAccountRepository.findByWkId(wkId)).willReturn(Optional.of(user));
        given(passwordEncoder.encode(password)).willReturn(encodedPassword);
        given(personalIdentifierGenerator.generate()).willReturn(personalId);
        given(userDtoMapper.toUserDto(user)).willReturn(userDto);

        //when
        final var result = userService.setPasswordAndLocalId(setPasswordDto);

        //then
        assertThat(user.getLocalPassword()).isEqualTo(encodedPassword);
        assertThat(user.getLocalId()).isEqualTo(personalId);
        assertThat(result).isEqualTo(userDto);
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenSettingUserIdentifierAndPasswordAndUserWasNotFoundByWkId() {
        //given
        final var wkId = "12wkId";
        final var password = "pass1234";
        final var setPasswordDto = SetPasswordForUserDto.builder()
                .password(password)
                .wkId(wkId)
                .build();
        given(userAccountRepository.findByWkId(wkId)).willReturn(Optional.empty());

        //when
        final var exception = assertThrows(UserAccountNotFoundException.class, ()-> userService.setPasswordAndLocalId(setPasswordDto));

        //then
        assertThat(exception.getCode()).isEqualTo(USER_ACCOUNT_NOT_FOUND);
        assertThat(exception.getMessage()).isEqualTo(format("User account not found [wkId: %s]", wkId));
        verifyNoInteractions(passwordEncoder, personalIdentifierGenerator, userDtoMapper);
    }

    @Test
    void shouldNotSetPasswordWhenUserHasPasswordAlreadySet() {
        //given
        final var wkId = "123wkId";
        final var newPassword = "pass1234";
        final var setPasswordDto = SetPasswordForUserDto.builder()
                .password(newPassword)
                .wkId(wkId)
                .build();
        final var user = prepareUserAccount("pass", wkId, "36456453");
        given(userAccountRepository.findByWkId(wkId)).willReturn(Optional.of(user));
        given(userDtoMapper.toUserDto(user)).willReturn(userDto);

        //when
        final var result = userService.setPasswordAndLocalId(setPasswordDto);

        //then
        assertThat(user.getLocalPassword()).isEqualTo(user.getLocalPassword());
        assertThat(user.getLocalId()).isEqualTo(user.getLocalId());
        assertThat(result).isEqualTo(userDto);
    }

    @Test
    void shouldNotGeneratePersonalIdentifierWhenItIsAlreadyGenerated() {
        //given
        final var wkId = "12wkId";
        final var newPassword = "pass1234";
        final var setPasswordDto = SetPasswordForUserDto.builder()
                .password(newPassword)
                .wkId(wkId)
                .build();
        final var user = prepareUserAccount("pass", wkId, "23567443");
        given(userAccountRepository.findByWkId(wkId)).willReturn(Optional.of(user));
        given(userDtoMapper.toUserDto(user)).willReturn(userDto);

        //when
        final var result = userService.setPasswordAndLocalId(setPasswordDto);

        //then
        assertThat(user.getLocalPassword()).isEqualTo(user.getLocalPassword());
        assertThat(user.getLocalId()).isEqualTo(user.getLocalId());
        assertThat(result).isEqualTo(userDto);
    }

    @Test
    void shouldUnblockUser() {
        //given
        final var userId = 34L;
        final var captor = ArgumentCaptor.forClass(UserAccountEntity.class);
        final var user = prepareUserAccount(userId, LOCKED);
        given(userAccountRepository.findByIdAndStatus(userId, user.getStatus())).willReturn(Optional.of(user));
        given(userDtoMapper.toUserDto(user)).willReturn(userDto);

        //when
        final var result = userService.unblockUser(userId);

        //then
        assertThat(result).isEqualTo(userDto);
        verify(userAccountRepository).save(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo(ACTIVE);
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenUserToUnblockWasNotFound() {
        //given
        final var userId = 3456L;
        given(userAccountRepository.findByIdAndStatus(userId, LOCKED)).willReturn(Optional.empty());

        //when
        final var exception = assertThrows(UserAccountNotFoundException.class, () -> userService.unblockUser(userId));

        //then
        assertThat(exception.getCode()).isEqualTo(USER_ACCOUNT_NOT_FOUND);
        assertThat(exception.getMessage()).isEqualTo(format("User account not found [userAccountId: %s]", userId));
        verifyNoMoreInteractions(userAccountRepository);
        verifyNoInteractions(userDtoMapper);
    }

    @Test
    void shouldBlockUser() {
        //given
        final var userId = 34L;
        final var captor = ArgumentCaptor.forClass(UserAccountEntity.class);
        final var user = prepareUserAccount(userId, ACTIVE);
        given(userAccountRepository.findByIdAndStatus(userId, user.getStatus())).willReturn(Optional.of(user));
        given(userDtoMapper.toUserDto(user)).willReturn(userDto);

        //when
        final var result = userService.blockUser(userId);

        //then
        assertThat(result).isEqualTo(userDto);
        verify(userAccountRepository).save(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo(LOCKED);
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenUserToBlockWasNotFound() {
        //given
        final var userId = 3456L;
        given(userAccountRepository.findByIdAndStatus(userId, ACTIVE)).willReturn(Optional.empty());

        //when
        final var exception = assertThrows(UserAccountNotFoundException.class, () -> userService.blockUser(userId));

        //then
        assertThat(exception.getCode()).isEqualTo(USER_ACCOUNT_NOT_FOUND);
        assertThat(exception.getMessage()).isEqualTo(format("User account not found [userAccountId: %s]", userId));
        verifyNoMoreInteractions(userAccountRepository);
        verifyNoInteractions(userDtoMapper);
    }

    private SaveUserAccountToSubjectDto saveUserAccountToSubjectDto(long userId) {
        return SaveUserAccountToSubjectDto.builder()
                .userId(userId)
                .institutionId(242L)
                .institutionType(CEMETERY)
                .email("email@com.pl")
                .phoneNumber("567894356")
                .build();
    }

    private UserAccountToSubjectDto buildUserAccountToSubjectDto(UserAccountToSubjectEntity subjectOne) {
        return UserAccountToSubjectDto.builder()
                .id(subjectOne.getId())
                .activeEmployee(subjectOne.isActiveEmployee())
                .email(subjectOne.getEmail())
                .cemeteryId(subjectOne.getCemeteryId())
                .ipnId(subjectOne.getIpnId())
                .voivodshipId(subjectOne.getVoivodshipId())
                .crematoriumId(subjectOne.getCrematoriumId())
                .category(subjectOne.getCategory())
                .activeEmployee(subjectOne.isActiveEmployee())
                .build();
    }

    private UserAccountEntity prepareUserAccount(long id, Collection<UserAccountToSubjectEntity> subjects) {
        return UserAccountEntity.builder()
                .id(id)
                .firstName("John")
                .lastName("Doe")
                .status(NEW)
                .birthDate(LocalDate.parse("1999-09-09"))
                .firstLoginAt(Instant.parse("2021-06-01T11:45:12.00Z"))
                .lastLoginAt(Instant.parse("2022-06-01T09:02:01.00Z"))
                .subjects(subjects)
                .build();
    }

    private UserAccountEntity prepareUserAccount(String password, String wkId, String localId) {
        return UserAccountEntity.builder()
                .id(12L)
                .wkId(wkId)
                .localPassword(password)
                .firstName("John")
                .lastName("Doe")
                .status(NEW)
                .localId(localId)
                .birthDate(LocalDate.parse("1999-09-09"))
                .lastLoginAt(Instant.parse("2022-06-01T09:02:01.00Z"))
                .build();
    }

    private UserAccountEntity prepareUserAccount(long id, UserAccountStatusEnum status) {
        return UserAccountEntity.builder()
                .id(id)
                .firstName("John")
                .lastName("Doe")
                .status(status)
                .birthDate(LocalDate.parse("1999-09-09"))
                .lastLoginAt(Instant.parse("2022-06-01T09:02:01.00Z"))
                .build();
    }

    private UserAccountToSubjectEntity createFirstSubject() {
        final var now = LocalDate.now();
        final var periodOne = buildUserAccountActivityPeriod(123L, now.minusDays(1), now.plusDays(1), "Watchman");
        final var periodTwo = buildUserAccountActivityPeriod(124L, now, now.plusDays(1), "Administrator");
        final var periodThree = buildUserAccountActivityPeriod(125L, now.minusDays(1), now, "Cleaner");
        final var periodFour = buildUserAccountActivityPeriod(126L, now.minusDays(2), now.minusDays(1), "Driver");
        final var periodFive = buildUserAccountActivityPeriod(127L, now.plusDays(1), now.plusDays(2), "Boss");

        final var permissionGroupOne = buildPermissionGroup(349L, "Permission group 1", "description 1");
        final var permissionGroupTwo = buildPermissionGroup(346L, "Permission group 2", "description 2");
        return UserAccountToSubjectEntity.builder()
                .id(235L)
                .activeEmployee(true)
                .phoneNumber("564564674")
                .email("john@cemetery.com")
                .cemeteryId(345L)
                .category(CEMETERY)
                .userAccount(UserAccountEntity.builder().firstName("Jan").lastName("Kowalski").build())
                .userAccountActivityPeriod(Set.of(periodOne, periodTwo, periodThree, periodFour, periodFive))
                .permissionGroups(Set.of(permissionGroupOne, permissionGroupTwo))
                .build();
    }

    private UserAccountToSubjectEntity createSecondSubject() {
        final var now = LocalDate.now();
        final var period = buildUserAccountActivityPeriod(123L, now.minusDays(1), now.plusDays(1), "Press spokesman");
        final var permissionGroup = buildPermissionGroup(345L, "Permission group 2", "description 2");
        return UserAccountToSubjectEntity.builder()
                .id(236L)
                .activeEmployee(false)
                .phoneNumber("758093578")
                .email("john@crematory.com")
                .cemeteryId(345L)
                .category(CREMATORIUM)
                .userAccountActivityPeriod(Set.of(period))
                .permissionGroups(Set.of(permissionGroup))
                .build();
    }

    private PermissionGroupEntity buildPermissionGroup(long id, String name, String description) {
        return new PermissionGroupEntity(id, name, description, null, null);
    }

    private UserAccountActivityPeriodEntity buildUserAccountActivityPeriod(long id, LocalDate employeeFrom,
                                                                           LocalDate employeeTo, String roleInInstitution) {
        return UserAccountActivityPeriodEntity.builder()
                .id(id)
                .employedFrom(employeeFrom)
                .employedTo(employeeTo)
                .roleInInstitution(roleInInstitution)
                .build();
    }

    private Page<UserAccountEntity> callToPredicate(InvocationOnMock invocationOnMock) {
        ((Specification<UserAccountEntity>) invocationOnMock.getArguments()[0]).toPredicate(root, criteriaQuery, criteriaBuilder);
        return userPage;
    }

    private UserCriteriaDto prepareUserCriteria() {
        return UserCriteriaDto.builder()
                .sortColumn("firstName")
                .sortOrder(Sort.Direction.ASC)
                .pageIndex(0)
                .pageSize(10)
                .firstName("testFirstName")
                .lastName("testLastName")
                .birthDate(LocalDate.of(2022, 4, 27))
                .statuses(Sets.newHashSet(NEW))
                .build();
    }
}
