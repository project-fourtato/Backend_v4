package com.hallym.booker.dto.Directmessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DirectmessageGetResponse {
    Long messageId;
    Long senderUid;
    Long recipientUid;
    LocalDateTime mdate;
    Integer mcheck;
    String mtitle;
    String mcontents;
}
