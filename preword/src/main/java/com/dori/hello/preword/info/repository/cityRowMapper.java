package com.dori.hello.preword.info.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.dori.hello.preword.info.model.city;

public class cityRowMapper implements RowMapper<city> {
	
	@Override
	public city mapRow(ResultSet rs, int rowNum) throws SQLException {
		city city = new city();
		city.setId(rs.getInt("ID"));
		city.setName(rs.getString("Name"));
		city.setCountryCode(rs.getString("countrycode"));
		city.setDistrict(rs.getString("district"));
		city.setPopulation(rs.getInt("population"));
		return city;
	}

}
