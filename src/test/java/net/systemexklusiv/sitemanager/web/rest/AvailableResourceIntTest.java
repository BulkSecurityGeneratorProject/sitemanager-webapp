package net.systemexklusiv.sitemanager.web.rest;

import net.systemexklusiv.sitemanager.SiteManagerApp;
import net.systemexklusiv.sitemanager.domain.Available;
import net.systemexklusiv.sitemanager.repository.AvailableRepository;
import net.systemexklusiv.sitemanager.service.AvailableService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the AvailableResource REST controller.
 *
 * @see AvailableResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SiteManagerApp.class)
@WebAppConfiguration
@IntegrationTest
public class AvailableResourceIntTest {


    private static final LocalDate DEFAULT_BEGIN_OF_AVAILABILITY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BEGIN_OF_AVAILABILITY = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_OF_AVAILABILITY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_OF_AVAILABILITY = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private AvailableRepository availableRepository;

    @Inject
    private AvailableService availableService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAvailableMockMvc;

    private Available available;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AvailableResource availableResource = new AvailableResource();
        ReflectionTestUtils.setField(availableResource, "availableService", availableService);
        this.restAvailableMockMvc = MockMvcBuilders.standaloneSetup(availableResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        available = new Available();
        available.setBeginOfAvailability(DEFAULT_BEGIN_OF_AVAILABILITY);
        available.setEndOfAvailability(DEFAULT_END_OF_AVAILABILITY);
    }

    @Test
    @Transactional
    public void createAvailable() throws Exception {
        int databaseSizeBeforeCreate = availableRepository.findAll().size();

        // Create the Available

        restAvailableMockMvc.perform(post("/api/availables")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(available)))
                .andExpect(status().isCreated());

        // Validate the Available in the database
        List<Available> availables = availableRepository.findAll();
        assertThat(availables).hasSize(databaseSizeBeforeCreate + 1);
        Available testAvailable = availables.get(availables.size() - 1);
        assertThat(testAvailable.getBeginOfAvailability()).isEqualTo(DEFAULT_BEGIN_OF_AVAILABILITY);
        assertThat(testAvailable.getEndOfAvailability()).isEqualTo(DEFAULT_END_OF_AVAILABILITY);
    }

    @Test
    @Transactional
    public void checkBeginOfAvailabilityIsRequired() throws Exception {
        int databaseSizeBeforeTest = availableRepository.findAll().size();
        // set the field null
        available.setBeginOfAvailability(null);

        // Create the Available, which fails.

        restAvailableMockMvc.perform(post("/api/availables")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(available)))
                .andExpect(status().isBadRequest());

        List<Available> availables = availableRepository.findAll();
        assertThat(availables).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndOfAvailabilityIsRequired() throws Exception {
        int databaseSizeBeforeTest = availableRepository.findAll().size();
        // set the field null
        available.setEndOfAvailability(null);

        // Create the Available, which fails.

        restAvailableMockMvc.perform(post("/api/availables")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(available)))
                .andExpect(status().isBadRequest());

        List<Available> availables = availableRepository.findAll();
        assertThat(availables).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAvailables() throws Exception {
        // Initialize the database
        availableRepository.saveAndFlush(available);

        // Get all the availables
        restAvailableMockMvc.perform(get("/api/availables?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(available.getId().intValue())))
                .andExpect(jsonPath("$.[*].beginOfAvailability").value(hasItem(DEFAULT_BEGIN_OF_AVAILABILITY.toString())))
                .andExpect(jsonPath("$.[*].endOfAvailability").value(hasItem(DEFAULT_END_OF_AVAILABILITY.toString())));
    }

    @Test
    @Transactional
    public void getAvailable() throws Exception {
        // Initialize the database
        availableRepository.saveAndFlush(available);

        // Get the available
        restAvailableMockMvc.perform(get("/api/availables/{id}", available.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(available.getId().intValue()))
            .andExpect(jsonPath("$.beginOfAvailability").value(DEFAULT_BEGIN_OF_AVAILABILITY.toString()))
            .andExpect(jsonPath("$.endOfAvailability").value(DEFAULT_END_OF_AVAILABILITY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAvailable() throws Exception {
        // Get the available
        restAvailableMockMvc.perform(get("/api/availables/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAvailable() throws Exception {
        // Initialize the database
        availableService.save(available);

        int databaseSizeBeforeUpdate = availableRepository.findAll().size();

        // Update the available
        Available updatedAvailable = new Available();
        updatedAvailable.setId(available.getId());
        updatedAvailable.setBeginOfAvailability(UPDATED_BEGIN_OF_AVAILABILITY);
        updatedAvailable.setEndOfAvailability(UPDATED_END_OF_AVAILABILITY);

        restAvailableMockMvc.perform(put("/api/availables")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAvailable)))
                .andExpect(status().isOk());

        // Validate the Available in the database
        List<Available> availables = availableRepository.findAll();
        assertThat(availables).hasSize(databaseSizeBeforeUpdate);
        Available testAvailable = availables.get(availables.size() - 1);
        assertThat(testAvailable.getBeginOfAvailability()).isEqualTo(UPDATED_BEGIN_OF_AVAILABILITY);
        assertThat(testAvailable.getEndOfAvailability()).isEqualTo(UPDATED_END_OF_AVAILABILITY);
    }

    @Test
    @Transactional
    public void deleteAvailable() throws Exception {
        // Initialize the database
        availableService.save(available);

        int databaseSizeBeforeDelete = availableRepository.findAll().size();

        // Get the available
        restAvailableMockMvc.perform(delete("/api/availables/{id}", available.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Available> availables = availableRepository.findAll();
        assertThat(availables).hasSize(databaseSizeBeforeDelete - 1);
    }
}
