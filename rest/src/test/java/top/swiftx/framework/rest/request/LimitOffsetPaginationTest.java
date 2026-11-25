package top.swiftx.framework.rest.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LimitOffsetPaginationTest {

    private LimitOffsetPagination pagination;

    @BeforeEach
    void setUp() {
        pagination = new LimitOffsetPagination();
    }

    @Test
    void testDefaultValues() {
        assertNull(pagination.getLimit());
        assertNull(pagination.getOffset());
    }

    @Test
    void testSettersAndGetters() {
        pagination.setLimit(10);
        pagination.setOffset(0);

        assertEquals(10, pagination.getLimit());
        assertEquals(0, pagination.getOffset());
    }

    @Test
    void testLimitZero() {
        pagination.setLimit(0);
        assertEquals(0, pagination.getLimit());
    }

    @Test
    void testOffsetZero() {
        pagination.setOffset(0);
        assertEquals(0, pagination.getOffset());
    }

    @Test
    void testNegativeOffset() {
        pagination.setOffset(-1);
        assertEquals(-1, pagination.getOffset());
    }

    @Test
    void testLargeLimit() {
        pagination.setLimit(1000);
        assertEquals(1000, pagination.getLimit());
    }

    @Test
    void testLargeOffset() {
        pagination.setOffset(10000);
        assertEquals(10000, pagination.getOffset());
    }

    @Test
    void testLimitOnly() {
        pagination.setLimit(25);
        assertEquals(25, pagination.getLimit());
        assertNull(pagination.getOffset());
    }

    @Test
    void testOffsetOnly() {
        pagination.setOffset(50);
        assertEquals(50, pagination.getOffset());
        assertNull(pagination.getLimit());
    }

    @Test
    void testIsSerializable() {
        assertInstanceOf(java.io.Serializable.class, pagination);
    }

    @Test
    void testToString() {
        pagination.setLimit(10);
        pagination.setOffset(5);

        String result = pagination.toString();
        assertNotNull(result);
        assertTrue(result.contains("LimitOffsetPagination"));
    }

    @Test
    void testEquals() {
        LimitOffsetPagination p1 = new LimitOffsetPagination();
        p1.setLimit(10);
        p1.setOffset(0);

        LimitOffsetPagination p2 = new LimitOffsetPagination();
        p2.setLimit(10);
        p2.setOffset(0);

        assertEquals(p1, p2);
    }

    @Test
    void testHashCode() {
        LimitOffsetPagination p1 = new LimitOffsetPagination();
        p1.setLimit(10);
        p1.setOffset(0);

        LimitOffsetPagination p2 = new LimitOffsetPagination();
        p2.setLimit(10);
        p2.setOffset(0);

        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void testNotEqualsDifferentLimit() {
        LimitOffsetPagination p1 = new LimitOffsetPagination();
        p1.setLimit(10);
        p1.setOffset(0);

        LimitOffsetPagination p2 = new LimitOffsetPagination();
        p2.setLimit(20);
        p2.setOffset(0);

        assertNotEquals(p1, p2);
    }

    @Test
    void testNotEqualsDifferentOffset() {
        LimitOffsetPagination p1 = new LimitOffsetPagination();
        p1.setLimit(10);
        p1.setOffset(0);

        LimitOffsetPagination p2 = new LimitOffsetPagination();
        p2.setLimit(10);
        p2.setOffset(10);

        assertNotEquals(p1, p2);
    }
}
