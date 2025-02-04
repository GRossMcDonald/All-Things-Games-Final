package com.wcci.final_project.dto;

import java.util.List;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserPayload {

    private String email;

    private List<Long> reviewIds;

    public UserPayload() {
        this.reviewIds = new ArrayList<>();
    }

    public UserPayload(String email) {
        this.email = email;
        this.reviewIds = new ArrayList<>();
    }
}
