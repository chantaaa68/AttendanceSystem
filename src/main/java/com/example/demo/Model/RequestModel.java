package com.example.demo.Model;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class RequestModel {

	@Id
	private Integer request_id;
	
	private Integer employee_id;
	
	//申請者の名前を入力する
	private String employee_name;
	
	private String request_date;
	
	private String selected_date;
	
	private String request_flg;
	
	//申請内容に基づく文字列を入力
	private String request_detail;
	
	private String request_reason;
	
	private Integer reaction_id;
	
	//idに基づく名前を入力
	private String reaction_name;
	
	private String reaction_date;
	
	private String reaction_comment;
	
	private String reaction_flg;
	
	//flgに基づく文字列を入力
	private String reaction_detail;
	
	//ここでしか使わないから他では要らないよ
	private String accept;

}
