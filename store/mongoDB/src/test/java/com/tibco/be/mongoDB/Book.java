/**
 * # Copyright (c) 2021-2022. TIBCO Software Inc.
 * # This file is subject to the license terms contained in the license file that is distributed with this file.
 */

package com.tibco.be.mongoDB;

import com.tibco.cep.kernel.model.entity.Id;

/**
 * Sample Book model for testing insert/update/delete/read operations in MongoDB
 */
public class Book {
	private static final Long RECORD_ID = 555l;

	private Id id;
	private String title;
	private String author;
	private double price;

	public Book(Object id) {
		this.id = (MongoDBProviderIntegrationTest.isLegacyID) ? Id.createId(Long.valueOf((String) id), null, 0)
				: Id.createId(0, (String) id, 0);
	}

	public Book() {
		id = (MongoDBProviderIntegrationTest.isLegacyID) ? Id.createId(RECORD_ID, null, 0)
				: Id.createId(0, RECORD_ID + "", 0);
	}

	public Id getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getPrice() {
		return price;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof Book))
			return false;

		Book otherBook = (Book) obj;
		if (this == otherBook)
			return true;

		if (this.getId().equals(otherBook.getId()) && this.getTitle().equals(otherBook.getTitle())
				&& this.getAuthor().contentEquals(otherBook.getAuthor()) && this.getPrice() == otherBook.getPrice())
			return true;

		return false;
	}

	@Override
	public int hashCode() {
		return getId().hashCode() + getTitle().hashCode() + getAuthor().hashCode();
	}
}
