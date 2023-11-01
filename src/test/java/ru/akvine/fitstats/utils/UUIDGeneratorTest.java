package ru.akvine.fitstats.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("UUID Generator Test")
class UUIDGeneratorTest {

    @Test
    @DisplayName("Test uuid with dashes")
    void test_uuid_with_dashes() {
        String uuid = UUIDGenerator.uuid();

        assertThat(uuid).isNotBlank();
        assertThat(uuid.contains("-")).isTrue();
    }

    @Test
    @DisplayName("Test uuid defined length")
    void test_uuid_defined_length() {
        int length = 10;
        String uuid = UUIDGenerator.uuid(length);

        assertThat(uuid).isNotBlank();
        assertThat(uuid.length()).isEqualTo(length);
    }

    @Test
    @DisplayName("Test uuid without dashes and defined length")
    void test_uuid_without_dashes_and_defined_length() {
        int length = 5;
        String uuid = UUIDGenerator.uuidWithoutDashes(length);

        assertThat(uuid).isNotBlank();
        assertThat(uuid.contains("-")).isFalse();
        assertThat(uuid.length()).isEqualTo(length);
    }
}