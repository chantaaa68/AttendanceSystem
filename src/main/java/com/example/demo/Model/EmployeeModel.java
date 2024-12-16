package com.example.demo.Model;

import org.springframework.data.annotation.Id;

import com.example.demo.Entity.EmployeeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;

@Entity
@Data
public class EmployeeModel {
	
	@Id
	@Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String last_name;
	
	private String first_name;
	
	private String joined_year;
	
	private float happy_days;
	
	//権限は0の場合一般権限、1の場合は管理者権限
	private String authority;
	
	private String password;
	
	//作成者IDに紐づく名前を
	private String created_by;
	
	//更新者IDに紐づく名前を
	private String updated_by;
	
	private String created_at;
	
	private String updated_at;
	
	private Boolean delete_flg;
	
	public EmployeeModel() {
		
	}
	
	public EmployeeModel(
			Integer id,String last_name, String first_name,
			String joined_year, float happy_days, String authority,
			String password, String created_by, String updated_by,
			String created_at, String updated_at,Boolean delete_flg) {
		this.id = id;
		this.last_name = last_name;
		this.first_name = first_name;
		this.joined_year = joined_year;
		this.happy_days = happy_days;
		this.authority = authority;
		this.password = password;
		this.updated_by = updated_by;
		this.created_by = created_by;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.delete_flg = delete_flg;
	}
	
	//このままでは作成者と更新者がありません。
	public EmployeeModel ConvertToFrom(EmployeeEntity entity) {
		EmployeeModel model = new EmployeeModel();
		
		model.setId(entity.getId());
		model.setLast_name(entity.getLast_name());
		model.setFirst_name(entity.getFirst_name());
		model.setJoined_year(entity.getJoined_year());
		model.setHappy_days(entity.getHappy_days());
		if(entity.getAuthority()) {
			model.setAuthority("管理者権限");
		}else {
			model.setAuthority("一般権限");
		}
		model.setPassword(entity.getPassword());
		model.setCreated_at(entity.getCreated_at());
		model.setUpdated_at(entity.getUpdated_at());
		model.setDelete_flg(entity.getDelete_flg());

		return model;
	}

}
