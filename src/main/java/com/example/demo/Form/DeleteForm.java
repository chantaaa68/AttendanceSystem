package com.example.demo.Form;

import java.io.Serializable;

import lombok.Data;


@Data
public class DeleteForm implements Serializable{
	
	private Integer employeeId;
	
	private Integer authorityId;
	
	private Integer requestId;

}
