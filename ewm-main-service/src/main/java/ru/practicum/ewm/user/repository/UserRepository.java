package ru.practicum.ewm.user.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.user.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByEmailIgnoreCase(String name);

    List<User> findAllByIdIn(List<Long> ids, PageRequest pageRequest);

    boolean existsByEmail(String email);
}
