package com.example.demo.Entity;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;

@Entity
@Data
public class EmployeeEntity {
	
	@Id
	@Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String last_name;
	
	private String first_name;
	
	private String joined_year;
	
	private float happy_days;
	
	private Boolean authority;
	
	private String password;
	
	private Integer created_by;
	
	private Integer updated_by;
	
	private String created_at;
	
	private String updated_at;
	
	private Boolean delete_flg;

}
