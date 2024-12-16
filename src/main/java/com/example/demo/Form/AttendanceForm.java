package com.example.demo.Form;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

import com.example.demo.Entity.AttendanceEntity;

import lombok.Data;

@Data
public class AttendanceForm implements Serializable{
	
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


	public AttendanceForm() {
		
	}
	
	public AttendanceForm ConvertTtFrom(AttendanceEntity entity) {

		AttendanceForm form = new AttendanceForm();
		
		form.setId(entity.getId());
		form.setDays(entity.getDays());
		form.setStart_time(entity.getStart_time());
		form.setEnd_time(entity.getEnd_time());
		form.setRest_start_time(entity.getRest_start_time());
		form.setRest_end_time(entity.getRest_end_time());
		form.setComment(entity.getComment());
		form.setAttendance_flg(entity.getAttendance_flg());
		form.setWork_total_time(entity.getWork_total_time());
		form.setRest_total_time(entity.getRest_total_time());
		form.setAdditional_total_time(entity.getAdditional_total_time());
		form.setTotal_work_minute(entity.getTotal_work_minute());
		
		return form;
		
	}

}
