package com.usedpic.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.usedpic.model.UsedPicService;

@RestController
@RequestMapping("/usedpic")
public class UsedPicController {

	@Autowired
	UsedPicService usedPicSvc;

	@PostMapping("/deleteOneUsedPic")
	public ResponseEntity<String> deleteSpecificOneUsedPic( @RequestParam("usedPicNo") String usedPicNo, Model model) {
		try {
	        Integer usedPicNoWantsDelete = Integer.valueOf(usedPicNo);

	        String result = usedPicSvc.deleteUsedPicById(usedPicNoWantsDelete);
	        if (result.equals("true")) {
	            return ResponseEntity.ok("UsedPic deleted successfully.");
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body("UsedPic not found or could not be deleted.");
	        }
	    } catch (NumberFormatException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body("Invalid usedPicNo format. Please provide a valid number.");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("An unexpected error occurred. Please try again later.");
	    }
	}
}
