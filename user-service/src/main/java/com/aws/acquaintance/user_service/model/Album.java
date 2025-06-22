package com.aws.acquaintance.user_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Album {

    //TODO: remove this class

    private String id;
    private String title;
    private String userId;
    private String description;
    private String albumUrl;
}
