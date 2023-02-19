package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.model.EndpointHitModel;
import ru.practicum.ewm.model.ViewStatsModel;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHitModel, Long> {

    @Query("SELECT new ru.practicum.ewm.model.ViewStatsModel(h.app, h.uri, COUNT(h.ip)) " +
            "FROM EndpointHitModel AS h " +
            "WHERE (h.timestamp >= :start) " +
            "AND (h.timestamp <= :end) " +
            "AND ((:uris) IS NULL OR h.uri IN (:uris))" +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.ip) DESC")
    List<ViewStatsModel> findViewStats(
            @Param("uris") List<String> uris,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query("SELECT new ru.practicum.ewm.model.ViewStatsModel(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM EndpointHitModel AS h " +
            "WHERE (h.timestamp >= :start) " +
            "AND (h.timestamp <= :end) " +
            "AND ((:uris) IS NULL OR h.uri IN (:uris))" +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.ip) DESC")
    List<ViewStatsModel> findViewStatsUniqueIp(
            @Param("uris") List<String> uris,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}
