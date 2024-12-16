package com.example.demo.Model;

import org.springframework.data.annotation.Id;

import com.example.demo.Entity.AttendanceEntity;

import jakarta.enterprise.inject.Model;
import lombok.Data;

@Model
@Data
public class AttendanceModel {
	
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
	
	

	public AttendanceModel() {
		
	}
	
	public AttendanceModel ConvertToFrom(AttendanceEntity entity) {
		AttendanceModel model = new AttendanceModel();
		model.setId(entity.getId());
		model.setDays(entity.getDays());
		model.setStart_time(entity.getStart_time());
		model.setEnd_time(entity.getEnd_time());
		model.setRest_start_time(entity.getRest_start_time());
		model.setRest_end_time(entity.getRest_end_time());
		model.setComment(entity.getComment());
		model.setAttendance_flg(entity.getAttendance_flg());
		model.setWork_total_time(entity.getWork_total_time());
		model.setRest_total_time(entity.getRest_total_time());
		model.setAdditional_total_time(entity.getAdditional_total_time());
		model.setTotal_work_minute(entity.getTotal_work_minute());
		
		return model;
	}
	
}
