package com.example.demo.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;

import com.example.demo.Form.RequestForm;
import com.example.demo.Repository.RequestRepository;

@Service
public class RequestService {
	
	@Autowired
	RequestRepository rep;
	
	//申請登録処理(チェックも)
	public String Request(RequestForm form,Integer id) throws ParseException{
		
		//idをformに入力する
		form.setEmployee_id(id);
		
		//申請時点での日付をformに入力する
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年 MM月 dd日");
		String today = dateFormat.format(date);
		
		form.setRequest_date(today);
		
		//申請日が入力されている場合
		if((form.getSelected_date() != null) && !(form.getSelected_date().isEmpty())) {
		
	        // selectedDateはISO 8601形式（yyyy-MM-dd）の文字列
	        LocalDate localdate = LocalDate.parse(form.getSelected_date());
	
	        // yyyy年 MM月 dd日 にフォーマットする
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年 MM月 dd日");
	        String formattedDate = localdate.format(formatter);
	        
	        form.setSelected_date(formattedDate);
		}
        
		if(RequestCheck(form).isEmpty()) {
			
			rep.Request(form);
		}else {
			return RequestCheck(form);
		}
		
		return "";
	}
	
	public String RequestCheck(RequestForm form) throws ParseException{
		
		String str = form.getRequest_flg();
		String date = form.getSelected_date();
		
		//有給申請関連の場合、日付を検証する
		if("3".equals(str) || "4".equals(str) || "5".equals(str) || "6".equals(str)) {
			
			if(form.getSelected_date().isEmpty()) {
				return "有給を取得する日付を選択してください";
			}
		}else if("0".equals(str)) {
			if(form.getSelected_date().isEmpty()) {
				return "更新対象の日付を選択してください";
			}
		}else if(date ==null) {
			return "再読み込みをしても申請は通りません";
		}else if(!(date.isEmpty())) {
			return "申請希望日は必要ありません";
		}
		
		
		
		//すべての項目で、申請理由の入力の有無を確認する
		if(form.getRequest_reason().isEmpty()) {
			return "申請理由を入力して下さい";
		}
		return "";
	}


}
