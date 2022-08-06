package com.redlian.demo.service;

import java.util.Map;

import lombok.Data;

@Data
public class LCoinResponse {
	private Times time;

	@Data
	public static class Times {
		String updated;
		String updatedISO;
		String updateduk;

	}

	private String disclaimer;

	private String chartName;

	private Map<String, CoinInfo> bpi;

	@Data
	public static class CoinInfo {
		private String code;
		private String symbol;
		private String rate;
		private String description;
		private String rate_float;
	}

}
