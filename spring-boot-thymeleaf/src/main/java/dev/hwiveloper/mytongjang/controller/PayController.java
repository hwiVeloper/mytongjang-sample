package dev.hwiveloper.mytongjang.controller;

import dev.hwiveloper.mytongjang.util.DateUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 
 * @author firstkhw
 * @date : 2019. 2. 25.
 * 통합인증 결제 관련 샘플 호출
 *
 */
@Controller
@Slf4j
public class PayController {

	/**
	 * 기본 화면 로드
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model, HttpServletRequest request) {
		String mercntId = "ms00003t";
		model.addAttribute("mercntId", mercntId);
		model.addAttribute("ordNo", "SAMPLE_" + DateUtil.getDateTimeMillisecond());

		model.addAttribute("baseUrl", request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort());

		model.addAttribute("productNm", "테스트 상품");
		return "index";
	}

}
