package com.kh.moyoung.movie.model.vo;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
	private int no;
	
	private int writerNo;
	
	private String writerId;
	
	private String title;
	
	private String content;
	
	private String originalFileName;
	
	private String renamedFileName;
	
	private int readCount;
	
	private String status;
	
	private String type;
		
	private Date createDate;
	
	private Date modifyDate;
}