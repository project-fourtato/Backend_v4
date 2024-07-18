package com.hallym.booker.dto.LibraryList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LibraryListDTO {
    private String libName;
    private String address;
    private String tel;
    private String latitude; //위도
    private String longitude; //경도
    private String homepage;
    private String closed;
    private String operatingTime;
}
