package com.hallym.booker.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class LibraryList {

    @Id
    private String libCode;
    private String libName;
}