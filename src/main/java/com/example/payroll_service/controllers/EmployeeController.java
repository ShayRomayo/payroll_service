package com.example.payroll_service.controllers;

import com.example.payroll_service.EmployeeNotFoundException;
import com.example.payroll_service.models.Employee;
import com.example.payroll_service.models.EmployeeModelAssembler;
import com.example.payroll_service.repositories.EmployeeRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Controller
public class EmployeeController {

    private final static Logger LOGGER = Logger.getLogger(EmployeeController.class.getName());
    private final EmployeeRepository repository;
    private final EmployeeModelAssembler assembler;

    public EmployeeController(EmployeeRepository repository, EmployeeModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    // Old method used as RestController to maintain proper link relations in repository
    @ResponseBody
    @RequestMapping("/employees")
    public CollectionModel<EntityModel<Employee>> all() {
        List<EntityModel<Employee>> employees = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return new CollectionModel<>(employees,
                linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
    }

    // New Get Mapping to actually display employees using template HTML
    @GetMapping("/employees")
    public String all(Model model) {
        List<Employee> employees = repository.findAll();
        model.addAttribute("employees", employees);
        model.addAttribute("newEmployee", new Employee());

        return "employees";
    }

    @ResponseBody
    @RequestMapping("/employees/{id}")
    public EntityModel<Employee> one(@PathVariable Long id) {
        return assembler.toModel(repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id)));
    }

    @GetMapping("/employees/{id}")
    public String one(@PathVariable Long id, Model model) {
        Optional<Employee> checkEmployee = repository.findById(id);
        Employee employee;
        if (checkEmployee.isPresent()) {
            employee = checkEmployee.get();
            model.addAttribute("name", employee.getFirstName() + " " + employee.getLastName());
            model.addAttribute("role", employee.getRole());
            model.addAttribute("id", employee.getId());
        } else {
            model.addAttribute("name", "foo bar");
            model.addAttribute("role", "cat");
            model.addAttribute("id", -1);
        }

        return "employees-id";
    }

    @PostMapping("/employees")
    public String newEmployee(@ModelAttribute Employee newEmployee) throws URISyntaxException {
        repository.save(newEmployee);

        return "redirect:/employees";
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) throws URISyntaxException {
        Employee updatedEmployee = repository.findById(id)
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    return repository.save(employee);
                })
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return repository.save(newEmployee);
                });

        EntityModel<Employee> model = assembler.toModel(updatedEmployee);

        return ResponseEntity
                .created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
