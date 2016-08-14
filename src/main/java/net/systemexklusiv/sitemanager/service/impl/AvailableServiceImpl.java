package net.systemexklusiv.sitemanager.service.impl;

import net.systemexklusiv.sitemanager.service.AvailableService;
import net.systemexklusiv.sitemanager.domain.Available;
import net.systemexklusiv.sitemanager.repository.AvailableRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Available.
 */
@Service
@Transactional
public class AvailableServiceImpl implements AvailableService{

    private final Logger log = LoggerFactory.getLogger(AvailableServiceImpl.class);
    
    @Inject
    private AvailableRepository availableRepository;
    
    /**
     * Save a available.
     * 
     * @param available the entity to save
     * @return the persisted entity
     */
    public Available save(Available available) {
        log.debug("Request to save Available : {}", available);
        Available result = availableRepository.save(available);
        return result;
    }

    /**
     *  Get all the availables.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Available> findAll() {
        log.debug("Request to get all Availables");
        List<Available> result = availableRepository.findAll();
        return result;
    }

    /**
     *  Get one available by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Available findOne(Long id) {
        log.debug("Request to get Available : {}", id);
        Available available = availableRepository.findOne(id);
        return available;
    }

    /**
     *  Delete the  available by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Available : {}", id);
        availableRepository.delete(id);
    }
}
