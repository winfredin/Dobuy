package com;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontEndController {
	 @GetMapping("member")
	    public String getMemberPage() {
	        return "front-end/member"; // 對應 templates/content/profile.html
	    }
	    
	    @GetMapping("content/profile")
	    public String getProfilePage() {
	        return "content/profile"; // 對應 templates/content/profile.html
	    }
	    @GetMapping("content/credit")
	    public String getcreditPage() {
	        return "content/credit"; // 對應 templates/content/profile.html
	    }
	    @GetMapping("content/changeps")
	    public String getchangepsPage() {
	        return "content/changeps"; // 對應 templates/content/profile.html
	    }
	    @GetMapping("content/delete")
	    public String getdeletePage() {
	        return "content/delete"; // 對應 templates/content/profile.html
	    }
	    @GetMapping("content/add")
	    public String getaddPage() {
	        return "content/add"; // 對應 templates/content/profile.html
	    }
}
