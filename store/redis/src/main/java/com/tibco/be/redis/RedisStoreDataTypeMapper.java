/**
 * # Copyright (c) 2019-2020. TIBCO Software Inc.
 * # This file is subject to the license terms contained in the license file that is distributed with this file.
 */
package com.tibco.be.redis;

import com.tibco.cep.store.custom.StoreDataTypeMapper;

/**
 * @author TIBCO Software
 * 
 * This class maps BE data types to redis store data types.
 * 
 */
public class RedisStoreDataTypeMapper extends StoreDataTypeMapper {

	private static RedisStoreDataTypeMapper redisDataMapper = new RedisStoreDataTypeMapper();
	
	public RedisStoreDataTypeMapper() {
		
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
	
	public static RedisStoreDataTypeMapper getInstance() {
		return redisDataMapper;
		
	}
}
