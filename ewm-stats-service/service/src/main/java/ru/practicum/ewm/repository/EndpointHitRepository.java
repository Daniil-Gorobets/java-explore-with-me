package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.model.EndpointHitModel;
import ru.practicum.ewm.model.ViewStatsModel;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EndpointHitRepository extends JpaRepository<EndpointHitModel, Long> {

    @Query(value = "SELECT new ru.practicum.ewm.model.ViewStatsModel(hits.app, hits.uri, COUNT(DISTINCT hits.ip)) " +
            "FROM EndpointHitModel as hits " +
            "WHERE (:uris IS NULL OR hits.uri IN :uris) " +
            "AND hits.timestamp >= :start " +
            "AND hits.timestamp <= :end " +
            "GROUP BY hits.app, hits.uri")
    List<ViewStatsModel> findViewStatsUniqueIp(
            @Param("uris") List<String> uris,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query(value = "SELECT new ru.practicum.ewm.model.ViewStatsModel(hits.app, hits.uri, COUNT(hits.ip)) " +
            "FROM EndpointHitModel as hits " +
            "WHERE (:uris IS NULL OR hits.uri IN :uris) " +
            "AND hits.timestamp >= :start " +
            "AND hits.timestamp <= :end " +
            "GROUP BY hits.app, hits.uri")
    List<ViewStatsModel> findViewStats(
            @Param("uris") List<String> uris,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}
