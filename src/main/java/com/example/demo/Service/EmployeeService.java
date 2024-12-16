package com.example.demo.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.EmployeeEntity;
import com.example.demo.Form.EmployeeForm;
import com.example.demo.Model.EmployeeModel;
import com.example.demo.Repository.EmployeeRepository;

@Service
public class EmployeeService {
	
	@Autowired
	EmployeeRepository rep;
	
	//全ユーザーを取得する
	public List<EmployeeModel> AllEmployee(){
		
		List<Map<String,Object>> list = rep.AllEmployee();
		
		List<EmployeeModel> modelList = new ArrayList<>();
		
		//1人ずつ値をentityに格納し、それをModelに変換する
		for(Map<String,Object> map:list) {
			EmployeeEntity employee = new EmployeeEntity();
			
			employee.setId((Integer.parseInt(map.get("id").toString())));
			employee.setLast_name((String)map.get("last_name"));
			employee.setFirst_name((String)map.get("first_name"));
			employee.setJoined_year((String)map.get("joined_year"));
			employee.setHappy_days((float)(map.get("happy_days")));
			employee.setAuthority((Boolean)map.get("authority"));
			employee.setPassword((String)map.get("password"));
			employee.setCreated_by((Integer.parseInt(map.get("created_by").toString())));
			employee.setUpdated_by((Integer.parseInt(map.get("updated_by").toString())));
			employee.setCreated_at((String)map.get("created_at").toString());
			employee.setUpdated_at((String)map.get("updated_at").toString());
			employee.setDelete_flg((Boolean)map.get("delete_flg"));
			
			//変換して格納する用のModelを用意
			EmployeeModel em  = new EmployeeModel();
			EmployeeModel model = em.ConvertToFrom(employee);
			
			//ここまででは3種類で数値でしか表示できないので、以下で変換する
			
			//権限によって表示する内容を変える
			if(employee.getAuthority()) {
				model.setAuthority("管理者権限");
			}else {
				model.setAuthority("一般権限");
			}
			
			//取得したidより該当の名前を取得(作成者）
			List<Map<String,Object>> createdMap = rep.SingleEmployee(employee.getCreated_by());
			
			for(Map<String,Object> cMap :createdMap) {
				String first_name = (String)cMap.get("first_name");
				String last_name = (String)cMap.get("last_name");
				
				String created_by = last_name +" "+ first_name;
				
				model.setCreated_by(created_by);
			}
			
			//取得したidより該当の名前を取得(更新者)
			List<Map<String,Object>> updatedMap = rep.SingleEmployee(employee.getUpdated_by());
			
			for(Map<String,Object> cMap :updatedMap) {
				String first_name = (String)cMap.get("first_name");
				String last_name = (String)cMap.get("last_name");
				
				String updated_by = last_name +" "+ first_name;
				
				model.setUpdated_by(updated_by);
			}
			
			
			modelList.add(model);
		}
	
		return modelList;
	}
	
	//1人のユーザーデータを(更新用で)取得する
	public EmployeeForm SingleEmployeeForm(Integer id) {
		
		List<Map<String,Object>> singleMap = rep.SingleEmployee(id);
		
		EmployeeForm form = new EmployeeForm();
		
		for(Map<String,Object> map :singleMap) {
			
			form.setId((Integer.parseInt(map.get("id").toString())));
			form.setLast_name((String)map.get("last_name"));
			form.setFirst_name((String)map.get("first_name"));
			form.setJoined_year((String)map.get("joined_year"));
			form.setHappy_days((float)(map.get("happy_days")));
			form.setAuthority((Boolean)map.get("authority"));
			form.setPassword((String)map.get("password"));
			form.setCreated_by((Integer.parseInt(map.get("created_by").toString())));
			form.setUpdated_by((Integer.parseInt(map.get("updated_by").toString())));
			form.setCreated_at((String)map.get("created_at").toString());
			form.setUpdated_at((String)map.get("updated_at").toString());
			form.setDelete_flg((Boolean)map.get("delete_flg"));
			
		}
		
		return form;
	}
	
