/**
 * 
 */
package com.tibco.cep.driver.kafkastreams;

import com.tibco.be.model.rdf.RDFTypes;

/**
 * @author shivkumarchelwa
 *
 */
public class BEKafkaStreamsConstants {

	public static final String DURATION_UNIT_NANOS = "Nanoseconds";
	public static final String DURATION_UNIT_MILLIS = "Milliseconds";
	public static final String DURATION_UNIT_SECONDS = "Seconds";
	public static final String DURATION_UNIT_MINUTES = "Minutes";
	public static final String DURATION_UNIT_HOURS = "Hours";
	public static final String DURATION_UNIT_DAYS = "Days";

	// KStream Stateless Transformations
	public static final String KSTREAM_FILTER = "Filter";
	public static final String KSTREAM_FILTER_NOT = "FilterNot";
	public static final String KSTREAM_MAP_VALUES = "MapValues";
	public static final String KSTREAM_FLAT_MAP_VALUES = "FlatMapValues";
	public static final String KSTREAM_SELECT_KEY = "SelectKey";
	public static final String KSTREAM_GROUP_BY_KEY = "GroupByKey";
	public static final String KSTREAM_INNER_JOIN = "Join";
	public static final String KSTREAM_LEFT_JOIN = "LeftJoin";
	public static final String KSTREAM_OUTER_JOIN = "OuterJoin";

	// KGroupedStream Transformations
	public static final String KGROUPEDSTREAM_COUNT = "Count";
	public static final String KGROUPEDSTREAM_AGGREGATE = "Aggregate";
	public static final String KGROUPEDSTREAM_REDUCE = "Reduce";

	// KTable Transformations
	public static final String KTABLE_TO_STREAM = "ToStream";

	public static final String INPUTS = "Inputs";
	public static final String INPUT_PREDICATE = "Predicate";
	public static final String INPUT_VALUE_MAPPER = "ValueMapper";
	public static final String INPUT_KEY_VALUE_MAPPER = "KeyValueMapper";
	public static final String INPUT_JOIN_TOPIC = "Topic";
	public static final String INPUT_VALUE_JOINER = "ValueJoiner";
	public static final String INPUT_JOIN_WINDOWS = "JoinWindows";
	public static final String INPUT_JOIN_WINDOW_UNITS = "JoinWindowUnits";
	public static final String INPUT_REDUCER = "Reducer";
	public static final String INPUT_AGGREGATOR = "Aggregator";
	public static final String INPUT_INITIALIZER = "Initializer";

	public static final String INPUT_JOINWINDOWS_WINDOW = "window";
	public static final String INPUT_JOINWINDOWS_AFTER = "after";
	public static final String INPUT_JOINWINDOWS_BEFORE = "before";
	public static final String INPUT_JOINWINDOWS_GRACE = "grace";

	public static final String KEY_VALUE_SERDES_TYPE_STRING = RDFTypes.STRING.getName();
	public static final String KEY_VALUE_SERDES_TYPE_INTEGER = RDFTypes.INTEGER.getName();
	public static final String KEY_VALUE_SERDES_TYPE_LONG = RDFTypes.LONG.getName();
	public static final String KEY_VALUE_SERDES_TYPE_DOUBLE = RDFTypes.DOUBLE.getName();
	public static final String KEY_VALUE_SERDES_TYPE_BYTEARRAY = "ByteArray";
}
