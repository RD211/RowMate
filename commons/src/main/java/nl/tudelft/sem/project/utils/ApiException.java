package nl.tudelft.sem.project.utils;

import lombok.Data;

@Data
public class ApiException {

    private int status;

    private String description;

    public ApiException(int status, String description) {
        this.status = status;
        this.description = description;
    }
}
