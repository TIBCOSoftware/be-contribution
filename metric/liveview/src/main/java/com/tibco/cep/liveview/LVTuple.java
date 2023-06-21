package com.tibco.cep.liveview;

import com.streambase.sb.Tuple;

/**
 * 
 * @author shivkumarchelwa
 *
 */
public class LVTuple {

	private Tuple tuple;
	private String tableName;

	public LVTuple(Tuple tuple, String tableName) {
		this.tuple = tuple;
		this.tableName = tableName;
	}

	public Tuple getTuple() {
		return tuple;
	}

	public String getTableName() {
		return tableName;
	}
}
