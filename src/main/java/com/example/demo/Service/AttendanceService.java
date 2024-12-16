package com.example.demo.Service;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.AttendanceEntity;
import com.example.demo.Form.AttendanceForm;
import com.example.demo.Model.AttendanceModel;
import com.example.demo.Repository.AttendanceRepository;

@Service
public class AttendanceService {
	
	@Autowired
	AttendanceRepository rep;
	
	AttendanceEntity entity = new AttendanceEntity();
	
	AttendanceForm form = new AttendanceForm();
	
	AttendanceModel model = new AttendanceModel();
	
	//時間変換(Date型）
	public Date TimeDateChanger() throws java.text.ParseException {
		Date date = new Date();
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH時 mm分");
		String timeDate = timeFormat.format(date);
		Date time = timeFormat.parse(timeDate);
		
		return time;
	}
	
	//時間変換(String型)
	public String TimeStringChanger() {
		Date date = new Date();
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH時 mm分");
		String timeDate = timeFormat.format(date);
		return timeDate;
	}
	
	//初期表示用Modelに格納するための時間取得
	public AttendanceModel TodaysAttendanceHistory(Integer id)  throws ParseException, java.text.ParseException{
		Date date = new Date();
		//日付の型を変える
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年 MM月 dd日");
		String today = dateFormat.format(date);

		List<Map<String,Object>> todayHistory = rep.TodaysHistory(id, today);
		
		AttendanceEntity attendance = entity.ConvertToFrom(todayHistory);
		
		AttendanceModel attendanceModel = model.ConvertToFrom(attendance);
		
		return attendanceModel;
	}
	
	//出勤打刻
	public String StartTimeStamp(Integer id) throws ParseException, java.text.ParseException{
		
		//新規で今日の日付を取得し、それを変換
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年 MM月 dd日");
		String today = dateFormat.format(date);
		
		//今日、打刻された履歴があるかを確認
		List<Map<String,Object>> todayHistory = rep.TodaysHistory(id, today);
		AttendanceEntity attendance = new AttendanceEntity();
		
		for(Map<String,Object> history : todayHistory) {
			attendance.setStart_time((String)history.get("Start_time"));
			attendance.setRest_start_time((String)history.get("rest_start_time"));
			attendance.setRest_end_time((String)history.get("rest_end_time"));
			attendance.setEnd_time((String)history.get("End_time"));
		}
		
		//された形跡がないなら、出勤打刻実施。あればアラートを返す
		if(todayHistory == null || todayHistory.isEmpty()) {
			//勤怠登録処理（時間の登録）
			rep.WorkStart(id, today, TimeStringChanger());
			
			String reaction = TimeStringChanger();
			System.out.println(reaction);
			 
			return "";
		}else if(attendance.getStart_time() != null){
			return "すでに退勤しています。";
		}else {
			return "1日に2回出勤打刻はできません。";
		}
	}
	
	//休憩打刻
	public String RestStartTimeStamp(Integer id) throws ParseException, java.text.ParseException{
		
		//新規で日付を取得し、それを変換
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年 MM月 dd日");
		String today = dateFormat.format(date);
		
		//今日の打刻履歴を取得する
		List<Map<String,Object>> todayHistory = rep.TodaysHistory(id, today);
		AttendanceEntity attendance = new AttendanceEntity();
		
		//された形跡がないなら、アラートを返す
		if(todayHistory == null || todayHistory.isEmpty()) {
			return "休憩？その前に出勤打刻してくださいよ";
		}
		
		//休憩履歴を取得
		for(Map<String,Object> history : todayHistory) {
			attendance.setStart_time((String)history.get("Start_time"));
			attendance.setRest_start_time((String)history.get("rest_start_time"));
			attendance.setRest_end_time((String)history.get("rest_end_time"));
			attendance.setEnd_time((String)history.get("End_time"));
		}
		if(attendance.getEnd_time() != null) {
			return "すでに退勤しています。";
		}

		//休憩履歴があれば、アラートを返し、ない場合は休憩打刻
		if(attendance.getRest_start_time() != null) {
			
			return "1日に2回休憩打刻はできません。";
		}else {
			//勤怠登録処理
			rep.RestStart(id, today, TimeStringChanger());
			
			String reaction = TimeStringChanger();
			System.out.println(reaction);
			 
			return "";
		}
	
	}
	
	//休憩終了打刻
		public String RestEndTimeStamp(Integer id) throws ParseException, java.text.ParseException{
			
			//新規で日付を取得し、それを変換
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年 MM月 dd日");
			String today = dateFormat.format(date);
			
			//今日の打刻履歴を取得する
			List<Map<String,Object>> todayHistory = rep.TodaysHistory(id, today);
			AttendanceEntity attendance = new AttendanceEntity();
			
			//された形跡がないなら、アラートを返す
			if(todayHistory == null || todayHistory.isEmpty()) {
				return "休憩終わり？その前に出勤打刻してくださいよ";
			}
			
			for(Map<String,Object> history : todayHistory) {
				attendance.setRest_start_time((String)history.get("rest_start_time"));
				attendance.setRest_end_time((String)history.get("rest_end_time"));
				attendance.setEnd_time((String)history.get("End_time"));
			}
			
			//休憩開始履歴もないなら、アラートを返す
			if(attendance.getRest_start_time() == null) {
				return "休憩終わり？その前に休憩開始打刻してくださいよ";
			}
			
			//退勤記録があれば、アラートを返す
			if(attendance.getEnd_time() != null) {
				return "すでに退勤しています。";
			}
			
			//休憩終了履歴があれば、アラートを返し、ない場合は休憩打刻
			if(attendance.getRest_end_time() != null) {
				return "1日に2回休憩終了打刻はできません。";
			}
			
			//勤怠登録処理
			rep.RestEnd(id, today, TimeStringChanger());
			
			String reaction = TimeStringChanger();
			System.out.println(reaction);
			 
			return "";
		
		
		}
		
