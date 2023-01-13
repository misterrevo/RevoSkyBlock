package com.revo.skyblock.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Island {

    private Long id;
    private Region region;
    private User owner;
    private List<User> members;
}
