package com.hallym.booker.dto.Directmessage;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DirectmessageSenderRequest {
    Long recipientUid;
    String mtitle;
    String mcontents;
}
