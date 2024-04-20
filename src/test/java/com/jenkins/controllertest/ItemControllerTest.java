package com.jenkins.controllertest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jenkins.controller.ItemController;
import com.jenkins.entity.Item;
import com.jenkins.exception.ItemNotFoundException;
import com.jenkins.repo.Itemrepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(ItemController.class)

public class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Itemrepo itemRepository;

    @InjectMocks
    private ItemController itemController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        // Inject the mocked dependencies
        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
    }

    @Test
    public void testGetById_Success() throws Exception {
        // Arrange
        long itemId = 1L;
        String name = "item";
        Item item = new Item(itemId,name);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        // Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/items/{id}", itemId))
                .andExpect(status().isOk()) // Expecting status 200
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assert
        String jsonResponse = result.getResponse().getContentAsString();
        Item actualItem = objectMapper.readValue(jsonResponse, Item.class);
        assertEquals(item, actualItem);
    }

    @Test
    void getById_WhenItemDoesNotExist_ThrowsException() {
        // Arrange
        Long itemId = 1L;
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());
        // Act & Assert
        Throwable exception = org.junit.jupiter.api.Assertions.assertThrows(ItemNotFoundException.class, () -> {
            itemController.getById(itemId);
        });
        assertEquals("could not find item " + itemId, exception.getMessage());
        assertTrue(true);

    }
}
