package com.example.demo.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.RequestEntity;
import com.example.demo.Form.RequestForm;
import com.example.demo.Model.RequestModel;
import com.example.demo.Repository.EmployeeRepository;
import com.example.demo.Repository.RequestRepository;

@Service
public class RequestCheckService {
	
	@Autowired
	RequestRepository rep;
	
	@Autowired
	EmployeeRepository eRep;
	
	//指定のidに基づいた情報をすべて取得する
	public List<RequestModel> RequestCheck(Integer id)throws ParseException{
		
		List<Map<String,Object>> list = rep.RequestAll(id);
		
		List<RequestModel> modelList = new ArrayList<>();
		
		for(Map<String,Object> map:list) {
			RequestModel model  = new RequestModel();
			
			model.setRequest_id((Integer.parseInt(map.get("request_id").toString())));
			model.setEmployee_id((Integer.parseInt(map.get("id").toString())));
			model.setRequest_date((String)map.get("request_date"));
			model.setSelected_date((String)map.get("selected_date"));
			model.setRequest_flg((String)map.get("request_flg"));
			model.setRequest_reason((String)map.get("request_reason"));
			model.setReaction_flg((String)map.get("reaction_flg"));
			
			if((map.get("reaction_id")) != null){
			model.setReaction_id((Integer.parseInt(map.get("reaction_id").toString())));
			//idとともにそれに該当する名前もあげちゃう
			model.setReaction_name(RequestIdChanger((Integer.parseInt(map.get("reaction_id").toString()))));
			}
			if(map.get("reaction_date") != null) {
			model.setReaction_date((String)map.get("reaction_date"));
			}
			if(map.get("reaction_comment") != null) {
			model.setReaction_comment((String)map.get("reaction_comment"));
			}
			
			//申請者の名前を受け渡す
			model.setEmployee_name(RequestIdChanger((Integer.parseInt(map.get("id").toString()))));
			
			//申請状況を文字化する
			model.setReaction_detail(ReactionFlgChanger((String)map.get("reaction_flg")));
			
			//申請内容を文字列化する
			model.setRequest_detail(RequestFlgChanger((String)map.get("request_flg")));
			
			
			modelList.add(model);
		}
		
		return modelList;
	}
	
	//対象1件のみを取得する
	public RequestModel SingleRequestCheck(Integer id) {
		
		List<Map<String,Object>> list = rep.SingleRequestSelect(id);
		
		RequestModel model  = new RequestModel();
		
		for(Map<String,Object> map:list) {
			
			model.setRequest_id((Integer.parseInt(map.get("request_id").toString())));
			model.setEmployee_id((Integer.parseInt(map.get("id").toString())));
			model.setRequest_date((String)map.get("request_date"));
			model.setSelected_date((String)map.get("selected_date"));
			model.setRequest_flg((String)map.get("request_flg"));
			model.setRequest_reason((String)map.get("request_reason"));
			model.setReaction_flg((String)map.get("reaction_flg"));
			
			if((map.get("reaction_id")) != null){
			model.setReaction_id((Integer.parseInt(map.get("reaction_id").toString())));
			//idとともにそれに該当する名前もあげちゃう
			model.setReaction_name(RequestIdChanger((Integer.parseInt(map.get("reaction_id").toString()))));
			}
			if(map.get("reaction_date") != null) {
			model.setReaction_date((String)map.get("reaction_date"));
			}
			if(map.get("reaction_comment") != null) {
			model.setReaction_comment((String)map.get("reaction_comment"));
			}
			
			//申請者の名前を受け渡す
			model.setEmployee_name(RequestIdChanger((Integer.parseInt(map.get("id").toString()))));
			
			//申請状況を文字化する
			model.setReaction_detail(ReactionFlgChanger((String)map.get("reaction_flg")));
			
			//申請内容を文字列化する
			model.setRequest_detail(RequestFlgChanger((String)map.get("request_flg")));
			
		}
		return model;
		
	}
	//対象1件のみを取得する
	public RequestForm RequestFormCheck(Integer id) {
		
		List<Map<String,Object>> list = rep.SingleRequestSelect(id);
		
		RequestEntity entity  = new RequestEntity();
		
		for(Map<String,Object> map:list) {
			
			entity.setRequest_id((Integer.parseInt(map.get("request_id").toString())));
			entity.setEmployee_id((Integer.parseInt(map.get("id").toString())));
			entity.setRequest_date((String)map.get("request_date"));
			entity.setSelected_date((String)map.get("selected_date"));
			entity.setRequest_flg((String)map.get("request_flg"));
			entity.setRequest_reason((String)map.get("request_reason"));
			entity.setReaction_flg((String)map.get("reaction_flg"));
			
			if((map.get("reaction_id")) != null){
			entity.setReaction_id((Integer.parseInt(map.get("reaction_id").toString())));
			}
			if(map.get("reaction_date") != null) {
			entity.setReaction_date((String)map.get("reaction_date"));
			}
			if(map.get("reaction_comment") != null) {
			entity.setReaction_comment((String)map.get("reaction_comment"));
			}
			
		}
		RequestForm converter = new RequestForm();
		RequestForm form = converter.ConvertToFrom(entity);
		
		
		return form;
		
	}
	
