package com.doan.AppTuyenDung.entity;
import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "statusCode", referencedColumnName = "code")
	@JsonIgnore
    private CodePostStatus statusCode;

    private String timeEnd;
    private String timePost;
    private Integer isHot;
    private java.util.Date createdAt;
    private java.util.Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "detailPostId")
    private DetailPost detailPost;

    @OneToMany(mappedBy = "post")
    private Set<Cv> cvs;

    @OneToMany(mappedBy = "post")
    private Set<Note> notes;

   
	public Post(Integer id, CodePostStatus statusCode, String timeEnd, String timePost, Integer isHot, Date createdAt,
			Date updatedAt, User user, DetailPost detailPost, Set<Cv> cvs, Set<Note> notes) {
		this.id = id;
		this.statusCode = statusCode;
		this.timeEnd = timeEnd;
		this.timePost = timePost;
		this.isHot = isHot;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.user = user;
		this.detailPost = detailPost;
		this.cvs = cvs;
		this.notes = notes;
	}


	public Post() {
    }


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public CodePostStatus getStatusCode() {
		return statusCode;
	}


	public void setStatusCode(CodePostStatus statusCode) {
		this.statusCode = statusCode;
	}


	public String getTimeEnd() {
		return timeEnd;
	}


	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}


	public String getTimePost() {
		return timePost;
	}


	public void setTimePost(String timePost) {
		this.timePost = timePost;
	}


	public Integer getIsHot() {
		return isHot;
	}


	public void setIsHot(Integer isHot) {
		this.isHot = isHot;
	}


	public java.util.Date getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(java.util.Date createdAt) {
		this.createdAt = createdAt;
	}


	public java.util.Date getUpdatedAt() {
		return updatedAt;
	}


	public void setUpdatedAt(java.util.Date updatedAt) {
		this.updatedAt = updatedAt;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public DetailPost getDetailPost() {
		return detailPost;
	}


	public void setDetailPost(DetailPost detailPost) {
		this.detailPost = detailPost;
	}


	public Set<Cv> getCvs() {
		return cvs;
	}


	public void setCvs(Set<Cv> cvs) {
		this.cvs = cvs;
	}


	public Set<Note> getNotes() {
		return notes;
	}


	public void setNotes(Set<Note> notes) {
		this.notes = notes;
	}



    // Getters and Setters
    
}