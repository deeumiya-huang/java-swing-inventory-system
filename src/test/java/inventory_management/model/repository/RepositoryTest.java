package inventory_management.model.repository;

import ictgradschool.industry.inventory_management.model.product.Product;
import ictgradschool.industry.inventory_management.model.repository.Repository;
import ictgradschool.industry.inventory_management.model.repository.RepositoryListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class RepositoryTest {
    private Repository repository;
    private File tempFile;
    private Product p1;
    private Product p2;

    @BeforeEach
    public void setUp(@TempDir Path tempDir) {
        tempFile = tempDir.resolve("test.dat").toFile();
        repository = new Repository(tempFile);

        p1 = new Product("0000000001","apple","yummy",1.5, 5);
        p2 = new Product("0000000002","banana","yummy",1, 2);
    }

    @Test
    public void testAddAndGetProduct() {
        repository.addProduct(p1);

        assertEquals(1, repository.size());
        assertEquals(p1, repository.getProduct("0000000001"));
        assertEquals(p1, repository.getProductAt(0));

        assertThrows(IllegalArgumentException.class,()-> {
            Product sameIdProduct = new Product("0000000001", "guava", "tasty", 2, 10);
            repository.addProduct(sameIdProduct);
        });
    }

    @Test
    public void testRemoveProductBy() {
        repository.addProduct(p1);
        repository.addProduct(p2);
        repository.removeProductBy("0000000001");

        assertEquals(1, repository.size());
        assertNull(repository.getProduct("0000000001"));
        assertEquals(p2, repository.getProductAt(0));
    }

    @Test
    public void testGetProductAt() {
        repository.addProduct(p1);
        // out of bound
        assertNull(repository.getProductAt(-1));
        assertNull(repository.getProductAt(1));
    }

    @Test
    public void testIterator() {
        repository.addProduct(p1);
        repository.addProduct(p2);

        int count = 0;
        for (Product product : repository) {
            assertNotNull(product);
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testNotifyListener() {
        RepositoryListener mockListener = mock(RepositoryListener.class);
        repository.addRepositoryListener(mockListener);

        repository.addProduct(p1);
        // verify repositoryHasChanged method in mockListener has been called for one time.
        verify(mockListener, times(1)).repositoryHasChanged(repository);

        repository.removeRepositoryListener(mockListener);

        repository.removeProductBy("0000000001");
        // because mockListener was removed from listeners, so the called time is still 1.
        verify(mockListener, times(1)).repositoryHasChanged(repository);
    }

    @Test
    public void testSaveAndLoadData() {
        repository.addProduct(p1);
        repository.addProduct(p2);

        assertDoesNotThrow(()-> repository.save());
        // open the same file in new Repository
        Repository newRepo = new Repository(tempFile);
        assertEquals(2, newRepo.loadData());
        assertEquals("0000000001", newRepo.getProductAt(0).getId());


    }

}
