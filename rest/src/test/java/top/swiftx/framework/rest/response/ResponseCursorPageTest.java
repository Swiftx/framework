package top.swiftx.framework.rest.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResponseCursorPageTest {

    @Test
    void createsCursorMetadataFromPageValues() {
        CursorPageResponse<String, String> response =
                CursorPageResponse.of(List.of("one"), 20, "next", 7L, "rv-42", null);

        assertEquals(List.of("one"), response.data());
        assertEquals(20, response.meta().limit());
        assertTrue(response.meta().hasNext());
        assertEquals("next", response.meta().nextCursor());
        assertEquals(7L, response.meta().remaining());
        assertEquals("rv-42", response.meta().snapshot());
        assertNull(response.meta().total());
        assertInstanceOf(java.io.Serializable.class, response);
        assertInstanceOf(java.io.Serializable.class, response.meta());
    }

    @Test
    void omitsAbsentOptionalMetadataFromJson() throws Exception {
        CursorPageResponse<String, String> response =
                CursorPageResponse.of(List.of(), 20, null, null, null, null);

        String json = new ObjectMapper().writeValueAsString(response);

        assertEquals(
                "{\"meta\":{\"limit\":20,\"hasNext\":false},\"data\":[]}",
                json
        );
        assertFalse(response.meta().hasNext());
    }

    @Test
    void defensivelyCopiesData() {
        ArrayList<String> mutable = new ArrayList<>(List.of("one"));
        CursorPageResponse<String, String> response =
                CursorPageResponse.of(mutable, 20, null, null, null, null);

        mutable.add("two");

        assertEquals(List.of("one"), response.data());
        assertThrows(UnsupportedOperationException.class, () -> response.data().add("two"));
    }
}
