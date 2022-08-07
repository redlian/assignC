package com.redlian.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.redlian.demo.controller.dto.DemoDto;
import com.redlian.demo.entity.Currency;
import com.redlian.demo.entity.CurrencyDao;
import com.redlian.demo.service.CoinService;

@SpringBootTest
public class CoinUserControllerTest {

	@Autowired
	private CoinService coinservice;

	@MockBean
	private CurrencyDao currencydao;

	@Autowired
	private CoinController coinContorllerA;

	private Currency data() {
		Currency a = new Currency();
		a.setName("NAME_A");
		a.setCoinCode("AAA");
		return a;
	}

	@Test
	public void findAll() {

		List<Currency> curList = new ArrayList<>();
		curList.add(data());
		Mockito.when(currencydao.findAll()).thenReturn(curList);
		final List<Currency> listAll = this.coinservice.findAll();
		assertEquals(coinservice.findAll(), curList);
		listAll.forEach(cur -> System.out.println("Test findAll: " + cur));
	}

	@Test
	public void addCoinTest() {
		DemoDto dto = new DemoDto();
		dto.setCoinCode("AAA");
		dto.setName("Name");
		Mockito.when(coinservice.addCoin(dto)).thenReturn(data());
		assertEquals(coinservice.addCoin(dto), data());
	}

	@Test
	public void testTime() {
		ZonedDateTime zdt = ZonedDateTime.parse("2022-08-06T13:13:00+00:00");
		;
		System.out.println(zdt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
	}

	@Test
	public void setCoinTest() {
		Mockito.when(this.coinservice.updateCoin()).thenReturn(data());
		assertEquals(coinservice.updateCoin(), data());
	}

	@Test
	public void delCoinTest() {
		Mockito.when(this.coinservice.deleteCoin("AAA")).thenReturn(data());
		assertEquals(coinservice.deleteCoin("AAA"), data());
	}

	private RestTemplate restTemplate;

	@BeforeEach
	public void init() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		restTemplate = Mockito.mock(RestTemplate.class);

	}

	@Test
	public void restCoinApi() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		final String url = "https://api.coindesk.com/v1/bpi/currentprice.json";

		String data = "{\"time\":{\"updated\":\"Aug 7, 2022 06:00:00 UTC\",\"updatedISO\":\"2022-08-07T06:00:00+00:00\",\"updateduk\":\"Aug 7, 2022 at 07:00 BST\"},\"disclaimer\":\"This data was produced from the CoinDesk Bitcoin Price Index (USD). Non-USD currency data converted using hourly conversion rate from openexchangerates.org\",\"chartName\":\"Bitcoin\",\"bpi\":{\"USD\":{\"code\":\"USD\",\"symbol\":\"&#36;\",\"rate\":\"22,972.8249\",\"description\":\"United States Dollar\",\"rate_float\":22972.8249},\"GBP\":{\"code\":\"GBP\",\"symbol\":\"&pound;\",\"rate\":\"19,195.9087\",\"description\":\"British Pound Sterling\",\"rate_float\":19195.9087},\"EUR\":{\"code\":\"EUR\",\"symbol\":\"&euro;\",\"rate\":\"22,378.8855\",\"description\":\"Euro\",\"rate_float\":22378.8855}}}";
		restTemplate.setRequestFactory(getClientHttpRequestFactory());
		Mockito.when(restTemplate.getForObject(url, String.class)).thenReturn(data);
		assertEquals(restTemplate.getForObject(url, String.class), data);

	}

	private HttpComponentsClientHttpRequestFactory getClientHttpRequestFactory()
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
//		CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier())
//				.build();
//		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
//		requestFactory.setHttpClient(httpClient);
//		requestFactory.setConnectTimeout(60000);
//		requestFactory.setReadTimeout(60000);
		////
//		final SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
//		clientHttpRequestFactory.setConnectTimeout(60000);
//		return clientHttpRequestFactory;
		//
		TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
		SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("https", sslsf).register("http", new PlainConnectionSocketFactory()).build();

		BasicHttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager(
				socketFactoryRegistry);
		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf)
				.setConnectionManager(connectionManager).build();

		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
		return requestFactory;
	}

}
