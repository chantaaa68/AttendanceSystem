package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Form.PasswordForm;
import com.example.demo.Repository.PasswordRepository;

@Service
public class PassUpdateService {
	
	@Autowired
	PasswordRepository rep;
	
	public String PassUpdate(PasswordForm form,Integer id) {
		
		form.setId(id);
		
		rep.PassUpdate(form);
		
		return "更新完了";
	}

}