	//ユーザーデータを取得する(nameオーバーライド）
		public List<EmployeeModel> LastNameEmployee(String name) {
			
			List<Map<String,Object>> list = rep.SingleEmployee(name);
			
			List<EmployeeModel> modelList = new ArrayList<>();
			
			//1人ずつ値をentityに格納し、それをModelに変換する
			for(Map<String,Object> map:list) {
				EmployeeEntity employee = new EmployeeEntity();
				
				employee.setId((Integer.parseInt(map.get("id").toString())));
				employee.setLast_name((String)map.get("last_name"));
				employee.setFirst_name((String)map.get("first_name"));
				employee.setJoined_year((String)map.get("joined_year"));
				employee.setHappy_days((float)(map.get("happy_days")));
				employee.setAuthority((Boolean)map.get("authority"));
				employee.setPassword((String)map.get("password"));
				employee.setCreated_by((Integer.parseInt(map.get("created_by").toString())));
				employee.setUpdated_by((Integer.parseInt(map.get("updated_by").toString())));
				employee.setCreated_at((String)map.get("created_at").toString());
				employee.setUpdated_at((String)map.get("updated_at").toString());
				employee.setDelete_flg((Boolean)map.get("delete_flg"));
				
				//変換して格納する用のModelを用意
				EmployeeModel em  = new EmployeeModel();
				EmployeeModel model = em.ConvertToFrom(employee);
				
				//ここまででは3種類で数値でしか表示できないので、以下で変換する
				
				//権限によって表示する内容を変える
				if(employee.getAuthority()) {
					model.setAuthority("管理者権限");
				}else {
					model.setAuthority("一般権限");
				}
				
				//取得したidより該当の名前を取得(作成者）
				List<Map<String,Object>> createdMap = rep.SingleEmployee(employee.getCreated_by());
				
				for(Map<String,Object> cMap :createdMap) {
					String first_name = (String)cMap.get("first_name");
					String last_name = (String)cMap.get("last_name");
					
					String created_by = last_name +" "+ first_name;
					
					model.setCreated_by(created_by);
				}
				
				//取得したidより該当の名前を取得(更新者)
				List<Map<String,Object>> updatedMap = rep.SingleEmployee(employee.getUpdated_by());
				
				for(Map<String,Object> cMap :updatedMap) {
					String first_name = (String)cMap.get("first_name");
					String last_name = (String)cMap.get("last_name");
					
					String updated_by = last_name +" "+ first_name;
					
					model.setUpdated_by(updated_by);
				}
				
				
				modelList.add(model);
			}
			
			return modelList;
		}
	
	//1人のユーザーデータを(閲覧用で)取得する
	public EmployeeModel SingleEmployeeModel(Integer id) {
		
		List<Map<String,Object>> singleMap = rep.SingleEmployee(id);
		
		EmployeeEntity entity = new EmployeeEntity();
		
		for(Map<String,Object> map :singleMap) {
			
			entity.setId((Integer.parseInt(map.get("id").toString())));
			entity.setLast_name((String)map.get("last_name"));
			entity.setFirst_name((String)map.get("first_name"));
			entity.setJoined_year((String)map.get("joined_year"));
			entity.setHappy_days((float)(map.get("happy_days")));
			entity.setAuthority((Boolean)map.get("authority"));
			entity.setPassword((String)map.get("password"));
			entity.setCreated_by((Integer.parseInt(map.get("created_by").toString())));
			entity.setUpdated_by((Integer.parseInt(map.get("updated_by").toString())));
			entity.setCreated_at((String)map.get("created_at").toString());
			entity.setUpdated_at((String)map.get("updated_at").toString());
			entity.setDelete_flg((Boolean)map.get("delete_flg"));
			
		}
		
		//変換して格納する用のModelを用意
		EmployeeModel em  = new EmployeeModel();
		EmployeeModel model = em.ConvertToFrom(entity);
		
		//ここまででは3種類で数値でしか表示できないので、以下で変換する
		
		//権限によって表示する内容を変える
		if(entity.getAuthority()) {
			model.setAuthority("管理者権限");
		}else {
			model.setAuthority("一般権限");
		}
		
		//取得したidより該当の名前を取得(作成者）
		List<Map<String,Object>> createdMap = rep.SingleEmployee(entity.getCreated_by());
		
		for(Map<String,Object> cMap :createdMap) {
			String first_name = (String)cMap.get("first_name");
			String last_name = (String)cMap.get("last_name");
			
			String created_by = last_name +" "+ first_name;
			
			model.setCreated_by(created_by);
		}
		
		//取得したidより該当の名前を取得(更新者)
		List<Map<String,Object>> updatedMap = rep.SingleEmployee(entity.getUpdated_by());
		
		for(Map<String,Object> cMap :updatedMap) {
			String first_name = (String)cMap.get("first_name");
			String last_name = (String)cMap.get("last_name");
			
			String updated_by = last_name +" "+ first_name;
			
			model.setUpdated_by(updated_by);
		}
		
		return model;
	}
	
	//登録用
	public String RegistEmployee(EmployeeForm form, Integer id) {

		rep.EmployeeRegister(form,id);
		
		return "登録が完了しました";
	}
	
	//更新用
	public String UpdateEmployee(EmployeeForm form, Integer id) {
		
		rep.EmployeeUpdate(form,id);
		
		return "更新が完了しました";
	}
	
	//削除用
	public String DeleteEmployee(Integer userId, Integer authorityId) {
		
		rep.EmployeeDelete(userId, authorityId);
		
		String result = "労働者番号" + userId + "の削除が完了しました";
		
		return result;
	}
	
	

}
