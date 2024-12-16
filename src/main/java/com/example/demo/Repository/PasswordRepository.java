package com.example.demo.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.Form.PasswordForm;

@Repository
public class PasswordRepository {
	
	@Autowired
	private JdbcTemplate jdbc;
	
	public int PassUpdate(PasswordForm form) {
		
		String SQL = "UPDATE employees SET password=? WHERE id=?;";
		
		int result = jdbc.update(SQL,form.getPassword(),form.getId());
		
		return result;
	}

}
