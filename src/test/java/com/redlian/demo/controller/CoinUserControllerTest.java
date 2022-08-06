package com.redlian.demo.controller;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.redlian.demo.entity.Currency;
import com.redlian.demo.service.CoinService;

//@SpringBootTest
public class CoinUserControllerTest {

	@Autowired
	private CoinService coinservice;

	@Test
	public void findAll() {
		final List<Currency> listAll = this.coinservice.findAll();
		listAll.forEach(cur -> System.out.println("Test findAll: " + cur));
	}

	@Test
	public void addCoinTest() {
		final Currency cur = this.coinservice.addCoin();
		System.out.println("Test addCoinTest: " + cur);
	}

	@Test
	public void testTime() {
//	     * the offset and zone if available, such as '2011-12-03T10:15:30',
		ZonedDateTime zdt = ZonedDateTime.parse("2022-08-06T13:13:00+00:00");
		;
		System.out.println(zdt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
		// "time": {
//    		"updated": "Aug 6, 2022 13:13:00 UTC",
//    		"updatedISO": "2022-08-06T13:13:00+00:00",
//    		"updateduk": "Aug 6, 2022 at 14:13 BST"
//    		},
	}
	// @Test
	// public void setCoinTest() {
	// final Currency cur = this.coinservice.updateCoin();
	// System.out.println("Test setCoinTest: " + cur);
	// }
	//
	// @Test
	// public void delCoinTest() {
	// final Currency cur = this.coinservice.deleteCoin("比利時法郎");
	// System.out.println("Test delCoinTest: " + cur);
	// }

}
