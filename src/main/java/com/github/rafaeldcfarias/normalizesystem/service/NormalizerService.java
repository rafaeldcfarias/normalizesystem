package com.github.rafaeldcfarias.normalizesystem.service;

import com.github.rafaeldcfarias.normalizesystem.dao.JobTitleRepository;
import com.github.rafaeldcfarias.normalizesystem.model.JobTitle;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.SimilarityScore;

import java.util.AbstractMap;
import java.util.Comparator;

public class NormalizerService {

    static final String NOT_VALID_EXCEPTION_MESSAGE = "Parameter should not be null, empty or without words.";

    private final JobTitleRepository jobTitleRepository;

    private final SimilarityScore<Double> similarityScore;

    public NormalizerService(JobTitleRepository jobTitleRepository, SimilarityScore<Double> similarityScore) {
        this.jobTitleRepository = jobTitleRepository;
        this.similarityScore = similarityScore;
    }

    public String normalize(String unnormalizedJobDescription) {
        if (StringUtils.isBlank(unnormalizedJobDescription))
            throw new IllegalArgumentException(NOT_VALID_EXCEPTION_MESSAGE);

        return jobTitleRepository.findAllDistinct().stream()
                .map(jTitle ->
                        new AbstractMap.SimpleImmutableEntry<JobTitle, Double>
                                (jTitle, measureSimilarity(unnormalizedJobDescription, jTitle))
                )
                .max(Comparator.comparingDouble(AbstractMap.SimpleImmutableEntry::getValue))
                .map(AbstractMap.SimpleImmutableEntry::getKey)
                .map(JobTitle::getDescription)
                .orElse(null);
    }

    public Double measureSimilarity(String unnormalizedJobDescription, JobTitle jTitle) {
        return similarityScore.apply(jTitle.getDescription(), unnormalizedJobDescription);
    }

}
