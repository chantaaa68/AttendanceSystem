package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.Form.DeleteForm;
import com.example.demo.Form.EmployeeForm;
import com.example.demo.Form.SortForm;
import com.example.demo.Model.EmployeeModel;
import com.example.demo.Service.EmployeeService;

@Controller
public class EmployeeController {
	
	@Autowired
	EmployeeService service;
	
	//労働者全表示用
	@RequestMapping("/UserIndex")
	public String AllSelect(Model model) {
		//削除実行の際に必要なものを渡しておく
		model.addAttribute("deleteForm",new DeleteForm());
		
		//検索用
		model.addAttribute("sortForm",new SortForm());
		
		//全体表示用のList
		List<EmployeeModel> employeeList = service.AllEmployee();
		model.addAttribute("employeeList", employeeList);
		
		return "userIndex";
	}
	
	//労働者詳細
	@RequestMapping("/EmployeeInfo/{id}")
	public String EmployeeInformation(@PathVariable Integer id, Model model) {
		
		EmployeeModel singleModel = service.SingleEmployeeModel(id);
		
		model.addAttribute("singleModel",singleModel);
		
		return "employeeInfo";
	}
	
	//労働者ソート検索
	@RequestMapping("/Sort")
	public String Sort(@ModelAttribute SortForm form, Model model) {
		//削除実行の際に必要なものを渡しておく
		model.addAttribute("deleteForm",new DeleteForm());
		
		//検索用
		model.addAttribute("sortForm",new SortForm());
		
		if(!(form.getName().isEmpty())) {
			//全体表示用のList
			List<EmployeeModel> employeeList = service.LastNameEmployee(form.getName());
			model.addAttribute("employeeList", employeeList);
		}else {
			//全体表示用のList
			List<EmployeeModel> employeeList = service.AllEmployee();
			model.addAttribute("employeeList", employeeList);
		}
		return "userIndex";
	}
	
	//労働者登録画面遷移用
	@RequestMapping("/RegistrationForm")
	public String EmployeeRegistrationForm(Model model) {
		
		model.addAttribute("employeeForm",new EmployeeForm());
		
		return "Registration";
	}
	
	//労働者登録処理
	@RequestMapping("/EmployeeRegistration/{id}")
	public String EmployeeRegister(@ModelAttribute EmployeeForm form,@PathVariable Integer id, Model model) {
		
		String result = service.RegistEmployee(form, id);
		
		model.addAttribute("reaction",result);
		
		return "Registration";
	}
	
	//労働者更新画面遷移用
	@RequestMapping("/UpdateForm/{id}")
	public String EmployeeUpdateForm(@PathVariable Integer id, Model model) {
		
		EmployeeForm form = service.SingleEmployeeForm(id);
		
		model.addAttribute("updateForm",form);
		
		return "update";
	}
	
	//労働者更新処理
	@RequestMapping("/Update/{id}")
	public String EmployeeUpdate(@ModelAttribute EmployeeForm form,@PathVariable Integer id, Model model) {
		
		String result = service.UpdateEmployee(form, id);
		
		model.addAttribute("reaction",result);
		
		EmployeeInformation(form.getId(),model);
		
		//個別の情報一覧に移動。ちゃんと更新されたか確認。
		return "employeeInfo";
	}
	
	//労働者削除処理
	@RequestMapping("/EmployeeDelete")
	public String EmployeeDelete(@ModelAttribute DeleteForm form, Model model) {
		
		if(form.getEmployeeId() !=null) {
		
		String result = service.DeleteEmployee(form.getEmployeeId(), form.getAuthorityId());
		
		model.addAttribute("reaction",result);
		
		}
		
		
		//労働者全体表示
		AllSelect(model);
		//検索用
		model.addAttribute("sortForm",new SortForm());
		
		return "userIndex";
	}

}
