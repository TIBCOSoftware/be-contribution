/*
* Copyright © 2020. TIBCO Software Inc.
* This file is subject to the license terms contained
* in the license file that is distributed with this file.
*/

package com.tibco.be.redis;

public interface RedisConstants {
	
	String PRIMARY = "Primary";
	String PROPERTY_KEY_SSL_TRUSTED_CERTIFICATE_FOLDER_PATH="ssl.trusted.certificate.folder.path";
	String PROPERTY_KEY_SSL_IDENTITY_FILE_PATH = "ssl.identity.file.path";
	String PROPERTY_KEY_SSL_TRUSTED_STORE_PASSWORD = "ssl.trusted.store.password";
	String PROPERTY_KEY_REDIS_AUTH_PASSWORD = "password";
	String LOCKS_HASH_NAME = "locks";
	String LOCKS_KEY_FIELD = "key";
	String LOCKS_MEMBERID_FIELD = "memberid";
	String LOCKS_SOCKET_FIELD = "socket";

}
