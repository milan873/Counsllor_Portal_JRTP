package in.ashokit.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.ashokit.dto.DashboardResponse;
import in.ashokit.entity.Counsellor;
import in.ashokit.entity.Enquiry;
import in.ashokit.repos.CounsellorRepo;
import in.ashokit.repos.EnquriyRepo;

@Service
public class CounsellorServiceImpl implements counsellorService {

	@Autowired
	private CounsellorRepo counsellorRepo;
	
	@Autowired
	private EnquriyRepo enqRepo;
	
	
	
	@Override
	public Counsellor findByEmail(String email) {
		return counsellorRepo.findByEmail(email);
	}

	@Override
	public boolean register(Counsellor counsellor) {
		Counsellor savedCounsellor = counsellorRepo.save(counsellor);
		if(null != savedCounsellor.getCounsellorId()) {
			return true;
		}
		return false;
	}

	@Override
	public Counsellor login(String email, String pwd) {
		Counsellor counsellor = counsellorRepo.findByEmailAndPwd(email, pwd);
		return counsellor;
	}

	@Override
	public DashboardResponse getDashboardInfo(Integer counsellorId) {
		
		DashboardResponse response = new DashboardResponse();
		List<Enquiry> enqList = enqRepo.getEnquriesByCounsellorId(counsellorId);
		
		int totalEnq = enqList.size();
		
	 int enrolledEnq = enqList.stream()
				.filter(e -> e.getEnqStatus().equals("Enrolled"))
				.collect(Collectors.toList())
				.size();
	 
	 int lostEnq = enqList.stream()
				.filter(e -> e.getEnqStatus().equals("Lost"))
				.collect(Collectors.toList())
				.size();
	 
	 int openEnq = enqList.stream()
				.filter(e -> e.getEnqStatus().equals("Open"))
				.collect(Collectors.toList())
				.size();
	 
	 response.setTotalEnqs(totalEnq);
	 response.setEnrolledEnqs(enrolledEnq);
	 response.setLostEnqs(lostEnq);
	 response.setOpenEnqs(openEnq);
		return response;
	}

}
