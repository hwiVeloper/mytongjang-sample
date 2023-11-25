package dev.hwiveloper.mytongjang.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PayReserv {
    private String resCode;
    private String trPriceEnc;
    private String emailEnc;
    private String cphoneNoEnc;
    private String signature;
    private String trDay;
    private String trTime;
    private String viewType;
}
