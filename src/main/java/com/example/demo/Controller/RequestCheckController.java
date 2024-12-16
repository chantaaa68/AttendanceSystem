package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.Form.DeleteForm;
import com.example.demo.Form.EmployeeForm;
import com.example.demo.Form.RequestForm;
import com.example.demo.Model.RequestModel;
import com.example.demo.Service.RequestCheckService;
import com.example.demo.Service.SessionService;

@Controller
public class RequestCheckController {
	
	@Autowired
	RequestCheckService service;
	
	@Autowired
	SessionService session;
	
	//自分の実施した申請確認する
	@RequestMapping("/AllRequestCheck/{id}")
	public String RequestCheck(@PathVariable Integer id, Model model)throws ParseException{
		
		try {
		List<RequestModel> list = service.RequestCheck(id);
		
		model.addAttribute("AllRequestModel",list);
		
		//削除用
		model.addAttribute("deleteForm",new DeleteForm());
		
		return "userRequest";
		
		} catch(Exception e) {
			e.printStackTrace();
			return "error";
		}
		
	}
	
	//自分の実施した申請確認する
	@RequestMapping("/AllRequestCheck")
	public String ReturnRequestCheck(Model model)throws ParseException{
		
		EmployeeForm form = session.GetSession();
		
		try {
		List<RequestModel> list = service.RequestCheck(form.getId());
		
		model.addAttribute("AllRequestModel",list);
		
		return "userRequest";
		
		} catch(Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	
	//申請内容の詳細を確認する
	@RequestMapping("/RequestInfo/{id}")
	public String RequestInfoCheck(@PathVariable Integer id, Model model){
		
		try {
		RequestModel requestModel = service.SingleRequestCheck(id);
		
		model.addAttribute("requestModel",requestModel);
		
		return "requestInfo";
		
		} catch(Exception e) {
			e.printStackTrace();
			return "error";
		}
		
	}
	//更新画面に遷移する
	@RequestMapping("/RequestUpdateForm/{id}")
	public String RequestUpdateForm(@PathVariable Integer id, Model model) {
		
		RequestForm form = service.RequestFormCheck(id);
		
		//日付のフォーマットを変換
		try {
			form.setSelected_date(service.DateConverter(form.getSelected_date()));
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		
		model.addAttribute("updateForm", form);
		
		return "requestUpdate";
	}
	
	//更新処理
	@RequestMapping("/RequestUpdate")
	public String RequestUpdate(@ModelAttribute RequestForm form, Model model) {
		
		String alert = service.RequestUpdate(form);
		model.addAttribute("alert",alert);
		
		//再度リストをセットする
		List<RequestModel> list = service.RequestCheck(form.getEmployee_id());
		model.addAttribute("AllRequestModel",list);
		
		return "userRequest";
		
	}
	
	
	//指定の申請を削除する
	@RequestMapping("/RequestDelete")
	public String RequestDelete(@ModelAttribute DeleteForm form, Model model)throws ParseException{
		
		try {
			String alert = service.Delete(form.getRequestId());
			model.addAttribute("alert",alert);
			
			//再度リストをセットする
			List<RequestModel> list = service.RequestCheck(form.getEmployeeId());
			model.addAttribute("AllRequestModel",list);
			
			//削除用
			model.addAttribute("deleteForm",new DeleteForm());
			
			return "userRequest";
			
		}catch(Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

}
