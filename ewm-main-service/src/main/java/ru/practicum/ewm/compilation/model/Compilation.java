package ru.practicum.ewm.compilation.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.event.model.Event;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "compilation_events",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private List<Event> events;

    @Column(name = "pinned")
    private Boolean pinned;

    @Column(name = "title", nullable = false, unique = true)
    private String title;
}
