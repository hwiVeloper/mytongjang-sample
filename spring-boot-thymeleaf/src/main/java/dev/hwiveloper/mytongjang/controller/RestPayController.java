package dev.hwiveloper.mytongjang.controller;

import dev.hwiveloper.mytongjang.dto.PayReserv;
import dev.hwiveloper.mytongjang.util.ChiperUtil;
import dev.hwiveloper.mytongjang.util.DateUtil;
import dev.hwiveloper.mytongjang.util.SignatureUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestPayController {
    @PostMapping(value = "/payReserv")
    public PayReserv payReserv(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String encrypt_key = "SETTLEBANKISGOODSETTLEBANKISGOOD";

        String hdInfo = request.getParameter("hdInfo");
        String apiVer = request.getParameter("apiVer");
        String processType = request.getParameter("processType");
        String mercntId = request.getParameter("mercntId");
        String ordNo = request.getParameter("ordNo");
        String trPrice = request.getParameter("trPrice");
        String productNm = request.getParameter("productNm");
        String dutyFreeYn = request.getParameter("dutyFreeYn");
        String addtionalDeductionType = request.getParameter("addtionalDeductionType");
        String shopNm = request.getParameter("shopNm");
        String cphoneNo = request.getParameter("cphoneNo");
        String email = request.getParameter("email");
        String callbackUrl = request.getParameter("callbackUrl");
        String returnUrl = request.getParameter("returnUrl");
        String cancelUrl = request.getParameter("cancelUrl");
        String mercntParam1 = request.getParameter("mercntParam1");
        String mercntParam2 = request.getParameter("mercntParam2");
        String viewType = request.getParameter("viewType");
        String trDay = DateUtil.currentDateString();
        String trTime = DateUtil.currentTimeString();

        String trPriceEnc = ChiperUtil.aesEncryptEcb(encrypt_key, trPrice);
        String cphoneNoEnc = "";
        if(cphoneNo != null && !cphoneNo.equals("")){
            cphoneNoEnc = ChiperUtil.aesEncryptEcb(encrypt_key, cphoneNo);
        }
        String emailEnc = "";
        if(email != null && !email.equals("")){
            emailEnc = ChiperUtil.aesEncryptEcb(encrypt_key, email);
        }

        String hashValue = SignatureUtil.sha256(mercntId+ordNo+trDay+trTime+trPrice+encrypt_key);

        //응답 파라메터

        PayReserv payReserv = PayReserv.builder()
                .resCode("0000")
                .trPriceEnc(trPriceEnc)
                .emailEnc(emailEnc)
                .cphoneNoEnc(cphoneNoEnc)
                .signature(hashValue)
                .trDay(trDay)
                .trTime(trTime)
                .viewType(viewType)
                .build();
//        resParam.put("resCode", "0000");
//        resParam.put("trPriceEnc", trPriceEnc);
//        resParam.put("emailEnc", emailEnc);
//        resParam.put("cphoneNoEnc", cphoneNoEnc);
//        resParam.put("signature", hashValue);
//        resParam.put("trDay", trDay);
//        resParam.put("trTime", trTime);
//        resParam.put("viewType", viewType);

        return payReserv;
    }
}
