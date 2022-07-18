package com.github.rafaeldcfarias.normalizesystem.model;

import java.util.Objects;

public class JobTitle{
    private String description;

    public JobTitle(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobTitle jobTitle = (JobTitle) o;
        return description.equals(jobTitle.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }
}
