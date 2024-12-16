package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.Form.RequestForm;
import com.example.demo.Service.RequestService;

@Controller
public class RequestController {
	
	
	@Autowired
	RequestService service;
	
	//有給申請起動時にセットするフォーム
	@RequestMapping("/HappyRequest/{id}")
	public String HappyRequest(@PathVariable Integer id, Model model){
		model.addAttribute("requestForm", new RequestForm());
		
		return "happyRequest";
	}
	
	//実際に申請を登録する
	@RequestMapping("/RequestCheck/{id}")
	public String RequsstCheck(@ModelAttribute RequestForm form, @PathVariable Integer id, Model model) {
		
		//申請するためのサービスを呼び出す
		//空の場合はそのまま。入っていた場合は再度フロントへ返す
		String alert = service.Request(form,id);
		
		String str = form.getRequest_flg();
		
		try {
		
			if("3".equals(str) || "4".equals(str) || "5".equals(str) || "6".equals(str)) {
			
				if(alert.isEmpty()) {
					alert = "申請が完了しました。";
					model.addAttribute("alert",alert);
					model.addAttribute("requestForm", new RequestForm());
					return "happyRequest";
				}else {
					//setするためのreactionを表示する
					model.addAttribute("alert",alert);
					model.addAttribute("requestForm", form);
					return "happyRequest";
				}
			}else {
				if(alert.isEmpty()) {
					alert = "申請が完了しました。";
					model.addAttribute("alert",alert);
					model.addAttribute("requestForm", new RequestForm());
					return "OtherRequest";
				}else {
					//setするためのreactionを表示する
					model.addAttribute("alert",alert);
					model.addAttribute("requestForm", form);
					return "OtherRequest";
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	
	//その他申請起動時にセットするフォーム
	@RequestMapping("/OtherRequest/{id}")
	public String OtherRequest(@PathVariable Integer id, Model model){
		model.addAttribute("requestForm", new RequestForm());
		
		return "otherRequest";
	}

}
