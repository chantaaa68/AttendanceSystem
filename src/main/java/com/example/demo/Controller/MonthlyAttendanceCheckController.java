package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.Form.EmployeeForm;
import com.example.demo.Form.HistoryCheckForm;
import com.example.demo.Model.AttendanceModel;
import com.example.demo.Service.MonthlyAttendanceCheckService;
import com.example.demo.Service.SessionService;

@Controller
public class MonthlyAttendanceCheckController {
	
	@Autowired
	MonthlyAttendanceCheckService service;
	
	@Autowired
	SessionService sService;
	
	//初期表示
	@RequestMapping("/AllAttendanceCheck/{id}")
	public String AllAttendanceCheck(@PathVariable Integer id, Model model) {
		
		List<AttendanceModel> list = service.AllHistoryCheck(id);
		
		model.addAttribute("attendanceList",list);
		
		//検索用
		HistoryCheckForm form = new HistoryCheckForm();
		model.addAttribute("historyCheckForm",form);
		
		return "attendanceHistory";
	}
	
	//条件検索
	@RequestMapping("/MonthlyAttendanceCheck")
	public String AllAttendanceCheck(@ModelAttribute HistoryCheckForm form, Model model) {
		
		if("".equals(form.getMonth()) || "".equals(form.getYear()) || form.getMonth()==null || form.getYear()==null) {
			EmployeeForm employeeForm = sService.GetSession();
			List<AttendanceModel> list = service.AllHistoryCheck(employeeForm.getId());
			
			model.addAttribute("attendanceList",list);
			model.addAttribute("historyCheckForm",form);
			
		}else {
		
			System.out.println(form.getMonth()+"月"+ form.getYear()+ "年");
			
			List<AttendanceModel> list = service.HistoryCheck(form);
			
			model.addAttribute("attendanceList",list);
			
			model.addAttribute("historyCheckForm",form);
		}
		
		return "attendanceHistory";
	}
}
