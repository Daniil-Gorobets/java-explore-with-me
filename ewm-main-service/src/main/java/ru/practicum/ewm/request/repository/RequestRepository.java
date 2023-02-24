package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.request.model.ParticipationRequest;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {

    @Query("SELECT e.id, COUNT(pr.id) AS requests " +
            "FROM Event AS e " +
            "LEFT JOIN ParticipationRequest AS pr ON e.id = pr.event.id " +
            "WHERE e.id IN (:events) " +
            "GROUP BY e.id")
    List<Object[]> findParticipationRequestsWithStatNumberForEvents(
            @Param("events") List<Long> events);

    List<ParticipationRequest> findAllByEventIdAndEvent_InitiatorId(Long eventId, Long initiatorId);

    List<ParticipationRequest> findAllByIdIn(List<Long> requestIds);

    List<ParticipationRequest> findAllByRequesterId(Long requesterId);

    Boolean existsByRequesterIdAndEventId(Long requesterId, Long eventId);
}
