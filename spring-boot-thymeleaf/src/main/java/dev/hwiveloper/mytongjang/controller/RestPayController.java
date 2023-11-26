package dev.hwiveloper.mytongjang.controller;

import dev.hwiveloper.mytongjang.domain.Pay;
import dev.hwiveloper.mytongjang.dto.PayReserv;
import dev.hwiveloper.mytongjang.repository.PayRepository;
import dev.hwiveloper.mytongjang.util.ChiperUtil;
import dev.hwiveloper.mytongjang.util.DateUtil;
import dev.hwiveloper.mytongjang.util.SignatureUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RestPayController {

    private final PayRepository payRepo;

    @PostMapping(value = "/payReserv")
    public PayReserv payReserv(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String encrypt_key = "SETTLEBANKISGOODSETTLEBANKISGOOD";

        String hdInfo = request.getParameter("hdInfo");
        String apiVer = request.getParameter("apiVer");
        String processType = request.getParameter("processType");
        String mercntId = request.getParameter("mercntId");
        String ordNo = request.getParameter("ordNo");
        String trPrice = request.getParameter("trPricePlain");
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

        // 주문 정보 저장
        Pay order = Pay.builder()
                .ordNo(ordNo)
                .mercntId(mercntId)
                .trPrice(Long.parseLong(trPrice))
                .productNm(productNm)
                .trDay(trDay)
                .trTime(trTime)
                .signature(hashValue)
                .mercntParam1(mercntParam1)
                .mercntParam2(mercntParam2)
                .viewType(viewType)
                .build();
        payRepo.save(order);

        return payReserv;
    }

    @RequestMapping(value = "/payAction")
    public String paymentAction(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String encrypt_key = "SETTLEBANKISGOODSETTLEBANKISGOOD";

        String hdInfo = request.getParameter("hdInfo");
        String apiVer = request.getParameter("apiVer");
        String mercntId = request.getParameter("mercntId");
        String authNo = request.getParameter("authNo");
        String reqDay = DateUtil.currentDateString();
        String reqTime = DateUtil.currentTimeString();
        String ordNo = request.getParameter("ordNo");

        String signature = SignatureUtil.sha256(mercntId+authNo+reqDay+reqTime+encrypt_key);

        //요청 파라메터
        JSONObject reqParam = new JSONObject();
        reqParam.put("hdInfo", hdInfo);
        reqParam.put("apiVer", apiVer);
        reqParam.put("mercntId", mercntId);
        reqParam.put("authNo", authNo);
        reqParam.put("reqDay", reqDay);
        reqParam.put("reqTime", reqTime);
        reqParam.put("signature", signature);

        // 응답 파라메터
        JSONObject responseJSON = null;

        RestTemplate rt = new RestTemplate();
        ResponseEntity<JSONObject> payApprovResponse = rt.exchange(
                "https://tbezauthapi.settlebank.co.kr/APIPayApprov.do",
                    HttpMethod.POST,
                    new HttpEntity<>(reqParam),
                    JSONObject.class);
        responseJSON = payApprovResponse.getBody();

        Pay pay = payRepo.findByOrdNo(ordNo);
        if ("0".equals(responseJSON.get("resultCd").toString())) {
            // 성공시 후처리
            pay.setPayResultCd("0");
            pay.setTrNo(responseJSON.get("trNo").toString());
            payRepo.save(pay);
        } else {
            // 실패시 후처리
            pay.setPayResultCd(responseJSON.get("resultCd").toString());
            pay.setPayResultMsg(responseJSON.get("resultMsg").toString());
            payRepo.save(pay);
        }

        model.addAttribute("result", responseJSON);

        return "comm/result";
    }
}
