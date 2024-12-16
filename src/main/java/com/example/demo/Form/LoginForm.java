package com.example.demo.Form;

import java.io.Serializable;

import lombok.Data;

@Data
public class LoginForm implements Serializable{
	
	//ログイン時に入力されたid
	private Integer id;
	
	//ログイン時に入力されたパスワード
	private String password;

}
