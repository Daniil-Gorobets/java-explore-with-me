package ru.practicum.ewm.request.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "participation_requests")
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "created")
    LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "event_id")
    Event event;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    User requester;

    @Column(name = "status")
    RequestStatus status;
}
