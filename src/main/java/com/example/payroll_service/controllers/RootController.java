package com.example.payroll_service.controllers;

import com.example.payroll_service.models.Order;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Controller
public class RootController {

    @GetMapping
    public String index(Model model) {
        RepresentationModel rootModel = new RepresentationModel();
        rootModel.add(linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
        rootModel.add(linkTo(methodOn(OrderController.class).all()).withRel("orders"));
        model.addAttribute("employees",
                linkTo(methodOn(EmployeeController.class).all()).withRel("employees").toUri().toString());
        model.addAttribute("orders",
                linkTo(methodOn(OrderController.class).all()).withRel("orders").toString());
        return "index";
    }
}
