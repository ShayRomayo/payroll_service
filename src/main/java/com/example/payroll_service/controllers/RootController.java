package com.example.payroll_service.controllers;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
public class RootController {

    @GetMapping
    public String index(Model model) {
        RepresentationModel rootModel = new RepresentationModel();
        rootModel.add(linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
        rootModel.add(linkTo(methodOn(OrderController.class).all()).withRel("orders"));
        model.addAttribute("employees",
                rootModel.getRequiredLink("employees").getHref());
        model.addAttribute("orders",
                rootModel.getRequiredLink("employees").getHref());
        return "index";
    }
}
