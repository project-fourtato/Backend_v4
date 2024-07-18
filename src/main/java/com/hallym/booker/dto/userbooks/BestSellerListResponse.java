package com.hallym.booker.dto.userbooks;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BestSellerListResponse {
    List<BestSellerResponse> result;
}
