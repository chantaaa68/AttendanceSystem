package com.example.demo.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.Model.AttendanceModel;
import com.example.demo.Model.RequestModel;

@Repository
public class AttendanceRepository {
	
	@Autowired
	private JdbcTemplate jdbc;
	
	//出勤打刻用(dateはyyyy年MM月dd日の型、stampは時間
	public int WorkStart(Integer id,String days,String stamp) throws ParseException{
		
		String SQL = "INSERT INTO attendances (id, days, start_time)"
				+ "VALUES(?,?,?)";
		
		int result = jdbc.update(SQL,id,days,stamp);
		System.out.println("新規で勤怠を登録したよ");
		
		return result;
	}
	
	//休憩開始
	public int RestStart(Integer id,String days,String stamp) throws ParseException{
		
		String SQL = "UPDATE attendances SET rest_start_time=? WHERE id = ? && days = ?;";
		
		int result = jdbc.update(SQL,stamp,id,days);
		
		return result;
	}
	
	//休憩終了
	public int RestEnd(Integer id,String days,String stamp) throws ParseException{
		
		String SQL = "UPDATE attendances SET rest_end_time=? WHERE id = ? && days = ?;";
		
		int result = jdbc.update(SQL,stamp,id,days);
		
		return result;
	}
	
	//退勤
	public int WorkEnd(Integer id,String days,String stamp) throws ParseException{
		
		String SQL = "UPDATE attendances SET end_time=? WHERE id = ? && days = ?;";
		
		int result = jdbc.update(SQL,stamp,id,days);
		
		return result;
	}
	
	//勤怠修正
	public int Correction(AttendanceModel model) throws ParseException{
		
		String SQL = "UPDATE attendances SET start_time=?,rest_start_time=?, rest_end_time=?, end_time=?, "
				+ "comment=?,work_total_time=?,rest_total_time=?,additional_total_time=?, total_work_minute=?,"
				+ " attendance_flg=? WHERE id = ? AND days = ?;";
		
		
		
		int result = jdbc.update(SQL,model.getStart_time(),model.getRest_start_time(),model.getRest_end_time()
				,model.getEnd_time(),model.getComment(),model.getWork_total_time(),model.getRest_total_time()
				,model.getAdditional_total_time(),model.getTotal_work_minute(),model.getAttendance_flg()
				,model.getId(),model.getDays());
		
		return result;
	}
	
	//有給用
	public int HappyRegister(RequestModel model,String comment, String flg) throws ParseException{
		
		String SQL = "INSERT INTO attendances (id, days, start_time, end_time, rest_start_time, "
				+ "rest_end_time, comment, attendance_flg)"
				+ "VALUES(?,?,?,?,?,?,?,?)";
		
		int result = jdbc.update(SQL,model.getEmployee_id(),model.getSelected_date(),"10時 00分","13時 00分",
				"14時 00分","19時 00分",comment,flg);
		
		System.out.println("有給で出勤を登録したよ");
		
		return result;
	}
	
	//対象のユーザーで対象の日付のものが存在するのか
	public List<Map<String,Object>> TodaysHistory(Integer id,String date)throws ParseException{
		
		String SQL = "SELECT * FROM attendances WHERE id = ? && days = ?";
		
		List<Map<String,Object>> history = jdbc.queryForList(SQL,id,date);
		
		return history;
	}
	
	//対象のユーザーの日付の勤怠情報を確認する
	public List<Map<String,Object>> TodaysAttendanceHistory(Integer id,Date date)throws ParseException{
		
		String SQL = "SELECT 'start_time','end_time','rest_start_time', 'rest_end_time' FROM attendances WHERE id = ? && days = ?";
		
		List<Map<String,Object>> history = jdbc.queryForList(SQL,id,date);
		
		return history;
	}
	
	//対象のユーザーの月ごとの勤怠情報を取得
	public List<Map<String,Object>> MonthlyAttendanceHistory(Integer id, String date)throws ParseException{
		
		String SQL = "SELECT * FROM attendances WHERE id = ? && days LIKE ?";
		
		List<Map<String,Object>> history = jdbc.queryForList(SQL,id,date);
		
		return history;
	}
	
	//対象のユーザーの勤怠情報を取得
	public List<Map<String,Object>> AllAttendanceHistory(Integer id)throws ParseException{
		
		String SQL = "SELECT * FROM attendances WHERE id = ?";
		
		List<Map<String,Object>> history = jdbc.queryForList(SQL,id);
		
		return history;
	}

}
