package com.example.demo.Form;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

import com.example.demo.Entity.RequestEntity;

import lombok.Data;

@Data
public class RequestForm implements Serializable{
	
	@Id
	private Integer request_id;
	
	private Integer employee_id;
	
	private String request_date;
	
	private String selected_date;
	
	private String request_flg;
	
	private String request_reason;
	
	private Integer reaction_id;
	
	private String reaction_date;
	
	private String reaction_comment;
	
	private String reaction_flg;
	
	public RequestForm() {
		
	}
	
	public RequestForm(Integer request_id, Integer employee_id, String request_date, String selected_date,
			String request_flg, String request_reason, Integer reaction_id, String reaction_date,
			String reaction_comment, String reaction_flg) {
		
	    this.request_id = request_id;
	    this.employee_id = employee_id;
	    this.request_date = request_date;
	    this.selected_date = selected_date;
	    this.request_flg = request_flg;
	    this.request_reason = request_reason;
	    this.reaction_id = reaction_id;
	    this.reaction_date = reaction_date;
	    this.reaction_comment = reaction_comment;
	    this.reaction_flg = reaction_flg;
	}

	public RequestForm ConvertToFrom(RequestEntity entity) {
		return new RequestForm(
				entity.getRequest_id(),
				entity.getEmployee_id(),
				entity.getRequest_date(),
				entity.getSelected_date(),
				entity.getRequest_flg(),
				entity.getRequest_reason(),
				entity.getReaction_id(),
				entity.getReaction_date(),
				entity.getReaction_comment(),
				entity.getReaction_flg()
				);

	}

}
