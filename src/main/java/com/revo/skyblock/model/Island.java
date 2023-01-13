package com.revo.skyblock.model;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Island {

    private Long id;
    private Region region;
    private User owner;
    @Builder.Default
    private List<User> members = new ArrayList<>();
}
