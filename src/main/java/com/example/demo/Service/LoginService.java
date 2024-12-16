package com.example.demo.Service;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.EmployeeEntity;
import com.example.demo.Form.EmployeeForm;
import com.example.demo.Form.LoginForm;
import com.example.demo.Repository.LoginRepository;


@Service
public class LoginService {
	
	@Autowired
	LoginRepository rep;
	
	EmployeeForm form = new EmployeeForm();
	
	//該当のユーザー情報をFormに変換して返す
	public EmployeeForm Login(int id) throws Exception{
		
		List<Map<String, Object>> user = null;
		try {
			user = rep.singleEmployee(id);
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
		EmployeeEntity employee = new EmployeeEntity();
		
		for(Map<String,Object> userInfo: user) {
			employee.setId((Integer.parseInt(userInfo.get("id").toString())));
			employee.setLast_name((String)userInfo.get("last_name"));
			employee.setFirst_name((String)userInfo.get("first_name"));
			employee.setJoined_year((String)userInfo.get("joined_year"));
			employee.setHappy_days((float)(userInfo.get("happy_days")));
			employee.setAuthority((Boolean)userInfo.get("authority"));
			employee.setPassword((String)userInfo.get("password"));
			employee.setCreated_by((Integer.parseInt(userInfo.get("created_by").toString())));
			employee.setUpdated_by((Integer.parseInt(userInfo.get("updated_by").toString())));
			employee.setCreated_at((String)userInfo.get("created_at").toString());
			employee.setUpdated_at((String)userInfo.get("updated_at").toString());
			employee.setDelete_flg((Boolean)userInfo.get("delete_flg"));
		}
		
		 EmployeeForm employeeForm = form.ConvertToFrom(employee);
		
		return employeeForm;
	}
	
	//権限が管理者なのかをチェックする
	public Boolean AuthorityChecker(Integer id) throws Exception{
		
		Map<String, Object> authority = null;
		try {
			authority = rep.AuthorityChecker(id);
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
		Boolean authoritycheck = (Boolean) authority.get("authority");
		
		System.out.println("あなたは管理者権限？" + authoritycheck);
		
		return authoritycheck;	
	}
	
	public Boolean LoginCheck(LoginForm form) throws Exception{
		//ログインチェックを実装しよう。
		List<Map<String,Object>> list = rep.singleEmployee(form.getId(), form.getPassword());
		
		//空の場合はfalse(ログインできないよ）、ある場合はtrue(ログイン可能)
		return !list.isEmpty();
	}
	
	public Boolean DeleteFlgCheck(LoginForm loginForm) throws Exception{
		
		form = Login(loginForm.getId());
		
		return form.getDelete_flg();
	}

}
