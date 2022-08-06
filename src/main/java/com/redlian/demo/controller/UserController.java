package com.redlian.demo.controller;

import java.util.List;

import javax.websocket.server.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.redlian.demo.entity.Currency;
import com.redlian.demo.entity.CurrencyDao;
import com.redlian.demo.entity.User;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired CurrencyDao currencydao;
    
    @GetMapping("dao")
    public List<Currency> listAll(){
    	return currencydao.findAll();
    }
    
    @GetMapping("aaa")
    public ResponseEntity<String> getAAA() {
        final ResponseEntity<String> res = new ResponseEntity<String>("aaa", HttpStatus.OK);
        return res;
    }

    @PostMapping("post")
    // curl -X POST localhost:8080/api/user/post -d {"key":"value", "key":"value"} -H "Content-Type: application/json"
    // curl --request POST localhost:8080/api/user/post --data '{"key":"value", "key":"value"}' --header "Content-Type: application/json"
    public ResponseEntity<String> getPost() {
        System.out.println("post");
        logger.debug("postMapping");
        final ResponseEntity<String> res = new ResponseEntity<String>("postmapping", HttpStatus.OK);
        return res;
    }

    @GetMapping("/test/{name}")
    public ResponseEntity<User> getTest(@PathVariable("name") final String name) {
        final User u = new User();
        u.setId("00");
        u.setName(name);
        return new ResponseEntity<User>(u, HttpStatus.OK);
    }

    @GetMapping("/testapi")
    public ResponseEntity<User> getUserApi(@RequestParam(value = "userName", defaultValue = "") final String name,
            @RequestParam(value = "time", defaultValue = "") final String time) {
        final User u = new User();
        u.setId("00");
        u.setName(name);
        u.setTime(time);
        //return new ResponseEntity<User>(u, HttpStatus.OK);
        return ResponseEntity.ok().body(u);
    }

    @GetMapping("/testdata")
    public ResponseEntity<User> getUserdata(@RequestBody final User u) {
        return ResponseEntity.ok().body(u);
    }

}
