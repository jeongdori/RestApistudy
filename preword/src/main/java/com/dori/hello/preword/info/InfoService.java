package com.dori.hello.preword.info;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dori.hello.preword.info.model.Project;
import com.dori.hello.preword.info.model.city;
import com.dori.hello.preword.info.repository.cityRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InfoService {
	
	private final cityRepository cityRepository;
	
	// spring 4.2 이상은 @Autowired 생략 가능
	public InfoService(cityRepository cityRepository) {
		this.cityRepository = cityRepository;
	}
	
	public Project getProjectInfo() {
		
		Project project = new Project();
		project.projectName = "preword";
		project.author = "hello-dori";
		project.createdDate = new Date();
		
		return project;
	}
	
	public List<city> getCityList() {
		return this.cityRepository.findList();
	}
	
	public List<city> findCityByCodeAndPopulation(String countryCode, int population) {
		log.debug("countryCode = {}, population = {}", countryCode, population);
		return this.cityRepository.findByCountryCodeAndPopulation(countryCode, population);
	}
	
	public city insert(city city) {
		return this.cityRepository.insert(city);
	}
	
	public Integer updateById(city city) {
		log.debug("city id = {}", city.getId());
		return cityRepository.updateById(city);
	}
	
	public Integer deleteById(Integer id) {
		log.debug("city id = {}", id);
		return cityRepository.deleteById(id);
	}

}
