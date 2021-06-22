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

	
	public static List<Bson> build(String aggFunction,List<String> groupByColSet,String aggCol,List<Bson> aggegationpipeline)
	{
		switch (aggFunction) {
		case "SUM":
			BsonField sum = sum(aggCol,"$"+aggCol);
			if(groupByColSet.isEmpty())
			{
				aggegationpipeline.add(group(null,sum));
				aggegationpipeline.add(project(fields(excludeId(),computed(aggCol, "$"+aggCol))));
			}
			else {
				aggegationpipeline.add(group(formGroupByMultipleFields(groupByColSet),sum));
				List<Bson> computedlist = getComputedGroupByfields(groupByColSet);
				computedlist.add(excludeId());
				computedlist.add(computed(aggCol, "$"+aggCol));
				aggegationpipeline.add(project(fields(computedlist)));
			}
			break;
		case "COUNT":
			BsonField count = sum("count",1);
			
			if(groupByColSet.isEmpty())
			{
				aggegationpipeline.add(group(null,count));
				aggegationpipeline.add(project(fields(excludeId(),computed("count", "$count"))));
			}
			else {
				aggegationpipeline.add(group(formGroupByMultipleFields(groupByColSet),count));
				List<Bson> computedlist = getComputedGroupByfields(groupByColSet);
				computedlist.add(excludeId());
				computedlist.add(computed("count", "$count"));
				aggegationpipeline.add(project(fields(computedlist)));
			}
			break;
		case "AVG":
			BsonField avg = avg(aggCol,"$"+aggCol);
			if(groupByColSet.isEmpty())
			{
				aggegationpipeline.add(group(null,avg));
				aggegationpipeline.add(project(fields(excludeId(),computed(aggCol, "$"+aggCol))));
			}
			else {
				aggegationpipeline.add(group(formGroupByMultipleFields(groupByColSet),avg));
				List<Bson> computedlist = getComputedGroupByfields(groupByColSet);
				computedlist.add(excludeId());
				computedlist.add(computed(aggCol, "$"+aggCol));
				aggegationpipeline.add(project(fields(computedlist)));
			}
			break;
		case "MAX":
			BsonField max = max(aggCol,"$"+aggCol);
			if(groupByColSet.isEmpty())
			{
				aggegationpipeline.add(group(null,max));
				aggegationpipeline.add(project(fields(excludeId(),computed(aggCol, "$"+aggCol))));
			}
			else {
				aggegationpipeline.add(group(formGroupByMultipleFields(groupByColSet),max));
				List<Bson> computedlist = getComputedGroupByfields(groupByColSet);
				computedlist.add(excludeId());
				computedlist.add(computed(aggCol, "$"+aggCol));
				aggegationpipeline.add(project(fields(computedlist)));
			}
			break;
		case "MIN":
			BsonField min = min(aggCol,"$"+aggCol);
			if(groupByColSet.isEmpty())
			{
				aggegationpipeline.add(group(null,min));
				aggegationpipeline.add(project(fields(excludeId(),computed(aggCol, "$"+aggCol))));
			}
			else {
				aggegationpipeline.add(group(formGroupByMultipleFields(groupByColSet),min));
				List<Bson> computedlist = getComputedGroupByfields(groupByColSet);
				computedlist.add(excludeId());
				computedlist.add(computed(aggCol, "$"+aggCol));
				aggegationpipeline.add(project(fields(computedlist)));
			}
			break;
		default:
			throw new RuntimeException("Aggregate function " + aggFunction + " not supported");
		}
		
		return aggegationpipeline;		
		
		
	}
	
	
	public static Bson formGroupByMultipleFields(List<String> groupByColSet)
	{
		Document id = new Document();
	    for (String s : groupByColSet) {
	      id.append(s, "$" + s);
	    }
		
		return id;
	}
	
	public static List<Bson> getComputedGroupByfields(List<String> groupByColSet)
	{
		List<Bson> fieldList = new ArrayList<Bson>();
		
		for(String field:groupByColSet)
		{
			fieldList.add(computed(field,"$_id."+field));
		}
		
		return fieldList;
		
		
	}	
	
}
