package com.example.demo.Entity;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class RequestEntity {
	
	@Id
	private Integer request_id;
	
	private Integer employee_id;
	
	private String request_date;
	
	private String selected_date;
	
	private String request_flg;
	
	private String request_reason;
	
	private Integer reaction_id;
	
	private String reaction_date;
	
	private String reaction_comment;
	
	private String reaction_flg;
	
	public RequestEntity(){
		
	}
}
