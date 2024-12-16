package com.example.demo.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.Form.EmployeeForm;


@Repository
public class EmployeeRepository {

	@Autowired
	private JdbcTemplate jdbc;
	
	//全労働者を取得する
	public List<Map<String,Object>> AllEmployee(){
		
		String SQL = "SELECT * FROM employees WHERE delete_flg=0";
		
		List<Map<String,Object>> allData = jdbc.queryForList(SQL);
		
		return allData;
	}
	
	//労働者1件を取得する
	public List<Map<String,Object>> SingleEmployee(Integer id){
		
		String SQL = "SELECT * FROM employees WHERE id = ?";
		
		List<Map<String,Object>> singleData = jdbc.queryForList(SQL,id);
		
		return singleData;
	}
	
	//労働者1件を取得する(nameオーバーライド）
	public List<Map<String,Object>> SingleEmployee(String name){
		
		String SQL = "SELECT * FROM employees WHERE last_name = ?";
		
		List<Map<String,Object>> singleData = jdbc.queryForList(SQL,name);
		
		return singleData;
	}
	
	//労働者登録
	public int EmployeeRegister(EmployeeForm form, Integer id) {
		String SQL= "INSERT INTO employees(last_name, first_name, joined_year, happy_days, "
				+ "authority, password, "
				+ "created_by, updated_by, created_at, updated_at,delete_flg)"
				+ "VALUES(?,?,?,0,?,?,?,?,?,?,0)";
		
		//現在時刻を作る
		Date date = new Date();
		
		//SQL文実行・登録処理
		int result = jdbc.update(SQL,form.getLast_name(),form.getFirst_name(),form.getJoined_year(),form.getAuthority(),"0000",
				id,id,date,date);
		
		System.out.println(SQL);
		
		return result;
	}
	
	//労働者更新(管理者権限ではパスワードは変更できない）
	public int EmployeeUpdate(EmployeeForm form, Integer id) {
		
		String SQL= "UPDATE employees SET last_name=?, first_name=?, joined_year=?, happy_days=?, "
				+ "authority=?, updated_by=?, updated_at=? WHERE id = ?;";
		
		//現在時刻を作る
		Date date = new Date();
		
		//SQL文実行・登録処理
		int result = jdbc.update(SQL,form.getLast_name(),form.getFirst_name(),form.getJoined_year(),
				form.getHappy_days(),form.getAuthority(),id,date,form.getId());
		
		return result;
	}
	
	//労働者削除
	public int EmployeeDelete(Integer userId, Integer authorityId) {
		
		String SQL =  "UPDATE employees SET delete_flg=1,updated_by=?, updated_at=? WHERE id = ?;";
		
		//現在時刻を作る
		Date date = new Date();
		
		int result = jdbc.update(SQL,authorityId,date,userId);
		
		return result;
	}
	
	//労働者1件の有給を取得する
	public List<Map<String,Object>> SingleEmployeeHappyDays(Integer id){
		
		String SQL = "SELECT happy_days FROM employees WHERE id = ?";
		
		List<Map<String,Object>> singleData = jdbc.queryForList(SQL,id);
		
		return singleData;
	}
	
	//有給日数を更新
	public int HappyDaysUpdate(Integer userId, Integer authorityId, float happy_days) {
		
		String SQL =  "UPDATE employees SET happy_days=?,updated_by=?, updated_at=? WHERE id = ?;";
		
		//現在時刻を作る
		Date date = new Date();
		
		int result = jdbc.update(SQL,happy_days,authorityId,date,userId);
		
		return result;
	}
}