		//退勤打刻
		public String EndTimeStamp(Integer id) throws ParseException, java.text.ParseException{
			
			//新規で日付を取得し、それを変換
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年 MM月 dd日");
			String today = dateFormat.format(date);
			
			//今日の打刻履歴を取得する
			List<Map<String,Object>> todayHistory = rep.TodaysHistory(id, today);
			AttendanceEntity attendance = new AttendanceEntity();
			
			
			//された形跡がないなら、アラートを返す
			if(todayHistory == null || todayHistory.isEmpty()) {
				return "退勤？その前に出勤打刻してくださいよ";
			}
			
			//退勤履歴を取得
			for(Map<String,Object> history : todayHistory) {
				attendance.setStart_time((String)history.get("Start_time"));
				attendance.setRest_start_time((String)history.get("rest_start_time"));
				attendance.setRest_end_time((String)history.get("rest_end_time"));
				attendance.setEnd_time((String)history.get("End_time"));
			}
			
			if(attendance.getRest_end_time().isEmpty()) {
				return "退勤？その前に休憩終了打刻してくださいよ";
			}
			
			if(attendance.getEnd_time() != null) {
				return "すでに退勤しています。";
			}
			
			//退勤履歴があれば、アラートを返し、ない場合は退勤打刻
			if(attendance.getEnd_time() != null) {
				
				return "1日に2回退勤終了打刻はできません。";
			}else {
				//勤怠登録処理
				rep.WorkEnd(id, today, TimeStringChanger());
				
				String reaction = TimeStringChanger();
				System.out.println(reaction);
				 
				return "";
			}
		
		}
		
		//退勤後勤怠確定
		public String EndCorrection(AttendanceModel model) {
			
			//時間単位・分単位の値を作成して格納
			 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H時 m分");
			
			 LocalTime startTime = LocalTime.parse(model.getStart_time(), formatter);
		     LocalTime endTime = LocalTime.parse(model.getEnd_time(), formatter);
		     int totalWorkMinutes = (int) ChronoUnit.MINUTES.between(startTime, endTime);
		     

		     //休憩を実施していた場合
		     if((model.getRest_start_time()!=null && !(model.getRest_start_time().isEmpty()))){
		    	 
		    	 if(model.getRest_end_time().isEmpty()) {
		    		 return "";
		    	 }
		    	 
		    	 LocalTime restStartTime = LocalTime.parse(model.getRest_start_time(), formatter);
		    	 LocalTime restEndTime = LocalTime.parse(model.getRest_end_time(), formatter);
		    	 
		        int breakMinutes = (int) ChronoUnit.MINUTES.between(restStartTime, restEndTime);
		        int netWorkMinutes = totalWorkMinutes - breakMinutes;
		        int additonalWorkMinutes = netWorkMinutes-480;
		        
		        //労働時間格納用
		        int workHours = netWorkMinutes / 60;
		        int workMinutes = netWorkMinutes % 60;
		        String workTime;
		        if(workMinutes<10) {
		        	workTime = workHours + ":0"+ workMinutes;
		        }else {
		        	workTime = workHours + ":"+ workMinutes;
		        }
		        
		        //休憩時間格納用
		        int restHours = breakMinutes / 60;
		        int restMinutes = breakMinutes % 60;
		        String restTime;
		        if(restMinutes<10) {
		        	restTime = restHours + ":0" + restMinutes;
		        }else {
		        	restTime = restHours + ":" + restMinutes;
		        }
		        
		        //残業時間格納用
		        int additionalHours = additonalWorkMinutes / 60;
		        int additionalMinutes = additonalWorkMinutes % 60;
		        String additionalTime;
		        if(additionalMinutes<10) {
		        	additionalTime = additionalHours +":0" + additionalMinutes;
		        }else {
		        	additionalTime = additionalHours +":" + additionalMinutes;
		        }
		        
		        model.setWork_total_time(workTime);
		        model.setRest_total_time(restTime);
		        
		        //残業時間を格納する
		        if(additonalWorkMinutes >0) {
		        	model.setAdditional_total_time(additionalTime);
		        }else {
		        	model.setAdditional_total_time("0:00");
		        }
		        
		        //総労働時間の入力
		        model.setTotal_work_minute(netWorkMinutes);
		        
				//確定処理
				rep.Correction(model);
		     } else {
	        
		        //労働時間格納用
		        int workHours = totalWorkMinutes / 60;
		        int workMinutes = totalWorkMinutes % 60;
		        String workTime;
		        if(workMinutes<10) {
		        	workTime = workHours + ":0"+ workMinutes;
		        }else {
		        	workTime = workHours + ":"+ workMinutes;
		        }
		        
		        //休憩時間格納用
		        String restTime = "0:00";
		        
		        //残業時間格納用
		        String additionalTime = "0:00";
		        
		        model.setWork_total_time(workTime);
		        model.setRest_total_time(restTime);
		        
		        model.setAdditional_total_time(additionalTime);
		        
		        //総労働時間の入力
		        model.setTotal_work_minute(totalWorkMinutes);
			    
				//確定処理
				rep.Correction(model);
		     }
			
			return "の勤怠を確定しました。";
		}

}
