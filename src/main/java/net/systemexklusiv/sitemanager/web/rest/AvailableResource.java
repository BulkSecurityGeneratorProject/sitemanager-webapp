package net.systemexklusiv.sitemanager.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.systemexklusiv.sitemanager.domain.Available;
import net.systemexklusiv.sitemanager.service.AvailableService;
import net.systemexklusiv.sitemanager.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Available.
 */
@RestController
@RequestMapping("/api")
public class AvailableResource {

    private final Logger log = LoggerFactory.getLogger(AvailableResource.class);
        
    @Inject
    private AvailableService availableService;
    
    /**
     * POST  /availables : Create a new available.
     *
     * @param available the available to create
     * @return the ResponseEntity with status 201 (Created) and with body the new available, or with status 400 (Bad Request) if the available has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/availables",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Available> createAvailable(@Valid @RequestBody Available available) throws URISyntaxException {
        log.debug("REST request to save Available : {}", available);
        if (available.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("available", "idexists", "A new available cannot already have an ID")).body(null);
        }
        Available result = availableService.save(available);
        return ResponseEntity.created(new URI("/api/availables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("available", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /availables : Updates an existing available.
     *
     * @param available the available to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated available,
     * or with status 400 (Bad Request) if the available is not valid,
     * or with status 500 (Internal Server Error) if the available couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/availables",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Available> updateAvailable(@Valid @RequestBody Available available) throws URISyntaxException {
        log.debug("REST request to update Available : {}", available);
        if (available.getId() == null) {
            return createAvailable(available);
        }
        Available result = availableService.save(available);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("available", available.getId().toString()))
            .body(result);
    }

    /**
     * GET  /availables : get all the availables.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of availables in body
     */
    @RequestMapping(value = "/availables",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Available> getAllAvailables() {
        log.debug("REST request to get all Availables");
        return availableService.findAll();
    }

    /**
     * GET  /availables/:id : get the "id" available.
     *
     * @param id the id of the available to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the available, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/availables/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Available> getAvailable(@PathVariable Long id) {
        log.debug("REST request to get Available : {}", id);
        Available available = availableService.findOne(id);
        return Optional.ofNullable(available)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /availables/:id : delete the "id" available.
     *
     * @param id the id of the available to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/availables/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAvailable(@PathVariable Long id) {
        log.debug("REST request to delete Available : {}", id);
        availableService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("available", id.toString())).build();
    }

}
