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
	// String sdata = "Temporary Message for Encrypt";
	String encVer = "1";

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
		jsonObject.put("RESPONSE_CODE", "200");

		/*
		 * wkl163M5GO959xITTp3rvvwQHPcJH1TfERKSwSRgLBWCx9LrlLVgjxZ4Wm5o1rmzwNgqXQOAibBYNs7WhSDUTVm1KOlKHKc
		 * +qCQShWaFAKxSfZW+qyuRjnv3F4A7ZD9sJQ/+rtyViuiM39OXedpbKzZb/uH8I9C8Z8m4WEHRJsbt
		 * /ENXV+85InA6xX1YyhF6yevCiNdf2Fxd5BLrL7vPbemWismKn12VEjmh+Hzarm7wa+
		 * uGq7WA1cK3tgwRGddCx4YjP01jWxRBXtsdBiw3O6wTuGIvVAKKTOlIG8Aq6Ffed5BVHCcIdPBr2Cl3jF
		 * /VHVfJ/J9bR+w1WvGehkCwsdL1CR8puBM+f7wBt4U7z8w=
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
		System.out.println("controller Enc-Key-Version 값:" + encVer);
		System.out.println("전달된 Enc-Key-Version 값:" + encVersion);

		// version 검증
		if (encVersion.equals(encVer)) {
			System.out.println("Enc-Key-Version 이 일치");
			
			String encData = body.get("data");
			// 복호화
			String res = decrypt(encData);
			System.out.println("decrypt 결과1: " + res);
			
			// json 파싱
			JSONObject jsonObject = new JSONObject(res);
			jsonObject.put("RESPONSE_CODE", "200");
			System.out.println("jsonObject: " + jsonObject);
			return encrypt(jsonObject.toString());

		} else {
			System.out.println("Enc-Key-Version 이 불일치");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("RESPONSE_CODE", "500");
			System.out.println("jsonObject: "+ jsonObject);
			return encrypt(jsonObject.toString());
		}
	}

	public String checksum(String message) {
		System.out.println("message " + message);
		ChecksumModule checksumModule = new ChecksumModule();
		String checksumResult = checksumModule.checksum(message);
		System.out.println("checksumResult " + checksumResult);

		return checksumResult;
	}

	public String encrypt(String data) {

		CryptoModule cryptoModule = new CryptoModule();
		String encryptResult = cryptoModule.encrypt(strKey, data);
		// System.out.println("encryptResult " + encryptResult);

		return encryptResult;
	}

	public String decrypt(String encryptResult) {
		CryptoModule cryptoModule = new CryptoModule();

		String decryptResult = cryptoModule.decrypt(strKey, encryptResult);
		// System.out.println("decryptResult " + decryptResult);

		return decryptResult;
	}
}
