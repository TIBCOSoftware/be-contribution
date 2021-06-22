package com.tibco.be.mongoDB;

import com.tibco.cep.store.custom.StoreDataTypeMapper;


/**
 * @author TIBCO Software
 * 
 * This class maps BE data types to mongoDB store data types.
 * 
 */

public class MongoDBDataTypeMapper extends StoreDataTypeMapper {
	
private static MongoDBDataTypeMapper mongodbDataMapper = new MongoDBDataTypeMapper();
	
	public MongoDBDataTypeMapper() {
		
	}

	@Override
	protected Object getBooleanType() {
		return "BOOLEAN";
	}

	@Override
	protected Object getDateTimeType() {
		return "DATETIME";
	}

	@Override
	protected Object getDoubleType() {
		return "DOUBLE";
	}

	@Override
	protected Object getFloatType() {
		return "FLOAT";
	}

	@Override
	protected Object getIntegerType() {
		return "INTEGER";
	}

	@Override
	protected Object getLongType() {
		return "LONG";
	}

	@Override
	protected Object getObjectType() {
		return "OBJECT";
	}

	@Override
	protected Object getShortType() {
		return "SHORT";
	}

	@Override
	protected Object getStringType() {
		return "STRING";
	}
	
	public static MongoDBDataTypeMapper getInstance() {
		return mongodbDataMapper;
		
	}
	
	
	
	

}
