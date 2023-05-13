package com.dumitrascuantonio.auctionapp.repository;

import com.dumitrascuantonio.auctionapp.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByEmail(String email);

    @Query("SELECT u FROM User u")
    List<User> findFirstWithPageable(Pageable pageable);

    List<User> findAllByUsernameContains(String keyword);

}
