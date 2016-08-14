package net.systemexklusiv.sitemanager.service;

import net.systemexklusiv.sitemanager.domain.Available;

import java.util.List;

/**
 * Service Interface for managing Available.
 */
public interface AvailableService {

    /**
     * Save a available.
     * 
     * @param available the entity to save
     * @return the persisted entity
     */
    Available save(Available available);

    /**
     *  Get all the availables.
     *  
     *  @return the list of entities
     */
    List<Available> findAll();

    /**
     *  Get the "id" available.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Available findOne(Long id);

    /**
     *  Delete the "id" available.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);
}
