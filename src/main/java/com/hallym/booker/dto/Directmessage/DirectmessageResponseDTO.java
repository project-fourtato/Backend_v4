package com.hallym.booker.dto.Directmessage;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
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