	//更新処理
	public String RequestUpdate(RequestForm form) {
		
		//申請時点での日付をformに入力する
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年 MM月 dd日");
		String today = dateFormat.format(date);
		
		form.setRequest_date(today);
		
		//申請日が入力されている場合
		if(!(form.getSelected_date().isEmpty())) {
		
	        // selectedDateはISO 8601形式（yyyy-MM-dd）の文字列
	        LocalDate localdate = LocalDate.parse(form.getSelected_date());
	
	        // yyyy年 MM月 dd日 にフォーマットする
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年 MM月 dd日");
	        String formattedDate = localdate.format(formatter);
	        
	        form.setSelected_date(formattedDate);
		}
		
		
		//申請チェック
		if(RequestCheck(form).isEmpty()) {
			rep.RequestUpdate(form);
			return "更新が完了しました";
		}else {
			return RequestCheck(form);
		}
	}
	
	public String Delete(Integer id) throws Exception{
		
		rep.RequestDelete(id);
		
		String result ="申請番号"+  id +"を削除しました";
		
		return result;
		
		
	}
	
	//申請者・対応者のIDを名前に変換する
	public String RequestIdChanger(Integer id) {
		
		List<Map<String,Object>> list = eRep.SingleEmployee(id);
		
		// listから最初の要素（この例では一つのマップ）を取得
		Map<String, Object> employee = list.get(0);

		// Mapからfirst_nameとlast_nameを取得
		String firstName = (String) employee.get("first_name");
		String lastName = (String) employee.get("last_name");

		// 名前を結合してnameにセットする
		String name = lastName + " " + firstName;
		
		return name;
	}
	
	//申請の内容をFlgからStringに変換する
	public String RequestFlgChanger(String flg) {
		
		String request = null;
		
		if("0".equals(flg)) {
			request = "打刻修正依頼";
		}else if("1".equals(flg)) {
			request = "登録情報更新";
		}else if("2".equals(flg)) {
			request = "ユーザー登録依頼";
		}else if("3".equals(flg)) {
			request = "有給申請";
		}else if("4".equals(flg)) {
			request = "午前休申請";
		}else if("5".equals(flg)) {
			request = "午後休申請";
		}else if("6".equals(flg)){
			request = "後日有給申請";
		}
		
		return request;
	}
	
	//対応状況をFlgからStringに変換する
	public String ReactionFlgChanger(String flg) {
		
		String request = null;
		
		if("0".equals(flg)) {
			request = "未対応";
		}else if("1".equals(flg)) {
			request = "対応中";
		}else if("2".equals(flg)) {
			request = "対応完了";
		}else if("3".equals(flg)) {
			request = "差し戻し";
		}
		
		return request;
	}
	
	public String RequestCheck(RequestForm form) throws ParseException{
		
		String str = form.getRequest_flg();
		
		//有給申請関連の場合、日付を検証する
		if("3".equals(str) || "4".equals(str) || "5".equals(str) || "6".equals(str)) {
			
			if(form.getSelected_date().isEmpty()) {
				return "有給を取得する日付を選択してください";
			}
		}else if("0".equals(str)) {
			if(form.getSelected_date().isEmpty()) {
				return "更新対象の日付を選択してください";
			}
		}
		
		//すべての項目で、申請理由の入力の有無を確認する
		if(form.getRequest_reason().isEmpty()) {
			return "申請理由を入力して下さい";
		}
		return "";
	}
	
	//日付のフォーマットを変更するよ
	public String DateConverter(String days) throws java.text.ParseException {
		 SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy年 MM月 dd日");
	     SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");


			// 入力日付文字列を Date オブジェクトにパースする
			Date date = inputFormat.parse(days);
			
			// Date オブジェクトを目的の出力形式にフォーマットする
			String formattedDate = outputFormat.format(date);
			
			return formattedDate;
	}

}
