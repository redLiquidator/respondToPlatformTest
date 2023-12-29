package com.ext;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import go.kr.dsp.api.ChecksumModule;
import go.kr.dsp.api.CryptoModule;

@RestController
public class EncryptController {

	String strKey = "rAKr1kf+NERW2TG+CmgKN/k9JOu0nct9aPXnHzp0RQ4=";

	@GetMapping("/test1")
	public void dotest() {
		System.out.println("dotest++++");

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("ID", "09dkf98dn");
		jsonObject.put("NM", "김서울");
		jsonObject.put("BRDT", "2004-12-03");
		jsonObject.put("MBL_NO", "01099998888");
		jsonObject.put("INSP_RSVT_NO", "100002");
		jsonObject.put("INSP_ADDR", "제주특별자치도 제주시 청사로 59");
		jsonObject.put("API_KEY", "87we98rn8ennr7wmm98emj798we7");
		jsonObject.put("ENC_KEY_VERSION", "2.1");
		jsonObject.put("INS_CODE", "11");

		/*
		 * 1.공통코드조회 POST
		 * https://api.openservice.go.kr/dev/I0000002/CarInscRsrvSrvc/retrieveMoisCmmCd
		 * Accept: application/json Content-Type: application/json Enc-key-version: 2
		 * Ins-Code: I0000008 Response-Code: 0 {"data":
		 * "IVf4T6yI80xKDqDbZmEMkz5iEJDcaA2EHBymA3I45D0CGSiHmxCs+w34AkJSZ99p6NSWxygFgIvtHkudInuutGTsV+gkdOuoqf3oz+dV2J753oWGcG27LWRLS8cEuGQs"}
		 */

		String result = encrypt(jsonObject.toString());
		System.out.println(result);
		System.out.println(decrypt(result));
	}

	@PostMapping(value = "/userinfo")
	public String userinfo(@RequestHeader Map<String, String> header, @RequestBody HashMap<String, String> body) {
		System.out.println("userinfo***" + header);
		System.out.println("userinfo***" + body);

		String encVersion = header.get("enc-key-version");
		System.out.println("controller Enc-Key-Version 값:" + Code.encVer);
		System.out.println("전달된 Enc-Key-Version 값:" + encVersion);

		// version 검증
		if (encVersion.equals(Code.encVer)) {
			System.out.println("Enc-Key-Version 이 일치");

			String encData = body.get("data");
			// 복호화
			String res = decrypt(encData);
			System.out.println("decrypt 결과1: " + res);

			// json 파싱
			JSONObject jsonObject = new JSONObject(res);
			jsonObject.put(Code.RESPONSE_CODE, "200");
			System.out.println("jsonObject: " + jsonObject);
			return encrypt(jsonObject.toString());

		} else {
			System.out.println("Enc-Key-Version 이 불일치");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(Code.RESPONSE_CODE, "500");
			System.out.println("jsonObject: " + jsonObject);
			return encrypt(jsonObject.toString());
		}
	}

	public String checksum(String message) {
		ChecksumModule checksumModule = new ChecksumModule();
		String checksumResult = checksumModule.checksum(message);

		return checksumResult;
	}

	public String encrypt(String data) {
		CryptoModule cryptoModule = new CryptoModule();
		String encryptResult = cryptoModule.encrypt(strKey, data);

		return encryptResult;
	}

	public String decrypt(String encryptResult) {
		CryptoModule cryptoModule = new CryptoModule();
		String decryptResult = cryptoModule.decrypt(strKey, encryptResult);

		return decryptResult;
	}
}
