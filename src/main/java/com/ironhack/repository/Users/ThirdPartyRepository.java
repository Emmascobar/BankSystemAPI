package com.ironhack.repository.Users;
import com.ironhack.model.Users.ThirdParty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThirdPartyRepository extends JpaRepository<ThirdParty, Long> {

    ThirdParty findThirdPartyById(Long id);
}