package com.hallym.booker.dto.Directmessage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DirectmessageResponseDTO {
    private Long messageId;
    private Long senderUid;
    private Long recipientUid;
    private LocalDateTime mdate;
    private Integer mcheck;
    private String mtitle;
    private String mcontents;
    private String nickname;
    private String userimageUrl;
    private String userimageName;
}
