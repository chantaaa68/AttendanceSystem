package com.example.demo.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class AttendanceEntity {
	
	@Id
	private int id;
	
	private String days;
	
	private String  start_time;
	
	private String end_time;
	
	private String rest_start_time;
	
	private String rest_end_time;
	
	private String comment;
	
	private String attendance_flg;
	
	private String work_total_time;
	
	private String rest_total_time;
	
	private String additional_total_time;
	
	private Integer total_work_minute;
	
	
	public AttendanceEntity() {
		
	}
	
	public AttendanceEntity ConvertToFrom(List<Map<String,Object>> list) {
		AttendanceEntity entity = new AttendanceEntity();
		
		for(Map<String,Object> map :list) {
			entity.setId((Integer.parseInt(map.get("id").toString())));
			entity.setDays((String)map.get("days"));
			entity.setStart_time((String)map.get("start_time"));
			entity.setEnd_time((String)map.get("end_time"));
			entity.setRest_start_time((String)map.get("rest_start_time"));
			entity.setRest_end_time((String)map.get("rest_end_time"));
			entity.setComment((String)map.get("comment"));
			entity.setWork_total_time((String)map.get("work_total_time"));
			entity.setRest_total_time((String)map.get("rest_total_time"));
			entity.setAdditional_total_time((String)map.get("additional_total_time"));
			if((map.get("total_work_minute")!=null)){
			entity.setTotal_work_minute((Integer.parseInt(map.get("total_work_minute").toString())));
			}
			entity.setAttendance_flg(FlgConverter((String)map.get("attendance_flg")));
			
		}
		
		return entity;
	}
	
	public List<AttendanceEntity> ConvertToFromList(List<Map<String,Object>> list) {
		List<AttendanceEntity> allList = new ArrayList<>();
		
		for(Map<String,Object> map :list) {
			AttendanceEntity entity = new AttendanceEntity();
			
			entity.setId((Integer.parseInt(map.get("id").toString())));
			entity.setDays((String)map.get("days"));
			entity.setStart_time((String)map.get("start_time"));
			entity.setEnd_time((String)map.get("end_time"));
			entity.setRest_start_time((String)map.get("rest_start_time"));
			entity.setRest_end_time((String)map.get("rest_end_time"));
			entity.setComment((String)map.get("comment"));
			entity.setWork_total_time((String)map.get("work_total_time"));
			entity.setRest_total_time((String)map.get("rest_total_time"));
			entity.setAdditional_total_time((String)map.get("additional_total_time"));
			if((map.get("total_work_minute")!=null)){
				entity.setTotal_work_minute((Integer.parseInt(map.get("total_work_minute").toString())));
			}
			entity.setAttendance_flg(FlgConverter((String)map.get("attendance_flg")));
			
			allList.add(entity);
		}
		
		return allList;
	}
	
	//変換用
		public String FlgConverter(String flg) {
			
			String flgName = null;
			
			if("0".equals(flg)) {
				flgName = "出勤";
			}else if("1".equals(flg)) {
				flgName = "遅刻";
			}else if("2".equals(flg)) {
				flgName = "早退";
			}else if("3".equals(flg)) {
				flgName = "有給";
			}else if("4".equals(flg)) {
				flgName = "午前休";
			}else if("5".equals(flg)) {
				flgName = "午後休";
			}else if("6".equals(flg)){
				flgName = "欠勤";
			}
			
			return flgName;
		}

}
