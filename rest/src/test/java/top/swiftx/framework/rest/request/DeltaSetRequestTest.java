package top.swiftx.framework.rest.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class DeltaSetRequestTest {

    private DeltaSetRequest request;

    @BeforeEach
    void setUp() {
        request = new DeltaSetRequest();
    }

    @Test
    void testDefaultValues() {
        assertNull(request.getApiVersion());
        assertNull(request.getLastRequested());
    }

    @Test
    void testSettersAndGetters() {
        Date now = new Date();
        request.setApiVersion("1.0");
        request.setLastRequested(now);

        assertEquals("1.0", request.getApiVersion());
        assertEquals(now, request.getLastRequested());
    }

    @Test
    void testApiVersionOnly() {
        request.setApiVersion("2.0");
        assertEquals("2.0", request.getApiVersion());
        assertNull(request.getLastRequested());
    }

    @Test
    void testLastRequestedOnly() {
        Date date = new Date();
        request.setLastRequested(date);
        assertEquals(date, request.getLastRequested());
        assertNull(request.getApiVersion());
    }

    @Test
    void testDifferentApiVersions() {
        request.setApiVersion("1.0.0");
        assertEquals("1.0.0", request.getApiVersion());

        request.setApiVersion("2.1.3-beta");
        assertEquals("2.1.3-beta", request.getApiVersion());
    }

    @Test
    void testPastDate() {
        Date pastDate = new Date(System.currentTimeMillis() - 10000000);
        request.setLastRequested(pastDate);
        assertEquals(pastDate, request.getLastRequested());
    }

    @Test
    void testFutureDate() {
        Date futureDate = new Date(System.currentTimeMillis() + 10000000);
        request.setLastRequested(futureDate);
        assertEquals(futureDate, request.getLastRequested());
    }

    @Test
    void testEmptyApiVersion() {
        request.setApiVersion("");
        assertEquals("", request.getApiVersion());
    }

    @Test
    void testNullApiVersion() {
        request.setApiVersion(null);
        assertNull(request.getApiVersion());
    }

    @Test
    void testIsSerializable() {
        assertInstanceOf(java.io.Serializable.class, request);
    }

    @Test
    void testToString() {
        request.setApiVersion("1.0");
        Date now = new Date();
        request.setLastRequested(now);

        String result = request.toString();
        assertNotNull(result);
        assertTrue(result.contains("DeltaSetRequest"));
    }

    @Test
    void testEquals() {
        Date now = new Date();

        DeltaSetRequest r1 = new DeltaSetRequest();
        r1.setApiVersion("1.0");
        r1.setLastRequested(now);

        DeltaSetRequest r2 = new DeltaSetRequest();
        r2.setApiVersion("1.0");
        r2.setLastRequested(now);

        assertEquals(r1, r2);
    }

    @Test
    void testHashCode() {
        Date now = new Date();

        DeltaSetRequest r1 = new DeltaSetRequest();
        r1.setApiVersion("1.0");
        r1.setLastRequested(now);

        DeltaSetRequest r2 = new DeltaSetRequest();
        r2.setApiVersion("1.0");
        r2.setLastRequested(now);

        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void testNotEqualsDifferentApiVersion() {
        Date now = new Date();

        DeltaSetRequest r1 = new DeltaSetRequest();
        r1.setApiVersion("1.0");
        r1.setLastRequested(now);

        DeltaSetRequest r2 = new DeltaSetRequest();
        r2.setApiVersion("2.0");
        r2.setLastRequested(now);

        assertNotEquals(r1, r2);
    }
}
