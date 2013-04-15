package com.successfactors.library.shared.model;

import java.io.Serializable;
import java.util.Date;

public class SLBookReview implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4688656576934914502L;

	private String bookISBN;
	private String userEmail;
	
	private String title;
	private String subTitle;
	private String content;
	private Date postDate;
	
	public SLBookReview() {
		
	}

	public String getBookISBN() {
		return bookISBN;
	}

	public void setBookISBN(String bookISBN) {
		this.bookISBN = bookISBN;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getPostDate() {
		return postDate;
	}

	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}
	
}