package in.ashokit.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.ashokit.dto.ViewEnqsFilterRequest;
import in.ashokit.entity.Enquiry;
import in.ashokit.service.enquiryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class EnquiryController {

	private enquiryService enqService;

	public EnquiryController(enquiryService enqService) {
		this.enqService = enqService;
	}
	
	
	@PostMapping("/filter-enqs")
	public String filterEnquries(ViewEnqsFilterRequest viewEnqsFilterRequest, HttpServletRequest req, Model model) {
		// get existing session obj
		HttpSession session = req.getSession(false);
		Integer counsellorId = (Integer) session.getAttribute("counsellorId");
		
		List<Enquiry> enqList = enqService.getEnquiresWithFilter(viewEnqsFilterRequest, counsellorId);
		
		model.addAttribute("enquries", enqList);
		
		
		return "viewEnqPage";
		
	}
	
	
	@GetMapping("/view-enquries")
	public String getEnquries(HttpServletRequest request, Model model) {
		// get existing session obj
				HttpSession session = request.getSession(false);
				Integer counsellorId = (Integer) session.getAttribute("counsellorId");
				
				List<Enquiry> enqList = enqService.getAllEnquiries(counsellorId);
				model.addAttribute("enquries", enqList);
				
				ViewEnqsFilterRequest filterReq = new ViewEnqsFilterRequest();
				model.addAttribute("viewEnqsFilterRequest",filterReq);
				
				return "viewEnqPage";
	}
	
	@GetMapping("/enquiry")
	public String addEnquiryPage(Model model) {
		Enquiry enqObj = new Enquiry();
		model.addAttribute("enq", enqObj);
		return "enquiryForm";
	}
	
	@GetMapping("/editEnq")
	public String editEnquiry(@RequestParam("enqId") Integer enqId ,Model model) {
		
		Enquiry enquiry = enqService.getEnquiryById(enqId);
		model.addAttribute("enq", enquiry);
		
		return "enquiryForm";
	}
	
	@PostMapping("/addEnq")
	public String handleAddEnquiry(@ModelAttribute("enq") Enquiry enq, HttpServletRequest req, Model model) throws Exception {

		// get existing session obj
		HttpSession session = req.getSession(false);
		Integer counsellorId = (Integer) session.getAttribute("counsellorId");

		boolean isSaved = enqService.addEnquiry(enq, counsellorId);
		if (isSaved) {
			model.addAttribute("smsg", "Enquiry Added");

		} else {
			model.addAttribute("emsg", "Failed to Add Enquiry");
		}

		return "enquiryForm";
	}

}
