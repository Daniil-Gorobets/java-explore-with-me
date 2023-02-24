package ru.practicum.ewm.event.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Boolean existsByCategoryId(Long catId);

    List<Event> findAllByIdIn(List<Long> events);

    Optional<Event> findByIdAndState(Long id, EventState eventState);

    @Query("SELECT e " +
            "FROM Event AS e " +
            "WHERE " +
            "(" +
                ":text IS NULL " +
                "OR LOWER(e.description) LIKE CONCAT('%', :text, '%') " +
                "OR LOWER(e.annotation) LIKE CONCAT('%', :text, '%')" +
            ")" +
            "AND (:users IS NULL OR e.initiator.id IN (:users)) " +
            "AND (:states IS NULL OR e.state IN (:states)) " +
            "AND (:categories IS NULL OR e.category.id IN (:categories)) " +
            "AND (:paid IS NULL OR e.paid = :paid)" +
            "AND (:rangeStart IS NULL OR e.eventDate >= :rangeStart) " +
            "AND (:rangeEnd IS NULL OR e.eventDate <= :rangeEnd)")
    List<Event> findByParams(
            @Param("text") String text,
            @Param("users") List<Long> users,
            @Param("states") List<EventState> states,
            @Param("categories") List<Long> categories,
            @Param("paid") Boolean paid,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            PageRequest pageRequest);


}
