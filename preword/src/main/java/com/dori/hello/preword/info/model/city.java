package com.dori.hello.preword.info.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class city {
	private Integer id;
	private String name;
	private String countryCode;
	private String district;
	private Integer population;

}
