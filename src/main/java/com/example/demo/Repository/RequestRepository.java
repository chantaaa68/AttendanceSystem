package com.example.demo.Repository;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.Form.RequestForm;
import com.example.demo.Model.RequestModel;

@Repository
public class RequestRepository {
	
	@Autowired
	private JdbcTemplate jdbc;
	
	//申請用
	public int Request(RequestForm form) throws ParseException{
		
		//申請直後は未確認となるため、reaction_situationは0を代入
		String SQL = "INSERT INTO requests (id, request_date, request_flg,"
				+ "selected_date, request_reason, reaction_flg)"
				+ "VALUES(?,?,?,?,?,0)";
		
		int result = jdbc.update(SQL,form.getEmployee_id(),form.getRequest_date(),
				form.getRequest_flg(),form.getSelected_date(),form.getRequest_reason());
		System.out.println(result + "件、新規で申請を登録したよ");
		
		return result;
	}
	
	//ユーザー側・申請修正
	public int RequestUpdate(RequestForm form) throws ParseException{
		
		String SQL = "UPDATE requests SET request_date=?,selected_date=?, request_flg=?,request_reason=?, "
				+ "reaction_flg=? WHERE request_id=?;";
		
		int result = jdbc.update(SQL,form.getRequest_date(),form.getSelected_date(),form.getRequest_flg(),
				form.getRequest_reason(),0,form.getRequest_id());
		
		System.out.println(result + "件、申請を更新したよ");
		
		return result;
	}
	
	//管理者側・申請対応
	public int RequestReaction(RequestModel model) throws ParseException{
		
		String SQL = "UPDATE requests SET reaction_id=?,reaction_comment=?, reaction_date=?, "
				+ "reaction_flg=? WHERE request_id=?;";
		
		int result = jdbc.update(SQL,model.getReaction_id(),model.getReaction_comment(),model.getReaction_date(),
				model.getReaction_flg(),model.getRequest_id());
		
		System.out.println(result + "件、申請を更新したよ");
		
		return result;
	}
	
	//ユーザー側がした自分の申請をすべて取得する
	public List<Map<String,Object>> RequestAll(Integer id) throws ParseException{
		
		String SQL = "SELECT * FROM requests WHERE id = ?";
		
		List<Map<String,Object>> result = jdbc.queryForList(SQL,id);
		
		System.out.println("ユーザー申請を全件取得したよ");
		
		return result;
	}
	
	//申請をすべて取得する
	public List<Map<String,Object>> RequestAll() throws ParseException{
		
		String SQL = "SELECT * FROM requests";
		
		List<Map<String,Object>> result = jdbc.queryForList(SQL);
		
		System.out.println("申請を全件取得したよ");
		
		return result;
	}
	
	//ユーザー側がした自分の申請、指定したフラグですべて取得する
	public List<Map<String,Object>> RequestSelect(Integer id, String flg) throws ParseException{
		
		String SQL = "SELECT * FROM requests WHERE request_id = ? && request_flg = ?";
		
		List<Map<String,Object>> result = jdbc.queryForList(SQL,id,flg);
		
		System.out.println(flg + "のユーザー申請を取得したよ");
		
		return result;
	}
	
	//ユーザー側がした自分の申請、指定したidで取得する
	public List<Map<String,Object>> SingleRequestSelect(Integer id) throws ParseException{
		
		String SQL = "SELECT * FROM requests WHERE request_id = ?";
		
		List<Map<String,Object>> result = jdbc.queryForList(SQL,id);
		
		System.out.println(id + "の申請番号の申請を取得したよ");
		
		return result;
	}
	
	//指定した申請の削除
	public int RequestDelete(Integer id) throws Exception{
		
		String SQL = "DELETE FROM requests WHERE request_id = ?";
		
		int result = jdbc.update(SQL,id); 
		
		return result;
	}
}
