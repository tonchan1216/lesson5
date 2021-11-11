package com.example.lesson5.backend.app.web;

import com.example.lesson5.backend.app.model.*;
import com.example.lesson5.backend.domain.service.SampleManyToManyService;
import com.example.lesson5.backend.domain.service.SampleOneToManyService;
import com.example.lesson5.backend.domain.service.SampleOneToOneService;
import com.example.lesson5.backend.domain.service.SampleService;
import com.example.lesson5.common.apinfra.exception.BusinessException;
import com.example.lesson5.common.web.model.AddressResource;
import com.example.lesson5.common.web.model.EmailResource;
import com.example.lesson5.common.web.model.GroupResource;
import com.example.lesson5.common.web.model.UserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;


@RestController
@RequestMapping("api/v1")
public class BackendController {

    @Autowired
    SampleService sampleService;

    @Autowired
    SampleOneToOneService sampleOneToOneService;

    @Autowired
    SampleOneToManyService sampleOneToManyService;

    @Autowired
    SampleManyToManyService sampleManyToManyService;

    @GetMapping("/users")
    public List<UserResource> getUsers(){
        return UserMapper.map(sampleService.findAll());
    }

    @GetMapping("/users/{userId:[0-9]+}")
    public UserResource getUser(@PathVariable Long userId)
            throws BusinessException {
        return UserMapper.map(sampleService.findOne(
                UserMapper.mapToEntity(User.builder().userId(userId).build())));
    }

    @PostMapping("/users/new")
    public UserResource addUser(
            @RequestBody @Validated({
                    User.AddUser.class, Default.class}) User user)
            throws BusinessException{
        return UserMapper.map(sampleService.add(
                UserMapper.mapToEntity(user)));
    }

    @PutMapping("/users/{userId:[0-9]+}")
    public UserResource updateUser(@RequestBody @Validated({
            User.UpdateUser.class, Default.class}) User user)
            throws BusinessException {
        return UserMapper.map(sampleService.update(
                UserMapper.mapToEntity(user)));
    }

    @DeleteMapping("/users/{userId:[0-9]+}")
    public UserResource deleteUser(@PathVariable Long userId)
            throws BusinessException{
        return UserMapper.map(sampleService.delete(UserMapper.mapToEntity(
                User.builder().userId(userId).build())));
    }

    @GetMapping("/users/loginId")
    public UserResource findUserOfLoginId(@Validated({
            User.FindUserOfLoginId.class, Default.class}) User user)
            throws BusinessException{
        return UserMapper.map(sampleService.findUserHave(
                user.getLoginId()));
    }

    @GetMapping("/users/{userId:[0-9]+}/address")
    public AddressResource findAddressOfUser(@PathVariable Long userId)
            throws BusinessException{
        return AddressMapper.map(sampleOneToOneService.findAddressOf(
                UserMapper.mapToEntity(User.builder().userId(userId).build())));
    }

    @GetMapping("/zipCodes/{zipCode:[-0-9]+}/users")
    public List<UserResource> findUsersHavingAddressOfZipCode(
            @PathVariable String zipCode){
        return UserMapper.map(sampleOneToOneService
                .findUsersHavingAddressOf(zipCode));
    }

    @GetMapping("/zipCodes/{zipCode:[-0-9]+}/not-users")
    public List<UserResource> findUsersNotHavingAddressOfZipCode(
            @PathVariable String zipCode){
        return UserMapper.map(sampleOneToOneService
                .findUsersNotHavingAddressOf(zipCode));
    }

    @PutMapping("/users/{userId:[0-9]+}/address")
    public AddressResource updateAddress(
            @RequestBody @Validated Address address) throws BusinessException{
        return AddressMapper.map(sampleOneToOneService.update(
                AddressMapper.mapToEntity(address)));
    }

    @GetMapping("/users/{userId:[0-9]+}/emails")
    public List<EmailResource> findEmailsOfUser(@PathVariable Long userId)
        throws BusinessException{
        return EmailMapper.map(sampleOneToManyService.getEmailsOf(
                UserMapper.mapToEntity(User.builder().userId(userId).build())));
    }

