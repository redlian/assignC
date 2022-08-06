package com.redlian.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "Currency")
public class Currency {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private Integer id;

	@Column(name = "Name", length = 10, unique = true)
	private String name;

	@Column(name = "CoinCode", length = 3)
	private String coinCode;

	@Column(name = "lastModifyDate")
	private String LMD;

}
