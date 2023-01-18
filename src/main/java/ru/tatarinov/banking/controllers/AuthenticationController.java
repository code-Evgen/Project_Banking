package ru.tatarinov.banking.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.tatarinov.banking.model.Client;
import ru.tatarinov.banking.services.ClientService;
import ru.tatarinov.banking.util.ClientValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {
    private final ClientService clientService;
    private final ClientValidator clientValidator;

    @Autowired
    public AuthenticationController(ClientService clientService, ClientValidator clientValidator) {
        this.clientService = clientService;
        this.clientValidator = clientValidator;
    }

    @GetMapping("/login")
    public String loginPage(){
        return "auth/login";
    }

    @GetMapping("/notFound")
    public String notFound(){
        return "auth/notFound";
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("client") Client client){
        return "/auth/registration";
    }

    @PostMapping("/registration")
    public String createUser(@ModelAttribute("client") @Valid Client client, BindingResult bindingResult){
        clientValidator.validate(client, bindingResult);
        if (bindingResult.hasErrors()){
            return "/auth/registration";
        }
        clientService.createUser(client);
        return "redirect:/auth/login";
    }

}
