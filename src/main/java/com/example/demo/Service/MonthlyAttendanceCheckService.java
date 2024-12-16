package com.example.demo.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.AttendanceEntity;
import com.example.demo.Form.HistoryCheckForm;
import com.example.demo.Model.AttendanceModel;
import com.example.demo.Repository.AttendanceRepository;

@Service
public class MonthlyAttendanceCheckService {
	
	@Autowired
	AttendanceRepository rep;
	
	AttendanceEntity entity = new AttendanceEntity();
	
	AttendanceModel model = new AttendanceModel();
	
	//その年月の履歴を確認する
	public List<AttendanceModel> HistoryCheck(HistoryCheckForm form) {
		
		//入力された値から履歴を呼び出す
		Integer id = form.getId();
		String checkMonth = form.getYear() + "年 "+ form.getMonth() + "月%";
		
		List<AttendanceModel> allList = new ArrayList<>();
		
		List<Map<String,Object>> list = rep.MonthlyAttendanceHistory(id,checkMonth);
		
		List<AttendanceEntity> entityList = entity.ConvertToFromList(list);
		
		for(AttendanceEntity attendanceEntity :entityList) {
			
			AttendanceModel attenanceModel = model.ConvertToFrom(attendanceEntity);
			
			allList.add(attenanceModel);
		}
		
		return allList;
	}
	
	//すべての履歴を取得する
	public List<AttendanceModel> AllHistoryCheck(Integer id) {
		
		List<AttendanceModel> allList = new ArrayList<>();
		
		List<Map<String,Object>> list = rep.AllAttendanceHistory(id);
		
		List<AttendanceEntity> entityList = entity.ConvertToFromList(list);
		
		for(AttendanceEntity attendanceEntity :entityList) {
			
			AttendanceModel attenanceModel = model.ConvertToFrom(attendanceEntity);
			
			allList.add(attenanceModel);
		}
		
		return allList;
	}
	
}
