package net.systemexklusiv.sitemanager.repository;

import net.systemexklusiv.sitemanager.domain.Available;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Available entity.
 */
@SuppressWarnings("unused")
public interface AvailableRepository extends JpaRepository<Available,Long> {

    @Query("select available from Available available where available.user.login = ?#{principal.username}")
    List<Available> findByUserIsCurrentUser();

}
