/*
* Copyright © 2020. TIBCO Software Inc.
* This file is subject to the license terms contained
* in the license file that is distributed with this file.
*/

package com.tibco.be.store.cassandra;

/**
 * Sample Book model for testing index/search/delete operations in Cassandra
 */
public class Book {
	
	private Integer id;
	private String title;
	private String author;
	
	public Book(int id) {
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof Book)) return false;
		
		Book otherBook = (Book) obj;
		if (this == otherBook) return true;
		
		if (this.id==otherBook.id && this.getTitle().equals(otherBook.getTitle()) && this.getAuthor().contentEquals(otherBook.getAuthor())) return true;
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.getId().hashCode() + getTitle().hashCode() + getAuthor().hashCode();
	}
}
