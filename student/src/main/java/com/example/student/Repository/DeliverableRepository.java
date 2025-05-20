
package com.example.student.Repository;

import com.example.student.Entities.Deliverable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DeliverableRepository extends JpaRepository<Deliverable, UUID> {
    List<Deliverable> findByStudentId(String studentId);
    List<Deliverable> findByProjectId(String projectId);
}
