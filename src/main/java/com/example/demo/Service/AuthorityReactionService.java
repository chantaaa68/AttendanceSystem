package com.example.demo.Service;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.AttendanceEntity;
import com.example.demo.Form.RequestForm;
import com.example.demo.Model.AttendanceModel;
import com.example.demo.Model.RequestModel;
import com.example.demo.Repository.AttendanceRepository;
import com.example.demo.Repository.EmployeeRepository;
import com.example.demo.Repository.RequestRepository;

@Service
public class AuthorityReactionService {
	
	@Autowired
	RequestRepository rep;
	
	@Autowired
	EmployeeRepository eRep;
	
	@Autowired
	AttendanceRepository aRep;
	
	AttendanceEntity entity = new AttendanceEntity();
	
	AttendanceModel attendanceModel = new AttendanceModel();
	
	//全権取得
	public List<RequestModel> AllUserRequest() throws ParseException{
		
		List<Map<String,Object>> list = rep.RequestAll();
		
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
	public RequestModel SingleRequestModel(Integer id) {
		
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
	
	//申請内容を登録する
	public String RequestReaction(RequestModel model,Integer id) {
		 
		//申請時点での日付をformに入力する
		model.setReaction_date(Today());
		
		//申請に対する対応をした人のIDを登録
		model.setReaction_id(id);
		
		//名前も入力
		model.setReaction_name(RequestIdChanger(id));
		
		//コメントがないと更新ができない
		if(model.getReaction_comment().isEmpty()) {
			return "コメントは必ず記入してください";
		}else {
			rep.RequestReaction(model);
			return "対応が完了しました";
		}
	}
	
	//差し戻し用
	public String CanNotAccept(RequestModel model,Integer id) {
		model.setReaction_flg("3");
		
		RequestReaction(model,id);
		
		String reaction = "申請番号"+ model.getRequest_id() +"を差し戻しました";
		
		return reaction;
	}
	
	//勤怠の修正の為のModelを受け取る
	public AttendanceModel AttendanceReaction(RequestModel model,Integer id) {
		
		//フロント用にModelに変換
		List<Map<String,Object>> todayHistory = 
				aRep.TodaysHistory(model.getEmployee_id(), model.getSelected_date());
		AttendanceEntity attendance = entity.ConvertToFrom(todayHistory);
		AttendanceModel setModel = attendanceModel.ConvertToFrom(attendance);
		
		model.setReaction_flg("1");

		//申請内容を登録する
		RequestReaction(model,id);
		
		return setModel;
	}

	//勤怠修正
	public String AttendanceCorrection(AttendanceModel model,Integer id) {
		//対応者の名前を持ってくる
		String name = RequestIdChanger(id);
		
		//有給申請に対するコメントを記載する
		String comment = Today() + "に、" + model.getComment() +  name + "が修正しました";
		
		model.setComment(comment);
		
		//時間単位・分単位の値を作成して格納
		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		
		 LocalTime startTime = LocalTime.parse(model.getStart_time(), formatter);
	     LocalTime endTime = LocalTime.parse(model.getEnd_time(), formatter);
	     LocalTime restStartTime = LocalTime.parse(model.getRest_start_time(), formatter);
	     LocalTime restEndTime = LocalTime.parse(model.getRest_end_time(), formatter);
	     
       int totalWorkMinutes = (int) ChronoUnit.MINUTES.between(startTime, endTime);
       int breakMinutes = (int) ChronoUnit.MINUTES.between(restStartTime, restEndTime);
       int netWorkMinutes = totalWorkMinutes - breakMinutes;
       int additonalWorkMinutes = netWorkMinutes-480;
       
       //労働時間格納用
       int workHours = netWorkMinutes / 60;
       int workMinutes = netWorkMinutes % 60;
       String workTime = workHours + ":"+ workMinutes;
       
       //休憩時間格納用
       int restHours = breakMinutes / 60;
       int restMinutes = breakMinutes % 60;
       String restTime = restHours + ":" + restMinutes;
       
       //残業時間格納用
       int additionalHours = additonalWorkMinutes / 60;
       int additonalMinutes = additonalWorkMinutes % 60;
       String additionalTime = additionalHours +":" + additonalMinutes;
       
       model.setWork_total_time(workTime);
       model.setRest_total_time(restTime);
       
       if(additonalWorkMinutes >=0) {
       	model.setAdditional_total_time(additionalTime);
       }
       //総労働時間の入力
       model.setTotal_work_minute(netWorkMinutes);
		
		//確定処理
		aRep.Correction(model);
		
		return "修正完了";
	}
	
	//新規登録用の対応処理
	public String RegisterReaction(RequestModel model,Integer id) {
		model.setReaction_flg("1");

		//申請内容を登録する
		return RequestReaction(model,id);
	}
	
	//更新用の対応処理
	public String UpdateReaction(RequestModel model,Integer id) {
		model.setReaction_flg("1");

		//申請内容を登録する
		return RequestReaction(model,id);
	}
	
	//有給申請を登録する
	public String HappyRequestReaction(RequestModel model,Integer id) {
		model.setReaction_flg("2");

		//申請内容を登録する
		RequestReaction(model,id);
		
		//有給申請に対するコメントを記載する
		String comment = model.getRequest_date() + "に申請があり、" + Today() + "に" +
						 model.getReaction_name() + "が有給承認しました。申請ID = " + model.getRequest_id();
		
		//申請用のフラグを作成
		String flg = "3";
		
		//有給登録作業
		aRep.HappyRegister(model, comment, flg);
		
		//有給日数調整
		eRep.HappyDaysUpdate(model.getEmployee_id(),id,ReduceHappyDays(model.getEmployee_id()));
		
		
		return "有給を承認しました";
	}
	
	//午前有給申請を登録する
	public String AMHappyRequestReaction(RequestModel model,Integer id) {
		model.setReaction_flg("2");
		
		//申請内容を登録する
		RequestReaction(model,id);
		
		//有給申請に対するコメントを記載する
		String comment = model.getRequest_date() + "に申請があり、" + Today() + "に" +
						 model.getReaction_name() + "が午前給承認しました。申請ID = " + model.getRequest_id();
		
		//申請用のフラグを作成
		String flg = "4";
		
		//有給登録作業
		aRep.HappyRegister(model, comment, flg);
		
		//有給日数調整
		eRep.HappyDaysUpdate(model.getEmployee_id(),id,HalfReduceHappyDays(model.getEmployee_id()));
				
		
		return "午前休を承認しました";
	}
	
	//午後有給申請を登録する
	public String PMHappyRequestReaction(RequestModel model,Integer id) {
		model.setReaction_flg("2");
		
		//申請内容を登録する
		RequestReaction(model,id);
		
		//有給申請に対するコメントを記載する
		String comment = model.getRequest_date() + "に申請があり、" + Today() + "に" +
						 model.getReaction_name() + "が午後給承認しました。申請ID = " + model.getRequest_id();
		
		//申請用のフラグを作成
		String flg = "5";
		
		//有給登録作業
		aRep.HappyRegister(model, comment, flg);
		
		//有給日数調整
		eRep.HappyDaysUpdate(model.getEmployee_id(),id,HalfReduceHappyDays(model.getEmployee_id()));
		
		return "午後休を承認しました";
	}
	
	//後日有給申請を登録する
	public String AfterHappyRequestReaction(RequestModel model,Integer id) {
		model.setReaction_flg("2");
		
		//申請内容を登録する
		RequestReaction(model,id);
		
		//有給申請に対するコメントを記載する
		String comment = model.getRequest_date() + "に申請があり、" + Today() + "に" +
						 model.getReaction_name() + "が後日有給承認しました。申請ID = " + model.getRequest_id();
		
		//申請用のフラグを作成
		String flg = "3";
		
		//有給登録作業
		aRep.HappyRegister(model, comment, flg);
		
		//有給日数調整
		eRep.HappyDaysUpdate(model.getEmployee_id(),id,ReduceHappyDays(model.getEmployee_id()));
				
		
		return "後日有給を承認しました";
	}
	
	//今日の日付を取得する
	public String Today() {
		//申請時点での日付をformに入力する
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年 MM月 dd日");
		String today = dateFormat.format(date);
		
		return today;
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
	//フラグ内容を日本語に
	
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
	
	//フラグ内容を日本語に
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

	//該当ユーザーの有給の日数を取得して減らします
	public float ReduceHappyDays(Integer id) {
		//申請者の有給日数を1日減らす
		List<Map<String,Object>> list = eRep.SingleEmployeeHappyDays(id);
		
		float happyDays = 0;
		
		for(Map<String,Object> map:list) {
			happyDays += (float)(map.get("happy_days"));
		}
		
		happyDays -= 1;
		
		return happyDays;
	}
	
	//該当ユーザーの有給の日数を取得して減らします（半休）
	public float HalfReduceHappyDays(Integer id) {
		//申請者の有給日数を1日減らす
		List<Map<String,Object>> list = eRep.SingleEmployeeHappyDays(id);
		
		float happyDays = 0;
		
		for(Map<String,Object> map:list) {
			happyDays += (float)(map.get("happy_days"));
		}
		
		happyDays -= 0.5;
		
		return happyDays;
	}


}
