package com.tibco.metric.store.elasticsearch;

import com.tibco.cep.kernel.model.entity.Id;

/**
 * Sample Book model for testing index/search/delete operations in Elasticsearch
 */
public class Book {
	private static final Long RECORD_ID = 555l;
	
	private Id id;
	private String title;
	private String author;
	
	public Book(Object id) {
		this.id = (ElasticSearchMetricStoreProviderIntegrationTest.isLegacyID) ? Id.createId(Long.valueOf((String)id), null, 0) : Id.createId(0,(String)id, 0);
	}
	
	public Book() {
		id = (ElasticSearchMetricStoreProviderIntegrationTest.isLegacyID) ? Id.createId(RECORD_ID, null, 0) : Id.createId(0, RECORD_ID+"", 0);
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
		
		if (this.getId().equals(otherBook.getId()) && this.getTitle().equals(otherBook.getTitle()) && this.getAuthor().contentEquals(otherBook.getAuthor())) return true;
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return getId().hashCode() + getTitle().hashCode() + getAuthor().hashCode();
	}
}
