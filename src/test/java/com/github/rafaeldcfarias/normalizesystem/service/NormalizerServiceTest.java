package com.github.rafaeldcfarias.normalizesystem.service;

import com.github.rafaeldcfarias.normalizesystem.dao.JobTitleRepository;
import com.github.rafaeldcfarias.normalizesystem.model.JobTitle;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.apache.commons.text.similarity.SimilarityScore;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class NormalizerServiceTest {

    @Mock
    JobTitleRepository jobTitleRepository;

    @Spy
    SimilarityScore<Double> similarityScore = new JaroWinklerSimilarity();

    @InjectMocks
    NormalizerService normalizerService;


    Set<JobTitle> jobTitles = Set.of(
            new JobTitle("Architect"),
            new JobTitle("Software engineer"),
            new JobTitle("Quantity surveyor"),
            new JobTitle("Accountant")
    );

    @Test()
    void shouldThrowIllegalArgumentException() {
        var input = " ";
        assertThrows(IllegalArgumentException.class, () -> normalizerService.normalize(input), NormalizerService.NOT_VALID_EXCEPTION_MESSAGE);
    }

    @Test()
    void shouldNormalizeFromJava_engineerToSoftware_engineer() {
        var input = "Java engineer";
        Mockito.when(jobTitleRepository.findAllDistinct()).thenReturn(jobTitles);
        String normalized = normalizerService.normalize(input);
        assertEquals("Software engineer", normalized);
    }

    @Test()
    void shouldNormalizeFromCsharp_engineerToSoftware_engineer() {
        var input = "C# engineer";
        Mockito.when(jobTitleRepository.findAllDistinct()).thenReturn(jobTitles);
        String normalized = normalizerService.normalize(input);
        assertEquals("Software engineer", normalized);
    }

    @Test()
    void shouldNormalizeFromAccountantToAccount() {
        var input = "Accountant";
        Mockito.when(jobTitleRepository.findAllDistinct()).thenReturn(jobTitles);
        String normalized = normalizerService.normalize(input);
        assertEquals("Accountant", normalized);
    }

    @Test()
    void shouldNormalizeFromChief_AccountantToAccount() {
        var input = "Chief Accountant";
        Mockito.when(jobTitleRepository.findAllDistinct()).thenReturn(jobTitles);
        String normalized = normalizerService.normalize(input);
        assertEquals("Accountant", normalized);
    }

    @Test()
    void shouldNormalizeFromChief_ArchitectToArchitect() {
        var input = "Chief Architect";
        Mockito.when(jobTitleRepository.findAllDistinct()).thenReturn(jobTitles);
        String normalized = normalizerService.normalize(input);
        assertEquals("Architect", normalized);
    }

    @Test()
    void shouldNormalizeFromArchitectToArchitect() {
        var input = "Architect";
        Mockito.when(jobTitleRepository.findAllDistinct()).thenReturn(jobTitles);
        String normalized = normalizerService.normalize(input);
        assertEquals("Architect", normalized);
    }

    @Test()
    void shouldNormalizeFromQuantity_surveyorToQuantity_Surveyor() {
        var input = "Quantity surveyor";
        Mockito.when(jobTitleRepository.findAllDistinct()).thenReturn(jobTitles);
        String normalized = normalizerService.normalize(input);
        assertEquals("Quantity surveyor", normalized);
    }

    @Test()
    void shouldNormalizeFromQuantity_surveyor_auxiliarToQuantity_Surveyor() {
        var input = "Quantity surveyor auxiliar";
        Mockito.when(jobTitleRepository.findAllDistinct()).thenReturn(jobTitles);
        String normalized = normalizerService.normalize(input);
        assertEquals("Quantity surveyor", normalized);
    }

    @Test
    void shouldReturnHundredPercentSimilar() {
        var input = "abcde";
        Double percentOfSimilarity = normalizerService.measureSimilarity(input, new JobTitle(input));
        assertEquals(1d, percentOfSimilarity);
    }

    @Test
    void shouldReturnZeroPercentOfSimilarity() {
        var input = "abcde";
        Double percentOfSimilarity = normalizerService.measureSimilarity(input, new JobTitle("zyxwv"));
        assertEquals(0d, percentOfSimilarity);
    }

    @Test
    void shouldReturnFiftyPercentOrMoreOfSimilarity() {
        var input = "abcdefghij";
        Double percentOfSimilarity = normalizerService.measureSimilarity(input, new JobTitle("abcdezyxwv"));
        MatcherAssert.assertThat(percentOfSimilarity, Matchers.greaterThanOrEqualTo(.5d));
    }

}