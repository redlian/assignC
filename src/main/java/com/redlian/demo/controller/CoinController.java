package com.redlian.demo.controller;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.net.ssl.SSLContext;

import org.apache.http.client.utils.DateUtils;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.redlian.demo.contant.ErrorCode;
import com.redlian.demo.controller.dto.DemoDto;
import com.redlian.demo.controller.dto.DemoDtoRes;
import com.redlian.demo.entity.Currency;
import com.redlian.demo.entity.User;
import com.redlian.demo.exception.DemoException;
import com.redlian.demo.service.CoinResponse;
import com.redlian.demo.service.CoinResponse.CoinInfo;
import com.redlian.demo.service.CoinResponse.Times;
import com.redlian.demo.service.CoinService;
import com.redlian.demo.service.LCoinResponse;
import com.redlian.demo.service.NewCoinRes;

@RestController
@RequestMapping("/coin")
public class CoinController {
	private static final Logger logger = LoggerFactory.getLogger(CoinController.class);

	private static final String DATEFORMATPATTERN = "yyyy/MM/dd HH:mm:ss";
	@Autowired
	private CoinService coinservice;

	@GetMapping("queryall")
	public List<Currency> listAll() {
		return this.coinservice.findAll();
	}

	// localhost:8081/api/coin/query?name=XXX
	@GetMapping("query")
	public Currency listBy(@RequestParam(value = "name", defaultValue = "") final String name) {
		final Currency curbyname = this.coinservice.queryByName(name);
		return curbyname;
	}

	@GetMapping("add")
	public ResponseEntity<Currency> add(@RequestBody final DemoDto dto) {
		final Currency cur = this.coinservice.addCoin(dto);
		return ResponseEntity.ok().body(cur);
	}

	@GetMapping("update")
	public ResponseEntity<Currency> update() {
		final Currency upcur = this.coinservice.updateCoin();
		return ResponseEntity.ok().body(upcur);
	}

	@GetMapping("Mupdate")
	public ResponseEntity<Integer> mUpdate(@RequestBody final DemoDto dto) {
		final int index = this.coinservice.updateMCoin(dto);
		return ResponseEntity.ok().body(index);
	}

	@GetMapping("del")
	public ResponseEntity<Integer> delete(@RequestParam(value = "name", defaultValue = "") final String name) {
		final int upcur = this.coinservice.deleteMCoin(name);
		return ResponseEntity.ok(upcur);
	}

	@GetMapping(value = "priceinfo")
	public ResponseEntity<List<NewCoinRes>> priceInfo()
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		ResponseEntity<DemoDtoRes> responseEntity = new ResponseEntity<DemoDtoRes>(HttpStatus.BAD_REQUEST);
		List<NewCoinRes> newResList = new ArrayList<NewCoinRes>();

		String strDate = DateUtils.formatDate(new Date(), "yyyy-MM-DD HH:mm:ss");
		logger.info("priceInfo start: {}", strDate);

		List<Currency> listCur = this.coinservice.findAll();
		Map<String, Object> map = new HashMap<>();
		for (Currency s : listCur) {
			map.put(s.getCoinCode(), s.getName());
		}

		final String url = "https://api.coindesk.com/v1/bpi/currentprice.json";
		final RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
		restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());

		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		final HttpEntity<?> httpEntity = new HttpEntity<>(headers);
		try {
			ResponseEntity<String> coindeskEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity,
					String.class);
			Gson g = new Gson();
			LCoinResponse res = g.fromJson(coindeskEntity.getBody(), LCoinResponse.class);

			String updateISO = res.getTime().getUpdatedISO();
			ZonedDateTime zdt = ZonedDateTime.parse(updateISO);
			String lmd = zdt.format(DateTimeFormatter.ofPattern(DATEFORMATPATTERN));

			Map<String, com.redlian.demo.service.LCoinResponse.CoinInfo> bpi = res.getBpi();
			logger.info("responseEntity={}", responseEntity);
			logger.info("{}", res);

			for (Entry<String, com.redlian.demo.service.LCoinResponse.CoinInfo> entry : bpi.entrySet()) {
				NewCoinRes ncr = new NewCoinRes();
				ncr.setCoinCode(entry.getKey());
				ncr.setName(ObjectUtils.nullSafeToString(map.get(entry.getKey())));
				ncr.setLmd(lmd);
				ncr.setRate(entry.getValue().getRate());
				boolean hasData = Optional.ofNullable(ncr).isPresent();
				if (hasData)
					newResList.add(ncr);
			}
		} catch (final ResourceAccessException e) {
			logger.error("DemoException:{}", e);
			throw new DemoException(ErrorCode.CONNEC_ERROR, e);
		} catch (final IllegalArgumentException e) {
			logger.error("Exceptio:{}", e);
			throw new IllegalArgumentException(e);
		}
		return responseEntity.ok(newResList);
	}

	@GetMapping(value = "rest")
	public ResponseEntity<LCoinResponse> restApi()
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		String strDate = DateUtils.formatDate(new Date(), "yyyy-MM-DD HH:mm:ss");
		System.out.println(strDate);
