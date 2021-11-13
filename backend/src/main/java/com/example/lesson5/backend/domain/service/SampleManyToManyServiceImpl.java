package com.example.lesson5.backend.domain.service;

import com.example.lesson5.backend.domain.model.entity.Group;
import com.example.lesson5.backend.domain.model.entity.Membership;
import com.example.lesson5.backend.domain.model.entity.User;
import com.example.lesson5.backend.domain.repository.GroupRepository;
import com.example.lesson5.backend.domain.repository.UserRepository;
import com.example.lesson5.backend.domain.repository.specification.FindGroupsByUserId;
import com.example.lesson5.backend.domain.repository.specification.FindUsersByGroup;
import com.example.lesson5.backend.domain.repository.specification.FindUsersByNotGroup;
import com.example.lesson5.common.apinfra.exception.BusinessException;
import com.example.lesson5.common.apinfra.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@Transactional
public class SampleManyToManyServiceImpl implements SampleManyToManyService{

    @Autowired
    MessageSource messageSource;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupRepository groupRepository;

    @Override
    public List<Group> getGroupsOf(User user) {
        return groupRepository.findAll(
                FindGroupsByUserId.builder().userId(user.getUserId()).build());
    }

    @Override
    public List<User> getUsersOf(Group group) {
        return userRepository.findAll(
                FindUsersByGroup.builder().group(group).build());
    }

    @Override
    public List<User> getUsersOfNot(Group group) {
        return userRepository.findAll(
                FindUsersByNotGroup.builder().group(group).build());
    }

    @Override
    public User addUserTo(Group group, User addUser) throws BusinessException {
        Optional<User> optionalUser = userRepository.findById(addUser.getUserId());
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            Collection<Membership> memberships = user.getMembershipsByUserId();
            if(memberships.stream().noneMatch(
                    membership -> membership.getGroupId()==group.getGroupId())){
                user.getMembershipsByUserId().add(
                        Membership.builder()
                                .userId(addUser.getUserId())
                                .groupId(group.getGroupId())
                                .ver(0)
                                .lastUpdatedAt(DateUtil.now())
                                .build());
                return user;
            } else {
                String errorCode = "E0005";
                throw new BusinessException(errorCode, messageSource.getMessage(
                        errorCode, new Long[]{addUser.getUserId(), group.getGroupId()}, Locale.getDefault()));
            }
        }else{
            String errorCode = "E0001";
            throw new BusinessException(errorCode, messageSource.getMessage(
                    errorCode, new Long[]{addUser.getUserId()}, Locale.getDefault()));
        }
    }

    @Override
    public User deleteUserFrom(Group group, User deleteUser) throws BusinessException{
        Optional<User> optionalUser = userRepository.findById(deleteUser.getUserId());
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            Collection<Membership> memberships = user.getMembershipsByUserId();
            if(memberships.stream().anyMatch(
                    membership -> membership.getGroupId()==group.getGroupId())){
                memberships.removeIf(
                        membership -> membership.getGroupId()==group.getGroupId());
                return user;
            }else {
                String errorCode = "E0006";
                throw new BusinessException(errorCode, messageSource.getMessage(
                        errorCode, new Long[]{deleteUser.getUserId(), group.getGroupId()}, Locale.getDefault()));
            }
        } else {
            String errorCode = "E0001";
            throw new BusinessException(errorCode, messageSource.getMessage(
                    errorCode, new Long[]{deleteUser.getUserId()}, Locale.getDefault()));
        }
    }

    @Override
    public Group delete(Group group) throws BusinessException{
        Optional<Group> optionalGroup = groupRepository.findById(group.getGroupId());
        if(optionalGroup.isPresent()){
            Group deleteGroup = optionalGroup.get();
            groupRepository.delete(deleteGroup);
            return deleteGroup;
        }else {
            String errorCode = "E0007";
            throw new BusinessException(errorCode, messageSource.getMessage(
                    errorCode, new Long[]{group.getGroupId()}, Locale.getDefault()));
        }
    }

    @Override
    public User delete(User user) throws BusinessException{
        Optional<User> optionalUser = userRepository.findById(user.getUserId());
        if(optionalUser.isPresent()){
            User deleteUser = optionalUser.get();
            userRepository.delete(deleteUser);
            return deleteUser;
        }else {
            String errorCode = "E0001";
            throw new BusinessException(errorCode, messageSource.getMessage(
                    errorCode, new Long[]{user.getUserId()}, Locale.getDefault()));
        }
    }

}
