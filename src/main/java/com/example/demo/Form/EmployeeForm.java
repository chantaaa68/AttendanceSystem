package com.example.demo.Form;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

import com.example.demo.Entity.EmployeeEntity;

import lombok.Data;

@Data
public class EmployeeForm implements Serializable{
	
	@Id
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
	
	// コンストラクターの定義
    public EmployeeForm(
    		Integer id, 
    		String last_name, 
    		String first_name, 
    		String joined_year, 
    		float happy_days,
    		Boolean authority, 
    		String password, 
    		Integer created_by, 
    		Integer updated_by,
    		String created_at, 
    		String updated_at, 
    		Boolean delete_flg
    		) {
        this.id = id;
        this.last_name = last_name;
        this.first_name = first_name;
        this.joined_year = joined_year;
        this.happy_days = happy_days;
        this.authority = authority;
        this.password = password;
        this.created_by = created_by;
        this.updated_by = updated_by;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.delete_flg = delete_flg;
    }
    
    public EmployeeForm() {

    }

	
	public EmployeeForm ConvertToFrom(EmployeeEntity entity) {
		return new EmployeeForm(
			entity.getId(),
			entity.getLast_name(),
			entity.getFirst_name(),
			entity.getJoined_year(),
			entity.getHappy_days(),
			entity.getAuthority(),
			entity.getPassword(),
			entity.getCreated_by(),
			entity.getUpdated_by(),
			entity.getCreated_at(),
			entity.getUpdated_at(),
			entity.getDelete_flg()
			);
	}


}
