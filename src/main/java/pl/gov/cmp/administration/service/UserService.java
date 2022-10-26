package pl.gov.cmp.administration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.gov.cmp.administration.model.dto.*;
import pl.gov.cmp.administration.model.mapper.UserDtoMapper;
import pl.gov.cmp.auth.exception.PermissionGroupNotFoundException;
import pl.gov.cmp.auth.exception.UserAccountNotFoundException;
import pl.gov.cmp.auth.exception.UserAccountToSubjectNotFoundException;
import pl.gov.cmp.auth.model.entity.*;
import pl.gov.cmp.auth.model.enums.UserAccountStatusEnum;
import pl.gov.cmp.auth.repository.UserAccountRepository;
import pl.gov.cmp.auth.repository.UserAccountToSubjectRepository;
import pl.gov.cmp.permission_group.PermissionGroupEntity;
import pl.gov.cmp.permission_group.PermissionGroupRepository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

import static java.time.LocalDate.now;
import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserAccountRepository userAccountRepository;
    private final UserDtoMapper userDtoMapper;
    private final UserAccountToSubjectRepository userAccountToSubjectRepository;
    private final PermissionGroupRepository permissionGroupRepository;
    private final InstitutionResolver institutionResolver;
    private final PermissionChangeNotifier permissionChangeNotifier;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PersonalIdentifierGenerator personalIdentifierGenerator;
    private final UserIdentifierSendService userIdentifierSendService;

    public Page<UserDto> findByCriteria(UserCriteriaDto criteria) {
        var pageRequest = PageRequest.of(criteria.getPageIndex(), criteria.getPageSize(), this.getSort(criteria));
        var userPage = this.userAccountRepository
                .findAll((root, query, criteriaBuilder) -> this.getPredicate(criteria, root, criteriaBuilder), pageRequest);

        var elements = this.userDtoMapper.toUserDtoList(userPage.getContent());
        return new PageImpl<>(elements, userPage.getPageable(), userPage.getTotalElements());
    }

    public UserAccountEntity findByIdWithInstitutions(long userId) {
        return userAccountRepository.findByIdWithSubjects(userId).orElseThrow(() -> new UserAccountNotFoundException(userId));
    }

    private Predicate getPredicate(UserCriteriaDto criteria, Root<UserAccountEntity> root, CriteriaBuilder criteriaBuilder) {
        var predicates = new ArrayList<>();
        if (criteria.getFirstName() != null) {
            predicates.add(criteriaBuilder.equal(root.get(UserAccountEntity_.firstName), criteria.getFirstName()));
        }
        if (criteria.getLastName() != null) {
            predicates.add(criteriaBuilder.equal(root.get(UserAccountEntity_.lastName), criteria.getLastName()));
        }
        if (criteria.getBirthDate() != null) {
            predicates.add(criteriaBuilder.equal(root.get(UserAccountEntity_.birthDate), criteria.getBirthDate()));
        }
        if (criteria.getStatuses() != null && !criteria.getStatuses().isEmpty()) {
            var inClause = criteriaBuilder.in(root.get(UserAccountEntity_.status));
            criteria.getStatuses().forEach(inClause::value);
            predicates.add(inClause);
        }
        if (criteria.getCategories() != null && !criteria.getCategories().isEmpty()) {
            Join<UserAccountEntity, UserAccountToSubjectEntity> subjects = root.join(UserAccountEntity_.subjects);
            var inClause = criteriaBuilder.in(subjects.get(UserAccountToSubjectEntity_.category));
            criteria.getCategories().forEach(inClause::value);
            predicates.add(inClause);
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    }

    private Sort getSort(UserCriteriaDto criteria) {
        if (criteria.getSortOrder() == Sort.Direction.ASC) {
            return Sort.by(criteria.getSortColumn()).ascending();
        }
        return Sort.by(criteria.getSortColumn()).descending();
    }

    public void updateUser(UpdateUserDto updateUserDto) {
        var userAccountToSubjectEntity = userAccountToSubjectRepository.findById(updateUserDto.getSubjectId())
                .orElseThrow(() -> new UserAccountToSubjectNotFoundException(updateUserDto.getSubjectId()));
        userAccountToSubjectEntity.setEmail(updateUserDto.getEmail());
        userAccountToSubjectEntity.setPhoneNumber(updateUserDto.getPhoneNumber());
        userAccountToSubjectRepository.save(userAccountToSubjectEntity);
    }

    public void addUserPermissionGroups(AddUserPermissionGroupsDto addUserPermissionGroupsDto) {
        var userAccountToSubjectEntity = userAccountToSubjectRepository.findById(addUserPermissionGroupsDto.getSubjectId())
                .orElseThrow(() -> new UserAccountToSubjectNotFoundException(addUserPermissionGroupsDto.getSubjectId()));
        final var userAccountToSubjectDetailedView = toDetailedView(userAccountToSubjectEntity);
        var permissionGroups = getAllById(addUserPermissionGroupsDto);
        userAccountToSubjectEntity.getPermissionGroups().addAll(permissionGroups);
        userAccountToSubjectRepository.save(userAccountToSubjectEntity);
        final var mailParameters = ChangePermissionMailParameters.builder()
                .email(userAccountToSubjectDetailedView.getEmail())
                .userGrantPermissionFirstName(userAccountToSubjectEntity.getUserAccount().getFirstName())
                .userGrantPermissionLastName(userAccountToSubjectEntity.getUserAccount().getLastName())
                .institutionName(userAccountToSubjectDetailedView.getInstitutionName())
                .groupsNames(userAccountToSubjectDetailedView
                        .getPermissionGroups()
                        .stream()
                        .map(PermissionGroupView::getName)
                        .collect(toList()))
                .build();
        permissionChangeNotifier.sendEmail(mailParameters);
    }

    private List<PermissionGroupEntity> getAllById(AddUserPermissionGroupsDto addUserPermissionGroupsDto) {
        if (addUserPermissionGroupsDto.getPermissionGroupIds() != null && !addUserPermissionGroupsDto.getPermissionGroupIds().isEmpty()) {
            return permissionGroupRepository.findAllById(addUserPermissionGroupsDto.getPermissionGroupIds());
        }
        else {
            return Collections.emptyList();
        }
    }

    public void removeUserPermissionGroup(long subjectId, long permissionGroupId) {
        var userAccountToSubjectEntity = userAccountToSubjectRepository.findById(subjectId)
                .orElseThrow(() -> new UserAccountToSubjectNotFoundException(subjectId));
        var permissionGroup = permissionGroupRepository.findById(permissionGroupId)
                .orElseThrow(() -> new PermissionGroupNotFoundException(permissionGroupId));
        userAccountToSubjectEntity.getPermissionGroups().remove(permissionGroup);
        userAccountToSubjectRepository.save(userAccountToSubjectEntity);
    }

    public UserDto unblockUser(Long userId) {
        var userAccount= userAccountRepository.findByIdAndStatus(userId, UserAccountStatusEnum.LOCKED)
                .orElseThrow(() -> new UserAccountNotFoundException(userId));
        userAccount.setStatus(UserAccountStatusEnum.ACTIVE);
        userAccountRepository.save(userAccount);
        return userDtoMapper.toUserDto(userAccount);
    }

    public UserDto blockUser(Long userId) {
        var userAccount= userAccountRepository.findByIdAndStatus(userId, UserAccountStatusEnum.ACTIVE)
                .orElseThrow(() -> new UserAccountNotFoundException(userId));
        userAccount.setStatus(UserAccountStatusEnum.LOCKED);
        userAccountRepository.save(userAccount);
        return userDtoMapper.toUserDto(userAccount);
    }

    @Transactional
    public UserDto setPasswordAndLocalId(SetPasswordForUserDto localUserDto) {
        final var wkId = localUserDto.getWkId();
        final var user = userAccountRepository.findByWkId(wkId)
                .orElseThrow(() -> new UserAccountNotFoundException(wkId));
        trySetPassword(localUserDto.getPassword(), user);
        trySetLocalId(user);
        userIdentifierSendService.sendUserIdentifier(user);
        return userDtoMapper.toUserDto(user);
    }

    private void trySetLocalId(UserAccountEntity user) {
        if(isNull(user.getLocalId())) {
            user.setLocalId(personalIdentifierGenerator.generate());
        } else {
            log.warn("Personal identifier is already set. Generating new personal identifier skipped");
        }
    }

    private void trySetPassword(String password, UserAccountEntity user) {
        if(isNull(user.getLocalPassword())) {
            user.setLocalPassword(passwordEncoder.encode(password));
        } else {
            log.warn("Password is already set. Setting new password skipped");
        }
    }

    public UserDetailedView getUserDetailedView(long id) {
        final var userAccount = findByIdWithInstitutions(id);
        return UserDetailedView.builder()
                .id(userAccount.getId())
                .firstName(userAccount.getFirstName())
                .lastName(userAccount.getLastName())
                .status(userAccount.getStatus().getName())
                .birthDate(userAccount.getBirthDate())
                .lastLoginAt(userAccount.getLastLoginAt())
                .firstLoginAt(userAccount.getFirstLoginAt())
                .subjects(buildSubjects(userAccount.getSubjects()))
                .personalIdentifier(userAccount.getLocalId())
                .build();
    }

    private List<UserAccountToSubjectDetailedView> buildSubjects(Collection<UserAccountToSubjectEntity> subjects) {
        return subjects.stream()
                .map(this::toDetailedView)
                .sorted(comparing(UserAccountToSubjectDetailedView::isActiveEmployee, reverseOrder())
                        .thenComparing(UserAccountToSubjectDetailedView::getInstitutionType)
                        .thenComparing(UserAccountToSubjectDetailedView::getInstitutionName))
                .collect(toList());
    }

    private UserAccountToSubjectDetailedView toDetailedView(UserAccountToSubjectEntity userAccountToSubject) {
        final var institutionData = institutionResolver.getInstitutionData(userDtoMapper.from(userAccountToSubject));
        return UserAccountToSubjectDetailedView.builder()
                .subjectId(userAccountToSubject.getId())
                .institutionId(institutionData.getLeft())
                .institutionName(institutionData.getRight())
                .institutionType(userAccountToSubject.getCategory().getName())
                .email(userAccountToSubject.getEmail())
                .phoneNumber(userAccountToSubject.getPhoneNumber())
                .rolesInInstitution(getRolesInInstitution(userAccountToSubject.getUserAccountActivityPeriod()))
                .permissionGroups(getPermissionGroups(userAccountToSubject.isActiveEmployee(), userAccountToSubject.getPermissionGroups()))
                .activeEmployee(userAccountToSubject.isActiveEmployee())
                .build();

    }

    private List<String> getRolesInInstitution(Collection<UserAccountActivityPeriodEntity> userAccountActivityPeriods) {
        return userAccountActivityPeriods.stream()
                .filter(this::hasActiveRoleInInstitution)
                .map(UserAccountActivityPeriodEntity::getRoleInInstitution)
                .sorted().collect(toList());
    }

    private boolean hasActiveRoleInInstitution(UserAccountActivityPeriodEntity userAccountActivityPeriod) {
        final var now = now();
        return !now.isBefore(userAccountActivityPeriod.getEmployedFrom()) && !now.isAfter(userAccountActivityPeriod.getEmployedTo());
    }

    private List<PermissionGroupView> getPermissionGroups(boolean isActiveEmployee, Set<PermissionGroupEntity> permissionGroups) {
        return isActiveEmployee ? preparePermissionGroupViews(permissionGroups) : List.of();
    }

    private List<PermissionGroupView> preparePermissionGroupViews(Set<PermissionGroupEntity> permissionGroups) {
        return permissionGroups.stream().map(this::buildPermissionGroupView).sorted(comparing(PermissionGroupView::getId)).collect(toList());
    }

    private PermissionGroupView buildPermissionGroupView(PermissionGroupEntity permissionGroup) {
        return new PermissionGroupView(permissionGroup.getId(), permissionGroup.getName(), permissionGroup.getDescription());
    }

    public void updateUserWithNewSubject(SaveUserAccountToSubjectDto userAccountToSubjectData) {
        final var userId = userAccountToSubjectData.getUserId();
        final var user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new UserAccountNotFoundException(userId));
        final var userAccountToSubject = buildUrAccountToSubjectEntity(user, userAccountToSubjectData);
        userAccountToSubjectRepository.save(userAccountToSubject);
    }

    private UserAccountToSubjectEntity buildUrAccountToSubjectEntity(UserAccountEntity user, SaveUserAccountToSubjectDto saveUserAccountToSubjectDto) {
        final var userAccountToSubject = new UserAccountToSubjectEntity();
        userAccountToSubject.setInstitutionId(saveUserAccountToSubjectDto.getInstitutionId(), saveUserAccountToSubjectDto.getInstitutionType());
        userAccountToSubject.setUserAccount(user);
        userAccountToSubject.setEmail(saveUserAccountToSubjectDto.getEmail());
        userAccountToSubject.setPhoneNumber(saveUserAccountToSubjectDto.getPhoneNumber());
        userAccountToSubject.setCategory(saveUserAccountToSubjectDto.getInstitutionType());
        userAccountToSubject.setActiveEmployee(true);
        return userAccountToSubject;
    }
}
