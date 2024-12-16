package com.example.demo.Form;

import java.io.Serializable;

import lombok.Data;

@Data
public class PasswordForm implements Serializable{
	
	//ログインしているid
	private Integer id;
	
	//新しいパスワード
	private String password;
	
	//チェック用パスワード
	private String passwordCheck;

}
