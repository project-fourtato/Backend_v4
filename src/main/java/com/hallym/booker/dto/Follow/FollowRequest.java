package com.hallym.booker.dto.Follow;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FollowRequest {
    private Long toUserId;
    private Long fromUserId;
}