//        final String url = "http://localhost:8081/api/coin/testres";
		final String url = "https://api.coindesk.com/v1/bpi/currentprice.json";
		final RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		final HttpEntity<?> httpEntity = new HttpEntity<>(headers);
		try {
			final ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity,
					String.class);
			restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
			Gson g = new Gson();
			LCoinResponse res = g.fromJson(responseEntity.getBody(), LCoinResponse.class);
			res.getBpi();
			logger.info("responseEntity={}", responseEntity);
			logger.info("{}", res);
			return ResponseEntity.ok().body(res);
		} catch (final ResourceAccessException e) {
			logger.error("DemoException:{}", e);
			throw new DemoException(ErrorCode.CONNEC_ERROR, e);
		} catch (final IllegalArgumentException e) {
			logger.error("Exceptio:{}", e);
			throw new IllegalArgumentException(e);
		}
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

	@GetMapping("demoApi")
	public ResponseEntity<String> getDemo() {
		logger.info("logger demoApi");
		final ResponseEntity<String> res = new ResponseEntity<String>("Response demoApi", HttpStatus.OK);
		return res;
	}

	@PostMapping("post")
	// curl -X POST localhost:8080/api/user/post -d {"key":"value", "key":"value"}
	// -H "Content-Type: application/json"
	// curl --request POST localhost:8080/api/user/post --data '{"key":"value",
	// "key":"value"}' --header "Content-Type: application/json"
	public ResponseEntity<String> getPost() {
		System.out.println("post");
		logger.debug("postMapping");
		final ResponseEntity<String> res = new ResponseEntity<String>("postmapping", HttpStatus.OK);
		return res;
	}

	// Optional by using required attribute
	@GetMapping("test","/test/{name}")
	public ResponseEntity<User> getTest(@PathVariable("name") final String name) {
		final User u = new User();
		u.setId("00");
		u.setName(name);
		return new ResponseEntity<User>(u, HttpStatus.OK);
	}

	// localhost:8080/api/coin/testapi?userName=???&time=???
	@GetMapping("/testapi")
	public ResponseEntity<User> getUserApi(@RequestParam(value = "userName", defaultValue = "") final String name,
			@RequestParam(value = "time", defaultValue = "") final String time) {
		final User u = new User();
		u.setId("00");
		u.setName(name);
		u.setTime(time);
		// return new ResponseEntity<User>(u, HttpStatus.OK);
		return ResponseEntity.ok().body(u);
	}

	@GetMapping("/testdata")
	public ResponseEntity<User> getUserdata(@RequestBody final User u) {
		return ResponseEntity.ok().body(u);
	}

	@GetMapping("/testres")
	public ResponseEntity<CoinResponse> getCoinRes() {
		final CoinResponse cres = new CoinResponse();
		final Times time = new Times();
		time.setUpdated("2000-01-01 11:11:11");
		cres.setTime(time);
//		final List<Map<String, CoinInfo>> bpi = new ArrayList<>();
		final Map<String, CoinInfo> bpi = new HashMap<>();
		final Map<String, CoinInfo> newCoin = new HashMap<>();
		final CoinInfo info = new CoinInfo();
		info.setCode("Unite State");
		newCoin.put("USD", info);
//		bpi.add(newCoin);
		bpi.put("USR", info);
		bpi.put("USRS", info);
		cres.setBpi(bpi);
		return ResponseEntity.ok().body(cres);
	}

}
