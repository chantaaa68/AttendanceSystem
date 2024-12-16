package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.Form.AttendanceForm;
import com.example.demo.Model.AttendanceModel;
import com.example.demo.Service.AttendanceService;
import com.example.demo.Service.SessionService;

@Controller
public class AttendanceController {
	
	@Autowired
	AttendanceService service;
	
	@Autowired
	SessionService session;
	
	//returnする文字列
	String reaction;
	
	AttendanceModel attendanceModel = new AttendanceModel();
	
	AttendanceForm form = new AttendanceForm();
	
	@RequestMapping("/Start/{id}")
	public String StartTimestamp(@PathVariable Integer id, Model model) {
		
		try {
			//打刻処理
			reaction = service.StartTimeStamp(id);
			
			//フロントに返すModelに格納
			attendanceModel = service.TodaysAttendanceHistory(id);
			
			model.addAttribute("attendanceModel",attendanceModel);
			model.addAttribute("reaction", reaction);
			String commonReaction = "修正したい場合は申請してください。";
			model.addAttribute("commonReaction",commonReaction);
			
			return "index";
			
		} catch (ParseException e) {

			e.printStackTrace();
			return "error";
		} catch (java.text.ParseException e) {

			e.printStackTrace();
			return "error";
		}
	}
	
	@RequestMapping("/RestStart/{id}")
	public String RestStartTimeStamp(@PathVariable Integer id, Model model) {
		
		try {
			//休憩打刻処理
			reaction = service.RestStartTimeStamp(id);
			
			//休憩打刻後、更新された打刻履歴をフロントに返すModelに格納
			attendanceModel = service.TodaysAttendanceHistory(id);
			
			model.addAttribute("attendanceModel",attendanceModel);
			model.addAttribute("reaction", reaction);
			String commonReaction = "修正したい場合は申請してください。";
			model.addAttribute("commonReaction",commonReaction);
			
			return "index";
			
		} catch (ParseException e) {

			e.printStackTrace();
			return "error";
		} catch (java.text.ParseException e) {

			e.printStackTrace();
			return "error";
		}
		
	}
	
	@RequestMapping("/RestEnd/{id}")
	public String RestEndTimeStamp(@PathVariable Integer id, Model model) {
		
		try {
			//休憩終了打刻処理
			reaction = service.RestEndTimeStamp(id);
			
			//休憩終了打刻後、更新された打刻履歴をフロントに返すModelに格納
			attendanceModel = service.TodaysAttendanceHistory(id);
			
			model.addAttribute("attendanceModel",attendanceModel);
			model.addAttribute("reaction", reaction);
			String commonReaction = "修正したい場合は申請してください。";
			model.addAttribute("commonReaction",commonReaction);

			return "index";
			
		} catch (ParseException e) {

			e.printStackTrace();
			return "error";
		} catch (java.text.ParseException e) {

			e.printStackTrace();
			return "error";
		}
	}
	
	@RequestMapping("/End/{id}")
	public String AuthorityRestStartTimeStamp(@PathVariable Integer id, Model model) {
		
		try {
			//退勤打刻処理
			reaction = service.EndTimeStamp(id);
			
			//退勤打刻を2回押してしまった場合、現在の打刻内容を返す
			attendanceModel = service.TodaysAttendanceHistory(id);
			
			model.addAttribute("attendanceModel",attendanceModel);
			model.addAttribute("reaction", reaction);
			String commonReaction = "修正したい場合は申請してください。";
			model.addAttribute("commonReaction",commonReaction);
			
			//アラート警告がある場合は画面遷移しない
			if(reaction ==null || reaction.isEmpty()) {
				
				return "attendanceCheck";
			}else {
				return "index";
			}
			
		} catch (ParseException e) {

			e.printStackTrace();
			return "error";
		} catch (java.text.ParseException e) {

			e.printStackTrace();
			return "error";
		}
		
	}
	
	@RequestMapping("/ToConfirm/{id}")
	public String AttendanceConfirm(@ModelAttribute AttendanceModel attendance,@PathVariable Integer id, Model model) {
		
		try {
		if(attendance.getDays()==null) {
			//現在の打刻内容を返す
			attendanceModel = service.TodaysAttendanceHistory(id);
			
			model.addAttribute("attendanceModel",attendanceModel);
			return "index";
			
		}else {
		
			String alert = service.EndCorrection(attendance);
			//空白で返ってくる（休憩したのに休憩終了打刻していない場合）
			if(alert.isEmpty()) {
				attendanceModel = service.TodaysAttendanceHistory(id);
				model.addAttribute("attendanceModel",attendanceModel);
				model.addAttribute("reaction","休憩終了時刻を入力してください");
				return "attendanceCheck";
			}
			
			reaction = attendance.getDays() + alert;
			model.addAttribute("reaction", reaction);
			return "index";
		}
	
		}catch (java.text.ParseException e) {
	
			e.printStackTrace();
			return "error";
		}
	}
}
