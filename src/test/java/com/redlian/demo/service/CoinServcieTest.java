package com.redlian.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.redlian.demo.entity.Currency;
import com.redlian.demo.entity.CurrencyDao;

public class CoinServcieTest {

    @Test
    public void testGetList() {
        final String name = "比利時法郎";
        final Currency testCur = new Currency();
        testCur.setName("比利時法郎");
        testCur.setCoinCode("BEF");
        final CurrencyDao dao = Mockito.mock(CurrencyDao.class);
        Mockito.when(dao.findByName(name)).thenReturn(testCur);
        final CoinService service = new CoinService(dao);
        service.queryByName(name);
        assertEquals(testCur, service.queryByName(name));
    }
}
