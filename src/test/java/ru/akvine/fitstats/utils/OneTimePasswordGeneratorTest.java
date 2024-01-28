package ru.akvine.fitstats.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("OneTimePasswordGenerator Test")
public class OneTimePasswordGeneratorTest {

    @DisplayName("Generated code is not blank")
    @Test
    public void generated_code_not_blank() {
        int length = 4;
        String code = OneTimePasswordGenerator.generate(length);

        assertThat(code).isNotBlank();
    }
}
