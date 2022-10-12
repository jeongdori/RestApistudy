package com.dori.hello.preword.info;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.dori.hello.preword.info.model.Project;
import com.dori.hello.preword.info.model.TestHello;
import com.dori.hello.preword.info.model.city;
import com.dori.hello.preword.info.model.fileData;
import com.dori.hello.preword.info.storage.StorageService;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("info")
public class InfoController {
	
	// ----------생성자를 이용해 BeanFactory에서  infoService를 찾음
	private InfoService infoService;
	private StorageService storageService;
	
	@Autowired  // spring 4.3 버전 이상부터는 생략 가능
	public InfoController(InfoService infoService, StorageService storageService) {
		this.infoService = infoService;
		this.storageService = storageService;
	}
	//----------------------------------------------------------
	
	
	@GetMapping("/project")
	public Object projectInfo() {
		log.debug("/info start");
		
		Project project = infoService.getProjectInfo();
		
		return project;
	}
	
	@GetMapping("custom")
	public String customJson() {
		JsonObject jo = new JsonObject();
		
		jo.addProperty("projectName", "preword");
		jo.addProperty("author", "hello dori");
		jo.addProperty("createdDate", new Date().toString());
		
		JsonArray ja = new JsonArray();
		for(int i=0; i<5; i++) {
			JsonObject jObj = new JsonObject();
			jObj.addProperty("prop"+i, i);
			ja.add(jObj);
		}
		
		jo.add("follower", ja);
		
		return jo.toString();
	}
	
	@GetMapping("cityList")
	public Object cityList() {
		log.debug("/cityList start");
		List<city> cityList = infoService.getCityList();
		return cityList;
	}
	
	@GetMapping("cityListByCode/{countryCode}/{population}")
	public Object cityByCountryCode(@PathVariable("countryCode") String ctCode, @PathVariable("population") int population) {
		log.debug("countryCode = {}, population {}", ctCode, population);
		List<city> cityList = infoService.findCityByCodeAndPopulation(ctCode, population);
		return cityList;
	}
	
	@GetMapping("cityAdd/{name}/{countryCode}/{district}/{population}")
	public Object cityAdd(@PathVariable(value="name") String name
			, @PathVariable(value="countryCode") String ctCode
			, @PathVariable(value="district") String district
			, @PathVariable(value="population") int population) {
		
		log.debug("name = {}, ctCode = {}, district = {}, population ={}", name, ctCode, district, population);
		
		return "ok";
	}
	
	
//	@GetMapping(value="cityAdd")
//	public Object cityAdd(city city) {
//		log.debug("city = {}", city.toString());
//		return "ok";
//	}
	
	
	@PostMapping("cityAdd")
	public ResponseEntity<city> cityAdd(@RequestBody city city) {
		try {
			log.debug("city = {}", city.toString());
			
			return new ResponseEntity<>(infoService.insert(city), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@PostMapping(value="cityEdit")
	public ResponseEntity<String> cityEdit(@RequestBody city city) {
		try {
			log.debug("city = {}", city.toString());
			Integer updatedCnt = infoService.updateById(city);
			return new ResponseEntity<>(String.format("%d updated", updatedCnt), HttpStatus.OK);
		}catch (Exception e) {
			log.error(e.toString());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value="cityDelete")
	public ResponseEntity<String> cityDelete(Integer id){
		
		try {
			log.debug("city id = {}", id);
			Integer deleteCnt = infoService.deleteById(id);
			return new ResponseEntity<>(String.format("%d deleted", deleteCnt), HttpStatus.OK);
			
		} catch (Exception e) {
			log.error(e.toString());
			return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value="uploadFile")
	public ResponseEntity<String> uploadFile(MultipartFile file) throws IllegalStateException, IOException{
		
		if(!file.isEmpty()) {
			log.debug("file org name = {}", file.getOriginalFilename());
			log.debug("file context type = {}", file.getContentType());
			file.transferTo(new File(file.getOriginalFilename()));
		}
		
		return new ResponseEntity<>("",HttpStatus.OK);
	}
	
	@PostMapping(value="upload")
	public ResponseEntity<String> upload(MultipartFile file) throws IllegalStateException, IOException{
		storageService.store(file);
		return new ResponseEntity<>("", HttpStatus.OK);
	}
	
	@GetMapping(value="download")
	public ResponseEntity<Resource> serveFile(@RequestParam(value="filename") String filename){
		
		Resource file = storageService.loadAsResource(filename);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment : filename=\""+file.getFilename() + "\"").body(file);
	}
	
	@PostMapping(value="deleteAll")
	public ResponseEntity<String> deleteAll(){
		storageService.deleteAll();
		return new ResponseEntity<>("",HttpStatus.OK);
	}
	
	@GetMapping(value="fileList")
	public ResponseEntity<List<fileData>> getListFiles(){
		List<fileData> fileinfos = storageService.loadAll()
				.map(path->{
					fileData data = new fileData();
					String filename = path.getFileName().toString();
					data.setFilename(filename);
					data.setUrl(MvcUriComponentsBuilder.fromMethodName(InfoController.class,"serveFile",filename).build().toString());
				
		
			try {
				data.setSize(Files.size(path));
			} catch (Exception e) {
				log.error(e.getMessage());
			}
			return data;
		
		}).collect(Collectors.toList());
		
		return ResponseEntity.status(HttpStatus.OK).body(fileinfos);
	}
	
	@PostMapping(value="test")
	public ResponseEntity<Void> test(@RequestPart("files") List<MultipartFile> files,
			@RequestParam("jsonList") String jsonList)throws JsonProcessingException {
		
		ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new SimpleModule());
        List<TestHello> testHelloList = objectMapper.readValue(jsonList, new TypeReference<>() {});
		
		log.debug("file count = {}", files.size());
		log.debug("TestHello count = {}", testHelloList.size());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	

}
