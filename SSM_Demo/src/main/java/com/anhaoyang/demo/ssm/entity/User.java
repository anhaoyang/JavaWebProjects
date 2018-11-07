package com.anhaoyang.demo.ssm.entity;

import java.io.Serializable;

import javax.validation.constraints.Email;

import org.springframework.validation.annotation.Validated;
@Validated
public class User implements Serializable{
	private static final long serialVersionUID = 855038581358549926L;

	private Long id;

    private String name;
    
    @Email(message="非合法的邮箱")
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + "]";
	}
}