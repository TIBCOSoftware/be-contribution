/**
 * # Copyright (c) 2019-2020. TIBCO Software Inc.
 * # This file is subject to the license terms contained in the license file that is distributed with this file.
 */
package com.tibco.be.mongoDB;

public interface MongoDBConstants {

	String PROPERTY_KEY_SSL_TRUSTED_CERTIFICATE_FOLDER_PATH = "ssl.trusted.certificate.folder.path";
	String PROPERTY_KEY_SSL_IDENTITY_FILE_PATH = "ssl.identity.file.path";
	String PROPERTY_KEY_SSL_TRUSTED_STORE_PASSWORD = "ssl.trusted.store.password";
	String PROPERTY_KEY_SSL_ENABLED = "isSecurityEnabled";
	String PROPERTY_KEY_MONGODB_AUTH_PASSWORD = "password";
	String PROPERTY_KEY_MONGODB_URI = "URI";
	String PROPERTY_KEY_MONGODB_AUTH_USER = "user";
	String PROPERTY_KEY_MONGODB_DB_NAME = "dbName";
	String PROPERTY_KEY_MONGODB_ID = "_id";
	String PROPERTY_KEY_MONGODB_TIME_CREATED = "time_created_";
	String PROPERTY_KEY_MONGODB_TIME_MODIFIED = "time_last_modified_";
	String LOCKS_COLLECTION_NAME = "locks";
	String DOCUMENT_LOCKS_KEY_FIELD = "key";
	String DOCUMENT_LOCKS_MEMBERID_FIELD = "memberid";
	String DOCUMENT_LOCKS_SOCKET_FIELD = "socket";

}
