package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	//method for adding common data to response
	@ModelAttribute
	public void addCommonData(Model model,Principal principal) {
		String username = principal.getName();
		System.out.println("USERNAME = " + username);
		
		//get the user using username(Email)
		User user= userRepository.getUserByUserName(username);
		model.addAttribute("user", user);
	}
	
	//dashboard home
	@RequestMapping("/index")
	public String dashboard(Model model,Principal principal) {
		return "normal/user_dashboard";
	}
	
	//open add contact handler
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model) {
		model.addAttribute("title","Add Contact");
		model.addAttribute("contact",new Contact());
		return "normal/add_contact_form";
	}
	
	//processing add-contact form
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,Principal principal) {
		
		try {
			String name = principal.getName();
			User user=this.userRepository.getUserByUserName(name);
			contact.setUser(user);
			
			if (!file.isEmpty()) {
		        String filename = file.getOriginalFilename();
		        contact.setImage(filename);
		        File saveFile=new ClassPathResource("static/img").getFile();
		        Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
		        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		    } else {
		        System.out.println("File is empty");
		    }
			user.getContacts().add(contact);
			this.userRepository.save(user);
			System.out.println("DATA :" + contact);
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		
		return "normal/add_contact_form";
	}
}
