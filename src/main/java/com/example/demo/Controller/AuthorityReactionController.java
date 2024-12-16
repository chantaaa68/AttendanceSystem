package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.Form.EmployeeForm;
import com.example.demo.Model.AttendanceModel;
import com.example.demo.Model.RequestModel;
import com.example.demo.Service.AuthorityReactionService;
import com.example.demo.Service.EmployeeService;
import com.example.demo.Service.RequestCheckService;
import com.example.demo.Service.SessionService;

@Controller
public class AuthorityReactionController {
	
	@Autowired
	AuthorityReactionService service;
	
	@Autowired
	EmployeeService eService;
	
	@Autowired
	RequestCheckService rService;
	
	@Autowired
	SessionService session;
	
	
	//ユーザーの実施した申請確認する
	@RequestMapping("/RequestIndex")
	public String RequestCheck(Model model)throws ParseException{
		
		try {
		List<RequestModel> list = service.AllUserRequest();
		
		model.addAttribute("AllRequestModel",list);
		
		return "allRequest";
		
		} catch(Exception e) {
			e.printStackTrace();
			return "error";
		}
		
	}
	
	//申請内容の詳細を確認する
	@RequestMapping("/AuthorityRequestInfo/{id}")
	public String AuthorityRequestInfoCheck(@PathVariable Integer id, Model model)throws ParseException{
		
		try {
		RequestModel requestModel = rService.SingleRequestCheck(id);
		
		model.addAttribute("requestModel",requestModel);
		
		return "authorityCheckRequestInfo";
		
		} catch(Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	
	//対応画面に持っていく資料
	@RequestMapping("/RequestReactionForm/{id}")
	public String RequestReaction(@PathVariable Integer id, Model model) {
		
		RequestModel requestModel = service.SingleRequestModel(id);
		
		model.addAttribute("requestModel",requestModel);
		
		return "requestReaction";
	}
	
	
	//対応後の更新処理
	@RequestMapping("/RequestReaction/{id}")
	public String RequestReaction(@ModelAttribute RequestModel requestModel,@PathVariable Integer id, Model model) {
		
		//差し戻しの場合の処理
		if("差し戻し".equals(requestModel.getAccept())) {
			String reaction = service.CanNotAccept(requestModel,id);
			model.addAttribute("alert",reaction);
			
			return "requestReaction";
		}
		
		//条件により遷移先を分岐させる
		if("0".equals(requestModel.getRequest_flg())) {
			//勤怠更新
			AttendanceModel attendanceModel = service.AttendanceReaction(requestModel,id);
			model.addAttribute("attendanceModel",attendanceModel);
			String reaction = requestModel.getRequest_reason();
			model.addAttribute("reaction",reaction);
			model.addAttribute("requestModel",requestModel);
			return "authorityAttendanceCheck";
			
		}else if("1".equals(requestModel.getRequest_flg())) {
			//情報更新
			EmployeeForm form = eService.SingleEmployeeForm(requestModel.getEmployee_id());
			model.addAttribute("updateForm",form);
			String reaction = requestModel.getRequest_reason();
			model.addAttribute("reaction",reaction);
			service.UpdateReaction(requestModel, id);
			return "update";
			
		}else if("2".equals(requestModel.getRequest_flg())) {
			//新規登録用
			String reaction = requestModel.getRequest_reason();
			service.RegisterReaction(requestModel, id);
			model.addAttribute("reaction",reaction);
			model.addAttribute("employeeForm",new EmployeeForm());
			return "Registration";
			
		}else if("3".equals(requestModel.getRequest_flg())) {
			//有給申請
			String reaction  = service.HappyRequestReaction(requestModel, id);
			model.addAttribute("alert",reaction);
			return "requestReaction";
			
		}else if("4".equals(requestModel.getRequest_flg())) {
			//午前休申請
			String reaction  = service.AMHappyRequestReaction(requestModel, id);
			model.addAttribute("alert",reaction);
			return "requestReaction";
			
		}else if("5".equals(requestModel.getRequest_flg())) {
			//午後休申請
			String reaction  = service.PMHappyRequestReaction(requestModel, id);
			model.addAttribute("alert",reaction);
			return "requestReaction";
			
		}else if("6".equals(requestModel.getRequest_flg())) {
			//後日有給申請
			String reaction  = service.AfterHappyRequestReaction(requestModel, id);
			model.addAttribute("alert",reaction);
			return "requestReaction";
		}
		return "requestReaction";
		
	}

	//勤怠の更新処理
	@RequestMapping("/AttendanceCorrection/{id}")
	public String AttendanceCorrection(@ModelAttribute AttendanceModel attendanceModel,@PathVariable Integer id, Model model) {
		
		service.AttendanceCorrection(attendanceModel,id);
		
		List<RequestModel> list = service.AllUserRequest();
		model.addAttribute("AllRequestModel",list);
		model.addAttribute("alert","勤怠の修正が完了しました");
		
		return "allRequest";
	}
}
