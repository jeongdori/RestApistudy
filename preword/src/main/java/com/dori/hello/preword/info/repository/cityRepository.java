package com.dori.hello.preword.info.repository;

import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.dori.hello.preword.info.model.city;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class cityRepository {
	
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	private final cityRowMapper cityRowMapper;
		
	public cityRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
		this.cityRowMapper = new cityRowMapper();
	}
	
	public List<city> findList(){
		
		log.debug("findList query : {}", CitySql.SELECT);
		
		return namedParameterJdbcTemplate.query(CitySql.SELECT, EmptySqlParameterSource.INSTANCE, this.cityRowMapper);
	}
	
	public List<city> findByCountryCodeAndPopulation(String countryCode, int population){
		
		String qry = CitySql.SELECT
				+ CitySql.COUNTRY_CODE_CONDITION
				+ CitySql.POPULATION_CONDITION;

		SqlParameterSource param = new MapSqlParameterSource("countryCode", countryCode)
				.addValue("population", population);
		
		return namedParameterJdbcTemplate.query(qry, param, this.cityRowMapper);
	}
	
	public city insert(city city) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		SqlParameterSource parameterSource = new MapSqlParameterSource("name", city.getName())
				.addValue("countryCode", city.getCountryCode())
				.addValue("district", city.getDistrict())
				.addValue("population", city.getPopulation()); 
		
		int affectedRows = namedParameterJdbcTemplate.update(CitySql.INSERT, parameterSource, keyHolder);
		log.debug("{} inserted, new id = {}", affectedRows, keyHolder.getKey());
		
		city.setId(keyHolder.getKey().intValue());
		return city;
	}
	
	public Integer updateById(city city) {
		String qry = CitySql.UPDATE + CitySql.ID_CONDITION;
		
		SqlParameterSource parameterSource = new MapSqlParameterSource("id", city.getId())
				.addValue("name", city.getName())
				.addValue("countryCode", city.getCountryCode())
				.addValue("district", city.getDistrict())
				.addValue("population", city.getPopulation()); 
		return namedParameterJdbcTemplate.update(qry, parameterSource);
	}
	
	public Integer deleteById(Integer id) {
		SqlParameterSource parameterSource = new MapSqlParameterSource("id",id);
		return namedParameterJdbcTemplate.update(CitySql.DELETE + CitySql.ID_CONDITION, parameterSource);
	}

}
