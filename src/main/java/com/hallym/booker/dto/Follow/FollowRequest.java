package com.hallym.booker.dto.Follow;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FollowRequest {
    private String toUserId;
    private String fromUserId;
}
