package com.hallym.booker.dto.Directmessage;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class GetDirectmessageResponse {
    Long messageId;
    Long senderUid;
    Long recipientUid;
    LocalDateTime mdate;
    Integer mcheck;
    String mtitle;
    String mcontents;
}
