package com.redlian.demo.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CurrencyDao extends JpaRepository<Currency, Integer> {

	public Currency findByName(String name);

	@Modifying
	@Transactional
	@Query("UPDATE Currency SET coin_code=:coincode ,last_modify_date=:lmd WHERE name=:name")
	public int updateCoin(@Param("coincode") String coincode, @Param("name") String name, @Param("lmd") String lmd);

	@Modifying
	@Transactional
	@Query("DELETE FROM Currency WHERE name=:name")
	public int deleteCoin(@Param("name") String name);
}
