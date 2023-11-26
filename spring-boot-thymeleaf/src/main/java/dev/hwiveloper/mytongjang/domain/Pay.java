package dev.hwiveloper.mytongjang.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@ToString
@Table(indexes = {@Index(name = "idx_pay_ord_no", columnList = "ord_no")})
public class Pay {
    // 결제인증
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="ord_no")
    private String ordNo;
    private String mercntId;
    private Long trPrice;
    private String productNm;
    private String trDay;
    private String trTime;
    private String signature;
    private String mercntParam1;
    private String mercntParam2;
    private String viewType;

    // 결제
    private String authNo;
    private String payResultCd;
    private String payResultMsg;
    private String trNo;

    // 취소
    private String cancelOrdNo;
    private String cancelResultCd;
    private String cancelResultMsg;
    private String cancelTrNo;

    private String payStatus; // O, A, P, C
}
