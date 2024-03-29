package org.example.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Account {
    private String id;
    private int money;

}
