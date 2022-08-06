package com.redlian.demo.service;

import java.util.Date;
import java.util.List;

import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.redlian.demo.controller.dto.DemoDto;
import com.redlian.demo.entity.Currency;
import com.redlian.demo.entity.CurrencyDao;

@Service
public class CoinService {

	public CoinService() {
	}

	public CoinService(final CurrencyDao currencydao) {
		this.currencydao = currencydao;
	}

	@Autowired
	private CurrencyDao currencydao;

	public List<Currency> findAll() {
		return this.currencydao.findAll();
	}

	public Currency queryByName(final String name) {
		final Currency curbyname = this.currencydao.findByName(name);
		return curbyname;
	}

	public Currency addCoin() {
		final Currency cur = new Currency();
		cur.setName("比利時法郎");
		cur.setCoinCode("BEF");
		return this.currencydao.save(cur);
	}

	public Currency addCoin(DemoDto dto) {
		Currency cur = new Currency();
		cur.setName(dto.getName());
		cur.setCoinCode(dto.getCoinCode());
		cur.setLMD(DateUtils.formatDate(new Date(), "yyyy/MM/dd HH:mm:ss"));
		return this.currencydao.save(cur);
	}

	public Currency updateCoin() {
		final Currency upcur = this.currencydao.findByName("比利時法郎");
		upcur.setCoinCode("AAA");
		return this.currencydao.save(upcur);
	}

	public int updateMCoin(@RequestBody final DemoDto dto) {
		String lmd = DateUtils.formatDate(new Date(), "yyyy/MM/dd HH:mm:ss");
		final int res = this.currencydao.updateCoin(dto.getCoinCode(), dto.getName(), lmd);
		return res;
	}

	public Currency deleteCoin(final String name) {
		final Currency delcur = this.currencydao.findByName(name);
		this.currencydao.delete(delcur);
		return delcur;
	}

	public int deleteMCoin(final String name) {
		int del = this.currencydao.deleteCoin(name);
		return del;
	}

}
