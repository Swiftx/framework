package top.swiftx.framework.rest.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CursorPaginationTest {

    private CursorPagination pagination;

    @BeforeEach
    void setUp() {
        pagination = new CursorPagination();
    }

    @Test
    void testDefaultValues() {
        assertNull(pagination.getCursor());
        assertNull(pagination.getLimit());
    }

    @Test
    void testSettersAndGetters() {
        pagination.setCursor("abc123");
        pagination.setLimit(10);

        assertEquals("abc123", pagination.getCursor());
        assertEquals(10, pagination.getLimit());
    }

    @Test
    void testCursorOnly() {
        pagination.setCursor("xyz789");
        assertEquals("xyz789", pagination.getCursor());
        assertNull(pagination.getLimit());
    }

    @Test
    void testLimitOnly() {
        pagination.setLimit(20);
        assertEquals(20, pagination.getLimit());
        assertNull(pagination.getCursor());
    }

    @Test
    void testEmptyCursor() {
        pagination.setCursor("");
        assertEquals("", pagination.getCursor());
    }

    @Test
    void testNullCursor() {
        pagination.setCursor(null);
        assertNull(pagination.getCursor());
    }

    @Test
    void testCursorWithSpecialCharacters() {
        pagination.setCursor("abc@#$%123");
        assertEquals("abc@#$%123", pagination.getCursor());
    }

    @Test
    void testLargeLimit() {
        pagination.setLimit(1000);
        assertEquals(1000, pagination.getLimit());
    }

    @Test
    void testLimitOne() {
        pagination.setLimit(1);
        assertEquals(1, pagination.getLimit());
    }

    @Test
    void testIsSerializable() {
        assertInstanceOf(java.io.Serializable.class, pagination);
    }

    @Test
    void testToString() {
        pagination.setCursor("cursor123");
        pagination.setLimit(10);

        String result = pagination.toString();
        assertNotNull(result);
        assertTrue(result.contains("CursorPagination"));
    }

    @Test
    void testEquals() {
        CursorPagination p1 = new CursorPagination();
        p1.setCursor("abc");
        p1.setLimit(10);

        CursorPagination p2 = new CursorPagination();
        p2.setCursor("abc");
        p2.setLimit(10);

        assertEquals(p1, p2);
    }

    @Test
    void testHashCode() {
        CursorPagination p1 = new CursorPagination();
        p1.setCursor("abc");
        p1.setLimit(10);

        CursorPagination p2 = new CursorPagination();
        p2.setCursor("abc");
        p2.setLimit(10);

        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void testNotEqualsDifferentCursor() {
        CursorPagination p1 = new CursorPagination();
        p1.setCursor("abc");
        p1.setLimit(10);

        CursorPagination p2 = new CursorPagination();
        p2.setCursor("xyz");
        p2.setLimit(10);

        assertNotEquals(p1, p2);
    }

    @Test
    void testNotEqualsDifferentLimit() {
        CursorPagination p1 = new CursorPagination();
        p1.setCursor("abc");
        p1.setLimit(10);

        CursorPagination p2 = new CursorPagination();
        p2.setCursor("abc");
        p2.setLimit(20);

        assertNotEquals(p1, p2);
    }
}
