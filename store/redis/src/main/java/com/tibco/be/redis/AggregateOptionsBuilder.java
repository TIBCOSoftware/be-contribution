/**
 * # Copyright (c) 2019-2020. TIBCO Software Inc.
 * # This file is subject to the license terms contained in the license file that is distributed with this file.
 */
package com.tibco.be.redis;

import java.util.List;

import com.redislabs.lettusearch.aggregate.AggregateOptions;
import com.redislabs.lettusearch.aggregate.Group;
import com.redislabs.lettusearch.aggregate.Operation;
import com.redislabs.lettusearch.aggregate.Reducer;
import com.redislabs.lettusearch.aggregate.reducer.Avg;
import com.redislabs.lettusearch.aggregate.reducer.Count;
import com.redislabs.lettusearch.aggregate.reducer.Max;
import com.redislabs.lettusearch.aggregate.reducer.Min;
import com.redislabs.lettusearch.aggregate.reducer.Sum;

/**
 * 
 * @author rakulkar
 *
 */
public class AggregateOptionsBuilder {
	
	public static AggregateOptions build(String aggFunction,List<String> groupByColSet,String aggCol)
	{
		Reducer reducer = null;
		switch (aggFunction) {
		case "SUM":
			reducer = Sum.builder().property(aggCol).as(aggFunction).build();
			break;
		case "COUNT":
			reducer = Count.builder().as(aggFunction).build();
			break;
		case "AVG":
			reducer = Avg.builder().property(aggCol).as(aggFunction).build();
			break;
		case "MAX":
			reducer = Max.builder().property(aggCol).as(aggFunction).build();
			break;
		case "MIN":
			reducer = Min.builder().property(aggCol).as(aggFunction).build();
			break;
		default:
			throw new RuntimeException("Aggregate function " + aggFunction + " not supported");
		}
		Operation operation;
		if (groupByColSet.isEmpty()) {
			operation = Group.builder().reducer(reducer).build();
		}
		else
		{
			operation = Group.builder().properties(groupByColSet).reducer(reducer).build();
		}
		AggregateOptions aggregateOptions = AggregateOptions.builder().operation(operation).build();
		return aggregateOptions;
	}

}
