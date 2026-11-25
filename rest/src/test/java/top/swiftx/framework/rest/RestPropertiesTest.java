package top.swiftx.framework.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * REST 配置属性测试。
 *
 * @author gpt-5.6-sol
 */
class RestPropertiesTest {

    private RestProperties restProperties;

    @BeforeEach
    void setUp() {
        restProperties = new RestProperties();
    }

    @Test
    void testDefaultValues() {
        assertTrue(restProperties.isEnabled());
        assertNull(restProperties.getCors());
    }

    @Test
    void testEnabledSetter() {
        restProperties.setEnabled(false);
        assertFalse(restProperties.isEnabled());

        restProperties.setEnabled(true);
        assertTrue(restProperties.isEnabled());
    }

    @Test
    void testCorsSetter() {
        Map<String, RestProperties.CORS> corsMap = new HashMap<>();
        RestProperties.CORS corsConfig = new RestProperties.CORS();
        corsMap.put("/api/**", corsConfig);

        restProperties.setCors(corsMap);
        var actual = configuredCors();
        assertEquals(1, actual.size());
        assertEquals(corsConfig, actual.get("/api/**"));
    }

    @Test
    void testCorsClassDefaultValues() {
        RestProperties.CORS cors = new RestProperties.CORS();

        assertTrue(cors.isEnabled());
        assertEquals("*", cors.getAllowedMethods());
        assertEquals("*", cors.getAllowedOrigins());
        assertEquals("*", cors.getAllowedHeaders());
    }

    @Test
    void testCorsClassSetters() {
        RestProperties.CORS cors = new RestProperties.CORS();

        cors.setEnabled(false);
        assertFalse(cors.isEnabled());

        cors.setAllowedMethods("GET,POST");
        assertEquals("GET,POST", cors.getAllowedMethods());

        cors.setAllowedOrigins("https://example.com");
        assertEquals("https://example.com", cors.getAllowedOrigins());

        cors.setAllowedHeaders("Content-Type,Authorization");
        assertEquals("Content-Type,Authorization", cors.getAllowedHeaders());
    }

    @Test
    void testCorsClassFullConfiguration() {
        RestProperties.CORS cors = new RestProperties.CORS();
        cors.setEnabled(true);
        cors.setAllowedMethods("GET,POST,PUT,DELETE");
        cors.setAllowedOrigins("https://api.example.com");
        cors.setAllowedHeaders("Content-Type,Authorization,X-Requested-With");

        assertTrue(cors.isEnabled());
        assertEquals("GET,POST,PUT,DELETE", cors.getAllowedMethods());
        assertEquals("https://api.example.com", cors.getAllowedOrigins());
        assertEquals("Content-Type,Authorization,X-Requested-With", cors.getAllowedHeaders());
    }

    @Test
    void testMultipleCorsConfigurations() {
        Map<String, RestProperties.CORS> corsMap = new HashMap<>();

        RestProperties.CORS apiCors = new RestProperties.CORS();
        apiCors.setAllowedOrigins("https://api.example.com");
        corsMap.put("/api/**", apiCors);

        RestProperties.CORS adminCors = new RestProperties.CORS();
        adminCors.setAllowedOrigins("https://admin.example.com");
        corsMap.put("/admin/**", adminCors);

        restProperties.setCors(corsMap);

        var actual = configuredCors();
        assertEquals(2, actual.size());
        assertEquals(
                "https://api.example.com",
                Objects.requireNonNull(actual.get("/api/**")).getAllowedOrigins()
        );
        assertEquals(
                "https://admin.example.com",
                Objects.requireNonNull(actual.get("/admin/**")).getAllowedOrigins()
        );
    }

    @Test
    void testCorsWithDisabledEntry() {
        Map<String, RestProperties.CORS> corsMap = new HashMap<>();

        RestProperties.CORS enabledCors = new RestProperties.CORS();
        enabledCors.setEnabled(true);
        corsMap.put("/public/**", enabledCors);

        RestProperties.CORS disabledCors = new RestProperties.CORS();
        disabledCors.setEnabled(false);
        corsMap.put("/private/**", disabledCors);

        restProperties.setCors(corsMap);

        var actual = configuredCors();
        assertTrue(Objects.requireNonNull(actual.get("/public/**")).isEnabled());
        assertFalse(Objects.requireNonNull(actual.get("/private/**")).isEnabled());
    }

    /**
     * 获取测试已设置的 CORS 配置。
     *
     * @return 非空 CORS 配置
     */
    private Map<String, RestProperties.CORS> configuredCors() {
        return Objects.requireNonNull(restProperties.getCors());
    }
}
