package com.tibco.be.mongoDB;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import static com.mongodb.client.model.Accumulators.avg;
import static com.mongodb.client.model.Accumulators.max;
import static com.mongodb.client.model.Accumulators.min;
import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Projections.computed;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;

import com.mongodb.client.model.BsonField;

public class AggregatesBuilder {

	
	public static List<Bson> build(String aggFunction,List<String> groupByColSet,String aggCol,List<Bson> aggegationpipeline){
		switch (aggFunction) {
		case "SUM":
			BsonField sum = sum(aggCol,"$"+aggCol);
			buildAggregationPipeline(sum, groupByColSet, aggegationpipeline);
			break;
		
		case "COUNT":
			BsonField count = sum("count",1);
			buildAggregationPipeline(count, groupByColSet, aggegationpipeline);			
			break;
		
		case "AVG":
			BsonField avg = avg(aggCol,"$"+aggCol);
			buildAggregationPipeline(avg, groupByColSet, aggegationpipeline);
			break;
		
		case "MAX":
			BsonField max = max(aggCol,"$"+aggCol);
			buildAggregationPipeline(max, groupByColSet, aggegationpipeline);
			break;
		
		case "MIN":
			BsonField min = min(aggCol,"$"+aggCol);
			buildAggregationPipeline(min, groupByColSet, aggegationpipeline);
			break;
		
		default:
			throw new RuntimeException("Aggregate function " + aggFunction + " not supported");
		}
		
		return aggegationpipeline;		
		
		
	}
	
	//If groupby column has multiple values then those are merged in single document using following function
	public static Bson formGroupByMultipleFields(List<String> groupByColSet){
		Document id = new Document();
	    for (String s : groupByColSet) {
	      id.append(s, "$" + s);
	    }
		
		return id;
	}
	
	//This function returns multiple groupby computed fields for aggregation result
	public static List<Bson> getComputedGroupByfields(List<String> groupByColSet){
		List<Bson> fieldList = new ArrayList<Bson>();
		
		for(String field:groupByColSet){
			fieldList.add(computed(field,"$_id."+field));
		}
		
		return fieldList;	
	}	
	
	//This function builds aggregation pipeline based on groupby column,aggregation column and aggregation function
	public static void buildAggregationPipeline(BsonField aggfunction,List<String> groupByColSet,List<Bson> aggegationpipeline){
		if(groupByColSet.isEmpty()){
			aggegationpipeline.add(group(null,aggfunction));
			if(aggfunction.getName().equalsIgnoreCase("count")) {
				aggegationpipeline.add(project(fields(excludeId(),computed(aggfunction.getName(), "$"+aggfunction.getName()))));
			}else
				aggegationpipeline.add(project(fields(excludeId(),computed(aggfunction.getName(), aggfunction.getValue()))));
		}
		else {
			aggegationpipeline.add(group(formGroupByMultipleFields(groupByColSet),aggfunction));
			List<Bson> computedlist = getComputedGroupByfields(groupByColSet);
			computedlist.add(excludeId());
			if(aggfunction.getName().equalsIgnoreCase("count")) {
				computedlist.add(computed(aggfunction.getName(), "$"+aggfunction.getName()));
			}
			else
				computedlist.add(computed(aggfunction.getName(), aggfunction.getValue()));
			aggegationpipeline.add(project(fields(computedlist)));
		}
	}
	
}
