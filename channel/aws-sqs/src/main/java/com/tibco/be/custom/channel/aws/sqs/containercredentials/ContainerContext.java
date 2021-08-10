package com.tibco.be.custom.channel.aws.sqs.containercredentials;

public class ContainerContext {
	
	private String regionName;
	private String queueUrl;
	
	
	public String getQueueUrl() { return queueUrl; }

	public void setQueueUrl(String queueUrl) { this.queueUrl = queueUrl; }

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}


	private ContainerContext(ContainerContextBuilder builder) {
		this.queueUrl = builder.queueUrl;
		this.regionName = builder.regionName;
	}
	
	public static class ContainerContextBuilder {

	    private String queueUrl;
	    private String regionName;

	    public ContainerContextBuilder() {
	    	
	    }

	    public ContainerContextBuilder(String regionName,String queueUrl) {
	      this.queueUrl = queueUrl;
	      this.regionName = regionName;
	    }

	    public ContainerContext.ContainerContextBuilder setQueueUrl(String queueUrl) {
	      this.queueUrl = queueUrl;
	      return this;
	    }
	    public ContainerContext.ContainerContextBuilder setRegionName(String regionName) {
	      this.regionName = regionName;
	      return this;
	    }

	    public ContainerContext build() {
	      return new ContainerContext(this);
	    }
	}

}
