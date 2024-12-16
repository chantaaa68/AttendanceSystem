package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.Form.EmployeeForm;
import com.example.demo.Form.LoginForm;
import com.example.demo.Form.PasswordForm;
import com.example.demo.Model.AttendanceModel;
import com.example.demo.Service.AttendanceService;
import com.example.demo.Service.LoginService;
import com.example.demo.Service.SessionService;

@Controller
public class LoginController {
	
	@Autowired
	JdbcTemplate jdbc;
	
	@Autowired
	LoginService service;
	
	@Autowired
	AttendanceService attendanceService;
	
	@Autowired
	SessionService session;
	
	/**
	 * 初期画面(ログイン画面が呼び出されるときに起動
	 */
	@RequestMapping("")
	public String LoginForm(Model model) {
		model.addAttribute("LoginForm", new LoginForm());
		return "login";
	}
	
	/**
	 * 権限を確認し、画面一覧に遷移する時に起動
	 */
	@RequestMapping("/Index")
	public String AuthorityChecker(@ModelAttribute LoginForm form, Model model) {
		
		//ここでログイン可否をする
		try {
			//他の画面から遷移してきたときのことを想定、セッションからformを取得する
			if(form.getId() == null) {
				form = session.GetLoginFormSession();
			}
			
			if(form != null) {
				//パスワード・IDチェック
				if(!(service.LoginCheck(form))){
					model.addAttribute("alert","idかpasswordが間違っています");
					model.addAttribute("LoginForm", new LoginForm());
					return "login";
				}
				//deleteFlgが立っている場合
				if(service.DeleteFlgCheck(form)) {
					model.addAttribute("alert","そのアカウントは削除されています");
					model.addAttribute("LoginForm", new LoginForm());
					return "login";
				}
				
				//初回ログイン（パスワードが0000の場合、変更を促す）
				if("0000".equals(form.getPassword())) {
					model.addAttribute("passwordForm",new PasswordForm());
					model.addAttribute("passReaction","パスワードが初期値です。変更をお願いします。");
					session.SetSession(form);
					return "passChanger";
				}
			
			
			//初期表示の際に、その人のその日の出勤日の勤怠を確認する為のモデル
			AttendanceModel attendanceModel = new AttendanceModel();
			attendanceModel = attendanceService.TodaysAttendanceHistory(form.getId());
			model.addAttribute(attendanceModel);

			//セッションに格納して画面遷移
			EmployeeForm loginEmployee = service.Login(form.getId());
			session.SetSession(loginEmployee);
			session.SetSession(form);
			
			}
			
			if(session.GetLoginFormSession()==null) {
				model.addAttribute("LoginForm", new LoginForm());
				return "login";
			}
			
			return "index";

		}catch(Exception e) {
			e.printStackTrace();
			return "error";
		}
		
	}
	
	@RequestMapping("/Logout")
	public String Logout(Model model) {
		session.RemoveSession();
		model.addAttribute("alert","ログアウトしました");
		model.addAttribute("LoginForm", new LoginForm());
		return "login";
		
	}
	


}
