package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.Form.EmployeeForm;
import com.example.demo.Form.LoginForm;
import com.example.demo.Form.PasswordForm;
import com.example.demo.Service.PassUpdateService;
import com.example.demo.Service.SessionService;

@Controller
public class PasswordController {
	
	@Autowired
	SessionService session;
	
	@Autowired
	PassUpdateService service;
	
	//初回ログイン時
	@RequestMapping("/PassCheck/{id}")
	public String PassUpdate(@ModelAttribute PasswordForm form,@PathVariable Integer id, Model model) {
		
		if((form.getPassword()).equals(form.getPasswordCheck())) {
			
			service.PassUpdate(form,id);
			model.addAttribute("alert","パスワードの更新が完了しました。再度ログインして下さい");
			model.addAttribute("LoginForm", new LoginForm());
			return "login";
		}else {
			model.addAttribute("passReaction","入力した値が一致しません");
			model.addAttribute("passwordForm",new PasswordForm());
			return "passChanger";
		}
	}
	

	//ヘッダーからの遷移時
	@RequestMapping("/PassChange")
	public String PassChange(Model model) {
		model.addAttribute("passwordForm",new PasswordForm());
		
		EmployeeForm form = session.GetSession();
		
		String pass = form.getPassword();
		
		String reaction ="現在のパスワードは" +  pass + "です";
		
		model.addAttribute("passReaction",reaction);
		return "passChanger";
	}
}
