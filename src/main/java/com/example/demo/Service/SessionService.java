package com.example.demo.Service;

import org.springframework.stereotype.Service;

import com.example.demo.Form.EmployeeForm;
import com.example.demo.Form.LoginForm;

import jakarta.servlet.http.HttpSession;

@Service
public class SessionService {
	
    // HttpSession型のフィールドを定義する
    private HttpSession session;
    
    // コンストラクタを作成し、@Autowiredアノテーションを付与する

    public SessionService(HttpSession session) {
        // フィールドに代入する
        this.session = session;
    }
    
    //sessionにEmployeeFormを登録する
    public String SetSession(EmployeeForm form) {
        // Sessionへの保存
        this.session.setAttribute("employeeForm", form);
        return "SetOK";
    }
    
    //sessionにLoginFormを登録する
    public String SetSession(LoginForm form) {
        // Sessionへの保存
        this.session.setAttribute("loginForm", form);
        return "SetOK";
    }
    
    //sesionからemployeeFormを取得する
    public EmployeeForm GetSession() {
        EmployeeForm form = (EmployeeForm)this.session.getAttribute("employeeForm");
        
        return form;
    }
    
    //sesionからLoginFormを取得する
    public LoginForm GetLoginFormSession() {
        LoginForm form = (LoginForm)this.session.getAttribute("loginForm");
        
        return form;
    }

    //ログアウト時やエラー時にセッションを破棄する
    public String RemoveSession() {
        session.removeAttribute("employeeForm");
        session.removeAttribute("loginForm");
        return "RemoveOK";
    }
}