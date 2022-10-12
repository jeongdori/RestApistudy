package com.dori.hello.preword.info.model;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Project {
	
	public String projectName;
	public String author;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-mm-dd")
	public Date createdDate;

}
