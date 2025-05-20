# Sistema de Gestión de Proyectos Académicos

Proyecto académico de microservicios desarrollado para la asignatura de Ingeniería de Software II.

---

## Arquitectura general

Este sistema está compuesto por varios microservicios independientes:

- **coordination-service**: Gestión de proyectos (aprobar, rechazar, asignar).
- **student-service**: Gestión de estudiantes y postulaciones a proyectos.
- **login-service**: Autenticación y autorización de usuarios (empresas, coordinadores, estudiantes).
- **project-service**: Gestión avanzada de proyectos.
- **company-service**: Gestión de empresas.
- **RabbitMQ**: Comunicación asíncrona entre microservicios (mensajería de eventos).

Cada microservicio tiene su **propia base de datos** independiente.

---

## 🚀 Tecnologías utilizadas

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- Spring Web (REST)
- RabbitMQ
- Docker y Docker Compose
- Maven