    @GetMapping("/emails/email/user")
    public UserResource findUserHavingEmail(@Validated(
            {Email.FindUserHave.class, Default.class}) Email email){
        return UserMapper.map(sampleOneToManyService
                .findUserHaving(email.getEmail()));
    }

    @PostMapping("/users/{userId:[0-9]+}/emails/new")
    public EmailResource addEmail(@RequestBody @Validated(
            {Email.Add.class, Default.class}) Email email) throws BusinessException{
        return EmailMapper.map(sampleOneToManyService.add(
                EmailMapper.mapToEntity(email)));
    }

    @PutMapping("/users/{userId:[0-9]+}/emails/{emailNo:[0-9]+}")
    public EmailResource updateEmail(@RequestBody @Validated(
            {Email.Update.class, Default.class}) Email email) throws BusinessException{
        return EmailMapper.map(sampleOneToManyService.update(
                EmailMapper.mapToEntity(email)));
    }

    @DeleteMapping("/users/{userId:[0-9]+}/emails/{emailNo:[0-9]+}")
    public EmailResource deleteEmail(@RequestBody @Validated Email email)
            throws BusinessException{
        return EmailMapper.map(sampleOneToManyService.delete(
                EmailMapper.mapToEntity(email)));
    }

    @DeleteMapping("/users/{userId:[0-9]+}/emails")
    public List<EmailResource> deleteAllEmail(@PathVariable Long userId)
            throws BusinessException{
        return EmailMapper.map(sampleOneToManyService.deleteAllEmail(
                UserMapper.mapToEntity(User.builder().userId(userId).build())));
    }

    @GetMapping("/users/{userId:[0-9]+}/groups")
    public List<GroupResource> findGroupsOfUser(@PathVariable Long userId){
        return GroupMapper.map(sampleManyToManyService.getGroupsOf(
                UserMapper.mapToEntity(User.builder().userId(userId).build())));
    }

    @GetMapping("/groups/{groupId:[0-9]+}/users")
    public List<UserResource> findUsersOfGroup(@PathVariable Long groupId){
        return UserMapper.map(sampleManyToManyService.getUsersOf(
                GroupMapper.mapToEntity(Group.builder().groupId(groupId).build())));
    }

    @GetMapping("/groups/{groupId:[0-9]+}/not-users")
    public List<UserResource> findUsersOfNotGroup(@PathVariable Long groupId){
        return UserMapper.map(sampleManyToManyService.getUsersOfNot(
                GroupMapper.mapToEntity(Group.builder().groupId(groupId).build())));
    }

    @PostMapping("/groups/{groupId:[0-9]+}/users/{userId:[0-9]+}")
    public UserResource addUserToGroup(
            @PathVariable Long userId, @PathVariable Long groupId)
            throws BusinessException{
        return UserMapper.map(sampleManyToManyService.addUserTo(
                GroupMapper.mapToEntity(Group.builder().groupId(groupId).build()),
                UserMapper.mapToEntity(User.builder().userId(userId).build())));
    }

    @DeleteMapping("/groups/{groupId:[0-9]+}/users/{userId:[0-9]+}")
    public UserResource deleteUserFromGroup(
            @PathVariable Long userId, @PathVariable Long groupId)
            throws BusinessException{
        return UserMapper.map(sampleManyToManyService.deleteUserFrom(
                GroupMapper.mapToEntity(Group.builder().groupId(groupId).build()),
                UserMapper.mapToEntity(User.builder().userId(userId).build())));
    }

    @DeleteMapping("/groups/{groupId:[0-9]+}")
    public GroupResource deleteGroup(@PathVariable Long groupId)
            throws BusinessException{
        return GroupMapper.map(sampleManyToManyService.delete(
                GroupMapper.mapToEntity(Group.builder().groupId(groupId).build())));
    }

}
