package com.example.lesson5.bff.app.web;

import com.example.lesson5.bff.app.model.*;
import com.example.lesson5.bff.domain.service.OrchestrateService;
import com.example.lesson5.bff.domain.service.SampleService;
import com.example.lesson5.common.apinfra.exception.BusinessException;
import com.example.lesson5.common.apinfra.exception.ValidationError;
import com.example.lesson5.common.apinfra.exception.ValidationErrorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class BackendForFrontendController {

    @Autowired
    MessageSource messageSource;

    @Autowired
    SampleService sampleService;

    @Autowired
    OrchestrateService orchestrateService;

    @ModelAttribute
    public AddUsersForm setUpForm(){
        return AddUsersForm.builder()
                .users(new ArrayList<>(
                        List.of(
                                User.builder()
                                        .address(new Address())
                                        .emailList(new ArrayList<>(List.of(new Email())))
                                        .build())))
                .build();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/index.html")
    public String index(){
        return "forward:portal";
    }

    @RequestMapping(method = RequestMethod.GET, value = "portal")
    public String portal(Model model){
        return "portal";
    }

    @RequestMapping(method = RequestMethod.GET, value = "getUser")
    @ResponseBody
    public ResponseEntity<User> getUser(String userId) throws BusinessException{
        return ResponseEntity.ok(UserMapper.map(sampleService.getUser(Long.getLong(userId))));
    }

    @RequestMapping(method = RequestMethod.GET, value = "getUsers")
    public String getUsers(Model model){
        model.addAttribute("users", UserMapper.map(sampleService.getUsers()));
        return "getUsers";
    }

    @RequestMapping(method = RequestMethod.GET, value = "isUsableLoginId")
    @ResponseBody
    public ResponseEntity<String> isUsableLoginId(String loginId, Locale locale){
        return sampleService.existsUserOfLoginId(loginId) ?
                ResponseEntity.ok(messageSource.getMessage("B0001", null, locale))
                : ResponseEntity.ok(messageSource.getMessage("B0002", null, locale));
    }

    @RequestMapping(method = RequestMethod.POST, value = "addUsers")
    public String addUsers(@Validated AddUsersForm addUsersForm,
                           BindingResult bindingResult, Model model, Locale locale){
        if(bindingResult.hasErrors()){
            return "portal";
        }

        try{
            model.addAttribute("users", orchestrateService.addUsers(
                    UserMapper.mapToResource(addUsersForm.getUsers())));
        }catch (BusinessException e){
            if(Objects.equals(e.getCode(), "E0002")){
                model.addAttribute("errorLoginId", e.getArgs()[0]);
                model.addAttribute("errorMessage", messageSource.getMessage(
                        e.getCode(), e.getArgs(), locale));
            }else if(Objects.equals(e.getCode(), "BE0001")){
                @SuppressWarnings("unchecked") List<ValidationError> validationErrors = (List<ValidationError>)e.getArgs()[0];
                bindingResult.getAllErrors().addAll(
                        ValidationErrorMapper.mapToFieldError(validationErrors));
            }
            return "portal";
        }
        return "addUsers";
    }



}
