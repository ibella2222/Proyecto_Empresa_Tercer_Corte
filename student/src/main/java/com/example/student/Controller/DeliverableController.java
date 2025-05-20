
package com.example.student.Controller;

import com.example.student.Entities.Deliverable;
import com.example.student.Service.DeliverableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/deliverables")
@CrossOrigin("*")
public class DeliverableController {

    @Autowired
    private DeliverableService service;

    @GetMapping
    public List<Deliverable> getAll() {
        return service.getAll();
    }

    @PostMapping
    public Deliverable create(@RequestBody Deliverable deliverable) {
        return service.create(deliverable);
    }

    @GetMapping("/student/{studentId}")
    public List<Deliverable> getByStudent(@PathVariable String studentId) {
        return service.getByStudent(studentId);
    }

    @PutMapping("/{id}/complete")
    public Deliverable complete(@PathVariable UUID id) {
        return service.markAsCompleted(id);
    }

    @PutMapping("/{id}/comment")
    public Deliverable comment(@PathVariable UUID id, @RequestBody String comment) {
        return service.addComment(id, comment);
    }
}
