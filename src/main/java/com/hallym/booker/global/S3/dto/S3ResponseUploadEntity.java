package com.hallym.booker.global.S3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class S3ResponseUploadEntity {
    String imageName;
    String imageUrl;
}
