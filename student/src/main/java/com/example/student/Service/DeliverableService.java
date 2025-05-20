
package com.example.student.Service;

import com.example.student.Entities.Deliverable;
import com.example.student.Repository.DeliverableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DeliverableService {

    @Autowired
    private DeliverableRepository deliverableRepository;

    @Autowired
    private com.example.student.Messaging.DeliverableEventPublisher publisher;

    public List<Deliverable> getAll() {
        return deliverableRepository.findAll();
    }

    public Deliverable create(Deliverable deliverable) {
        Deliverable saved = deliverableRepository.save(deliverable);
        publisher.sendDeliverable(saved);
        return saved;
    }

    public List<Deliverable> getByStudent(String studentId) {
        return deliverableRepository.findByStudentId(studentId);
    }

    public Deliverable markAsCompleted(UUID id) {
        Deliverable d = deliverableRepository.findById(id).orElseThrow();
        d.setCompleted(true);
        d.setSubmittedDate(java.time.LocalDate.now());
        Deliverable updated = deliverableRepository.save(d);
        publisher.sendDeliverable(updated);
        return updated;
    }

    public Deliverable addComment(UUID id, String comment) {
        Deliverable d = deliverableRepository.findById(id).orElseThrow();
        d.setComments(comment);
        Deliverable updated = deliverableRepository.save(d);
        publisher.sendDeliverable(updated);
        return updated;
    }
}
