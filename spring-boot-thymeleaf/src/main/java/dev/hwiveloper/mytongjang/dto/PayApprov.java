package dev.hwiveloper.mytongjang.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PayApprov {
    private String resultCd;
    private String errCd;
    private String resultMsg;
    private String trNo;
}
