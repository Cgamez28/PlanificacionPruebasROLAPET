package com.rolapet.catalog;

import org.junit.jupiter.api.*;
import org.mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

class CatalogServiceTest {

    @Mock
    CatalogRepository repo;

    CatalogService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new CatalogService(repo);
    }

    @Test
    void shouldReturnItemsWhenRepoHasItems() {
        Item casco = new Item("1", "Casco", 10000);
        Item luz = new Item("2", "Luz", 5000);
        Mockito.when(repo.findAll()).thenReturn(List.of(casco, luz));

        var result = service.getAllItems();

        assertThat(result).hasSize(2);
        Mockito.verify(repo).findAll();
    }

    @Test
    void shouldThrowWhenAddingItemWithoutName() {
        Item invalid = new Item("x", "", 100);
        assertThatThrownBy(() -> service.addItem(invalid))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Name required");
        Mockito.verify(repo, Mockito.never()).save(Mockito.any());
    }

    @Test
    void shouldSaveItemWhenValid() {
        Item item = new Item("3", "Guantes", 15000);
        Mockito.when(repo.save(item)).thenReturn(item);

        var result = service.addItem(item);

        assertThat(result.getName()).isEqualTo("Guantes");
        Mockito.verify(repo).save(item);
    }
}
