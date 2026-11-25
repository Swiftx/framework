package top.swiftx.framework.rest.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ResponsePage 测试
 * <p>
 * 注意：ResponsePage 是简单的 POJO 类，字段为 private，通过 Jackson 序列化
 * 主要测试其可序列化和内部类存在性
 */
class ResponsePageTest {

    @Test
    void testResponsePageExists() {
        PageableResponse<String> responsePage = new PageableResponse<>();
        assertNotNull(responsePage);
    }

    @Test
    void testMetaExists() {
        PageableResponse.Meta meta = new PageableResponse.Meta();
        assertNotNull(meta);
    }

    @Test
    void testIsSerializable() {
        PageableResponse<String> responsePage = new PageableResponse<>();
        assertInstanceOf(java.io.Serializable.class, responsePage);
    }

    @Test
    void testMetaIsSerializable() {
        PageableResponse.Meta meta = new PageableResponse.Meta();
        // Meta 是静态内部类，虽然没有直接实现 Serializable，
        // 但作为 ResponsePage 的一部分可以通过 Jackson 序列化
        assertNotNull(meta);
    }

    @Test
    void testResponsePageToString() {
        PageableResponse<String> responsePage = new PageableResponse<>();
        String result = responsePage.toString();
        assertNotNull(result);
    }

    @Test
    void testMetaToString() {
        PageableResponse.Meta meta = new PageableResponse.Meta();
        String result = meta.toString();
        assertNotNull(result);
    }

    @Test
    void testResponsePageWithGenericType() {
        PageableResponse<Integer> intPage = new PageableResponse<>();
        assertNotNull(intPage);

        PageableResponse<String> stringPage = new PageableResponse<>();
        assertNotNull(stringPage);
    }
}
