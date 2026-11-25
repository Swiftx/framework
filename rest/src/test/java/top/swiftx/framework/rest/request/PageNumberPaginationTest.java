package top.swiftx.framework.rest.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PageNumberPaginationTest {

    private PageNumberPagination pagination;

    @BeforeEach
    void setUp() {
        pagination = new PageNumberPagination();
    }

    @Test
    void testDefaultValues() {
        assertNull(pagination.getPage());
        assertNull(pagination.getPageSize());
    }

    @Test
    void testSettersAndGetters() {
        pagination.setPage(1);
        pagination.setPageSize(10);

        assertEquals(1, pagination.getPage());
        assertEquals(10, pagination.getPageSize());
    }

    @Test
    void testPageZero() {
        pagination.setPage(0);
        assertEquals(0, pagination.getPage());
    }

    @Test
    void testNegativePage() {
        pagination.setPage(-1);
        assertEquals(-1, pagination.getPage());
    }

    @Test
    void testLargePageSize() {
        pagination.setPageSize(1000);
        assertEquals(1000, pagination.getPageSize());
    }

    @Test
    void testPageSizeOne() {
        pagination.setPageSize(1);
        assertEquals(1, pagination.getPageSize());
    }

    @Test
    void testConstructorWithParameters() {
        PageNumberPagination p = new PageNumberPagination();
        p.setPage(5);
        p.setPageSize(20);

        assertEquals(5, p.getPage());
        assertEquals(20, p.getPageSize());
    }

    @Test
    void testPageOnly() {
        pagination.setPage(3);
        assertEquals(3, pagination.getPage());
        assertNull(pagination.getPageSize());
    }

    @Test
    void testPageSizeOnly() {
        pagination.setPageSize(25);
        assertEquals(25, pagination.getPageSize());
        assertNull(pagination.getPage());
    }

    @Test
    void testIsSerializable() {
        assertInstanceOf(java.io.Serializable.class, pagination);
    }

    @Test
    void testToString() {
        pagination.setPage(1);
        pagination.setPageSize(10);

        String result = pagination.toString();
        assertNotNull(result);
        assertTrue(result.contains("PageNumberPagination"));
    }

    @Test
    void testEquals() {
        PageNumberPagination p1 = new PageNumberPagination();
        p1.setPage(1);
        p1.setPageSize(10);

        PageNumberPagination p2 = new PageNumberPagination();
        p2.setPage(1);
        p2.setPageSize(10);

        assertEquals(p1, p2);
    }

    @Test
    void testHashCode() {
        PageNumberPagination p1 = new PageNumberPagination();
        p1.setPage(1);
        p1.setPageSize(10);

        PageNumberPagination p2 = new PageNumberPagination();
        p2.setPage(1);
        p2.setPageSize(10);

        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void testNotEquals() {
        PageNumberPagination p1 = new PageNumberPagination();
        p1.setPage(1);
        p1.setPageSize(10);

        PageNumberPagination p2 = new PageNumberPagination();
        p2.setPage(2);
        p2.setPageSize(10);

        assertNotEquals(p1, p2);
    }
}
