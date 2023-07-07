package com.tibco.cep.driver.kafka;

import com.tibco.cep.runtime.channel.ChannelProperties;

public class KafkaProperties implements ChannelProperties {
	public static final String KEY_CHANNEL_BOOTSTRAP_SERVER = "kafka.broker.urls";
	public static final String KEY_CHANNEL_SECURITY_PROTOCOL = "kafka.security.protocol";
	public static final String KEY_CHANNEL_SASL_MECHANISM = "kafka.sasl.mechanism";
	public static final String KEY_CHANNEL_TRUSTED_CERTS_FOLDER = "kafka.trusted.certs.folder";
	public static final String KEY_CHANNEL_KEYSTORE_IDENTITY = "kafka.keystore.identity";
	public static final String KEY_CHANNEL_TRUSTSTORE_PASSWORD = "kafka.truststore.password";

	public static final String KEY_DESTINATION_GROUP_ID = "group.id";
	public static final String KEY_DESTINATION_CLIENT_ID = "client.id";
	public static final String KEY_DESTINATION_TOPIC_NAME = "topic.name";
	public static final String KEY_DESTINATION_CONSUMER_THREADS = "consumer.threads";
	public static final String KEY_DESTINATION_POLL_INTERVAL = "poll.interval";
	public static final String KEY_DESTINATION_ENABLE_AUTOCOMMIT = "enable.autocommit";
	public static final String KEY_DESTINATION_AUTOCOMMIT_INTERVAL = "autocommit.interval";
	public static final String KEY_DESTINATION_HEARTBEAT_INTERVAL = "heartbeat.interval.msec";
	public static final String KEY_DESTINATION_SESSION_TIMEOUT = "session.timeout.msec";
	public static final String KEY_DESTINATION_COMPRESSION_TYPE = "compression.type";
	public static final String KEY_DESTINATION_AUTO_OFFSET_RESET = "auto.offset.reset";
	public static final String KEY_DESTINATION_REPLY_TOPIC = "destination.reply.topic";
	public static final String INTERNAL_PROP_KEY_CORRELATION_ID = "message.correlation.id";
	public static final String KEY_DESTINATION_MAX_POLL_INTERVAL = "max.poll.interval.ms";

//	public static final String KEY_DESTINATION_BATCH_SIZE = "batch.size";
//	public static final String KEY_DESTINATION_QUEUE_TIME = "queue.time";
	public static final String KEY_DESTINATION_SYNC_SENDER = "sync.sender";
	public static final String KEY_DESTINATION_SYNC_SENDER_MAX_WAIT = "sync.sender.max.wait";

	public static final String KEY_DESTINATION_MESSAGE_KEY_RF = "message.key.rf";

	public static final String KEY_SKIP_BE_ATTRIBUTES = "be.channel.kafka.skip.attributes";

	public static final String RESERVED_EVENT_PROP_MESSAGE_KEY = "_messageKey_";

	public static final String INTERNAL_PROP_KEY_CHANNEL_URI = "channel.uri";
	public static final String INTERNAL_PROP_KEY_DESTINATION_URI = "destination.uri";
	public static final String INTERNAL_PROP_KEY_KEY_SERIALIZER = "key.serializer";
	public static final String INTERNAL_PROP_KEY_VALUE_SERIALIZER = "value.serializer";
	public static final String INTERNAL_PROP_KEY_KEY_DESERIALIZER = "key.deserializer";
	public static final String INTERNAL_PROP_KEY_VALUE_DESERIALIZER = "value.deserializer";

	public static final String PROPERTY_KEY_KAFKA_CLIENT_PROPERTY_PREFIX = "be.channel.kafka";
	public static final String PROPERTY_KEY_KAFKA_ERROR_ENDPOINT_ENABLED = "be.kafka.error.endpoint.enable";
	public static final String PROPERTY_KEY_KAFKA_DEFAULT_ERROR_TOPIC_NAME = "be.kafka.default.error.topic.name";
	public static final String PROPERTY_KEY_KAFKA_PROCESS_EVENT_MAX_ATTEMPTS = "be.kafka.process.event.max.attempts";
	public static final String PROPERTY_KEY_KAFKA_PROCESS_EVENT_ATTEMPTS_INTERVAL = "be.kafka.process.event.attempts.interval";

