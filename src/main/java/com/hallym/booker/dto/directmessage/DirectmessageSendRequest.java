package com.hallym.booker.dto.Directmessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DirectmessageSendRequest {
    Long senderUid;
    Long recipientUid;
    String mtitle;
    String mcontents;
}
