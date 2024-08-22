package com.hallym.booker.dto.Follow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class LatestJournalsResponse {
    private String toUserId;
    private Long jid;
    private LocalDateTime pdatetime;
    private String ptitle;
    private String pcontents;
    private String nickname;
    private String userimageUrl;
    private String userimageName;

    @Builder
    public LatestJournalsResponse(String toUserId, Long jid, LocalDateTime pdatetime, String ptitle, String pcontents, String nickname, String userimageUrl, String userimageName) {
        this.toUserId = toUserId;
        this.jid = jid;
        this.pdatetime = pdatetime;
        this.ptitle = ptitle;
        this.pcontents = pcontents;
        this.nickname = nickname;
        this.userimageUrl = userimageUrl;
        this.userimageName = userimageName;
    }
}
