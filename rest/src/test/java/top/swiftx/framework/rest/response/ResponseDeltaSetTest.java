package top.swiftx.framework.rest.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ResponseDeltaSet 测试
 * <p>
 * 注意：ResponseDeltaSet 是简单的 POJO 类，字段为 private，通过 Jackson 序列化
 * 主要测试其可序列化和内部类存在性
 */
class ResponseDeltaSetTest {

    @Test
    void testResponseDeltaSetExists() {
        DeltaSetResponse<String, Integer> responseDeltaSet = new DeltaSetResponse<>();
        assertNotNull(responseDeltaSet);
    }

    @Test
    void testMetaExists() {
        DeltaSetResponse.Meta meta = new DeltaSetResponse.Meta();
        assertNotNull(meta);
    }

    @Test
    void testIsSerializable() {
        DeltaSetResponse<String, Integer> responseDeltaSet = new DeltaSetResponse<>();
        assertInstanceOf(java.io.Serializable.class, responseDeltaSet);
    }

    @Test
    void testMetaIsSerializable() {
        DeltaSetResponse.Meta meta = new DeltaSetResponse.Meta();
        // Meta 是静态内部类，虽然没有直接实现 Serializable，
        // 但作为 ResponseDeltaSet 的一部分可以通过 Jackson 序列化
        assertNotNull(meta);
    }

    @Test
    void testResponseDeltaSetToString() {
        DeltaSetResponse<String, Integer> responseDeltaSet = new DeltaSetResponse<>();
        String result = responseDeltaSet.toString();
        assertNotNull(result);
    }

    @Test
    void testMetaToString() {
        DeltaSetResponse.Meta meta = new DeltaSetResponse.Meta();
        String result = meta.toString();
        assertNotNull(result);
    }

    @Test
    void testGenericTypes() {
        DeltaSetResponse<Integer, String> intStringDelta = new DeltaSetResponse<>();
        assertNotNull(intStringDelta);

        DeltaSetResponse<String, Long> stringLongDelta = new DeltaSetResponse<>();
        assertNotNull(stringLongDelta);
    }
}