	public static final String KEY_DESTINATION_SCHEMA_SUBJECT = "schema.subject.name";
	public static final String SCHEMA_REGISTRY_URL = "schema.registry.url";
	public static final String SCHEMA_REGISTRY_PLATFORM = "schema.registry.platform";
	public static final String CONFLUENT_PLATFORM = "confluent.platform";
	public static final String TIBCO_PLATFORM = "tibco.platform";
	public static final String JSON_SCHEMA = "schema";
	public static final String BASIC_AUTH="basic.auth";
	public static final String SSL_ONE_WAY="ssl.oneWay";
	public static final String MUTUAL_AUTH="ssl.mutual";
	public static final String NO_AUTH="default";
	public static final String SCHEMA_REGISTRY_USERNAME="schema.registry.username";
	public static final String SCHEMA_REGISTRY_PASSWORD="schema.registry.password";
	public static final String SCHEMA_REGISTRY_AUTH_TYPE="schema.registry.authentication";
	public static final String UPDATE_SCHEMA_REGISTRY="update.schema.registry";
	
	
	public static final String SCHEMA_REGISTRY_SSL_KEYSTORE_LOCATION="schema.registry.ssl.keystore.location";
	public static final String SCHEMA_REGISTRY_SSL_KEYSTORE_PASSWORD="schema.registry.ssl.keystore.password";
	public static final String SCHEMA_REGISTRY_SSL_KEY_PASSWORD="schema.registry.ssl.key.password";
	public static final String SCHEMA_REGISTRY_SSL_KEYSTORE_TYPE="schema.registry.ssl.keystore.type";
	public static final String SCHEMA_REGISTRY_SSL_TRUSTSTORE_LOCATION = "schema.registry.ssl.truststore.location";
	public static final String SCHEMA_REGISTRY_SSL_TRUSTSTORE_PASSWORD="schema.registry.ssl.truststore.password";
	public static final String SCHEMA_REGISTRY_SSL_TRUSTSTORE_TYPE="schema.registry.ssl.truststore.type";
	
	public static final String SCHEMA_REGISTRY_SECURITY_PROTOCOL="schema.registry.security.protocal";
	public static final String SCHEMA_REGISTRY_SSL_CLIENT_AUTH="schema.registry.ssl.client.auth";
	public static final String SCHEMA_REGISTRY_SSL_ENDPOINT_IDENTIFICATION_ALGORITHM="schema.registry.ssl.endpoint.identification.algorithm";
	
	
	public static final String SCHEMA_REGISTRY_SSL_ENABLED_PROTOCOLS="schema.registry.ssl.enabled.protocols";
	
	public static final String SCHEMA_REGISTRY_BASIC_AUTH_CREDENTIAL_SOURCE="basic.auth.credentials.source";
	public static final String SCHEMA_REGISTRY_BASIC_AUTH_USER_INFO="schema.registry.basic.auth.user.info";
	
	public static final String SCHEMA_REGISTRY_TRUSTSTORE_LOCATION="schema.registry.trusted.certs.folder";
	public static final String SCHEMA_REGISTRY_TRUSTSTORE_PWD="schema.registry.truststore.password";
	public static final String SCHEMA_REGISTRY_KEYSTORE_IDENTITY="schema.registry.keystore.identity";
	
	public static final String FTL_REALMSERVERS="ftl.realmservers";
	public static final String FTL_TRUST_TYPE="ftl.trust.type";
	public static final String FTL_TRUST_FILE="ftl.trust.file";
	public static final String FTL_USERNAME="ftl.username";
	public static final String FTL_PASSWORD="ftl.password";
	
	
	public static final String CONFLUENT_SCHEMA_REGISTRY_SERIALIZER="io.confluent.kafka.serializers.KafkaAvroSerializer";
	public static final String TIBCO_SCHEMA_REGISTRY_SERIALIZER="com.tibco.messaging.kafka.avro.AvroSerializer";
	public static final String CONFLUENT_SCHEMA_REGISTRY_DESERIALIZER="io.confluent.kafka.serializers.KafkaAvroDeserializer";
	public static final String TIBCO_SCHEMA_REGISTRY_DESERIALIZER="com.tibco.messaging.kafka.avro.AvroDeserializer";
	
	
	public static final String PAYLOAD="payload";

	public static final String EVENT_FIELD_MSG_KEY = "kafka_message_key";
	public static final String EVENT_FIELD_MSG_OFFSET = "kafka_message_offset";
	public static final String EVENT_FIELD_MSG_PARTITION = "kafka_message_partition";
	public static final String EVENT_FIELD_MSG_TIMESTAMP = "kafka_message_timestamp";
	public static final String EVENT_FIELD_MSG_TIMESTAMPTYPE = "kafka_message_timestamptype";
}
