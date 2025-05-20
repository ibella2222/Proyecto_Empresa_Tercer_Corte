
package com.example.student.Entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "deliverables")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Deliverable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String title;
    private String description;
    private String studentId;
    private String projectId;
    private boolean completed;

    private LocalDate dueDate;
    private LocalDate submittedDate;

    @Lob
    private String comments;

    private String fileUrl;
}
