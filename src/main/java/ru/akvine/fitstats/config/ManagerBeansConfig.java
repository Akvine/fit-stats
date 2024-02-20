package ru.akvine.fitstats.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.akvine.fitstats.controllers.rest.converter.parser.Parser;
import ru.akvine.fitstats.enums.ConverterType;
import ru.akvine.fitstats.enums.FileType;
import ru.akvine.fitstats.enums.Macronutrient;
import ru.akvine.fitstats.enums.StatisticType;
import ru.akvine.fitstats.managers.*;
import ru.akvine.fitstats.services.processors.format.Converter;
import ru.akvine.fitstats.services.processors.macronutrient.MacronutrientProcessor;
import ru.akvine.fitstats.services.processors.statistic.main.StatisticProcessor;
import ru.akvine.fitstats.validators.file.FileValidator;

import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Configuration
public class ManagerBeansConfig {

    @Bean
    public MacronutrientProcessorsManager macronutrientProcessorsManager(List<MacronutrientProcessor> macronutrientProcessors) {
        Map<Macronutrient, MacronutrientProcessor> availableMacronutrientsProcessors = macronutrientProcessors
                .stream()
                .collect(toMap(MacronutrientProcessor::getType, identity()));
        return new MacronutrientProcessorsManager(availableMacronutrientsProcessors);
    }

    @Bean
    public StatisticProcessorsManager statisticProcessorsManager(List<StatisticProcessor> statisticProcessors) {
        Map<StatisticType, StatisticProcessor> availableStatisticProcessors = statisticProcessors
                .stream()
                .collect(toMap(StatisticProcessor::getType, identity()));
        return new StatisticProcessorsManager(availableStatisticProcessors);
    }

    @Bean
    public ParsersManager parsersManager(List<Parser> parsers) {
        Map<ConverterType, Parser> availableParsers = parsers
                .stream()
                .collect(toMap(Parser::getType, identity()));
        return new ParsersManager(availableParsers);
    }

    @Bean
    public ConvertersManager convertersManager(List<Converter> converters) {
        Map<ConverterType, Converter> availableConverters = converters
                .stream()
                .collect(toMap(Converter::getType, identity()));
        return new ConvertersManager(availableConverters);
    }

    @Bean
    public FileValidatorsManager fileValidatorsManager(List<FileValidator> fileValidators) {
        Map<FileType, FileValidator> availableFileValidators = fileValidators
                .stream()
                .collect(toMap(FileValidator::getType, identity()));
        return new FileValidatorsManager(availableFileValidators);
    }
}
