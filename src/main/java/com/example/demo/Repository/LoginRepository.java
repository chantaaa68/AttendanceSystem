package com.example.demo.Repository;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LoginRepository {
	
	@Autowired
	private JdbcTemplate jdbc;
	
	//対象のユーザーの権限を取得する
	public Map<String,Object> AuthorityChecker(int id) throws Exception{
		
		String SQL = "SELECT authority FROM employees WHERE id = ?";
		Map<String,Object> Authority = null;
		
		try {
			Authority = jdbc.queryForList(SQL, id).get(0);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return Authority;
	}
	
	//対象のユーザーの情報を取得する
	public List<Map<String,Object>> singleEmployee(int id) throws Exception {
		
		String SQL = "SELECT * FROM employees WHERE id = ?";
		
		List<Map<String,Object>> employee =null;
		
		try {
			employee = jdbc.queryForList(SQL, id);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return employee;
	}
	
	//対象のユーザーの情報を取得する
	public List<Map<String,Object>> singleEmployee(int id,String password) throws Exception {
		
		String SQL = "SELECT * FROM employees WHERE id = ? && password = ?";
		
		List<Map<String,Object>> employee =null;
		
		try {
			employee = jdbc.queryForList(SQL, id, password);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return employee;
	}

}
