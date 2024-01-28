package ru.akvine.fitstats.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("StringHelperTest Test")
public class StringHelperTest {

    @DisplayName("Split by delimiters - case 1")
    @Test
    public void split_by_delimiters_cas1() {
        String value = "Lorem ipsu, with some values, text for test";
        List<String> delimiters = List.of(",");

        List<String> splitted = StringHelper.splitter(value, delimiters);

        assertThat(splitted).isNotNull();
        assertThat(splitted.size()).isEqualTo(5);
    }

    @DisplayName("Split by delimiters - case 2")
    @Test
    public void split_by_delimiters_case2() {
        String value = "Lorem ipsu, with some values, text for test";
        List<String> delimiters = List.of(",", "text");

        List<String> splitted = StringHelper.splitter(value, delimiters);

        assertThat(splitted).isNotNull();
        assertThat(splitted.size()).isEqualTo(6);
    }
}
