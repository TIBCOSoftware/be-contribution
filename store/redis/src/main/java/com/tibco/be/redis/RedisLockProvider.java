package com.tibco.be.redis;

import java.util.HashMap;
import java.util.Map;

import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.store.custom.StoreColumnData;
import com.tibco.cep.store.custom.StoreHelper;
import com.tibco.cep.store.custom.StoreRowHolder;
import com.tibco.cep.store.locking.AbstractLockProvider;
import com.tibco.cep.store.locking.LockEntry;

public class RedisLockProvider extends AbstractLockProvider {

	public static final RedisLockProvider INSTANCE = new RedisLockProvider();
	public RedisStoreProvider storeProvider;
	private RedisStoreDataTypeMapper mapper;

	public RedisLockProvider() {
		this.storeProvider = (RedisStoreProvider) RedisStoreProvider.getStoreProviderInstance();
		this.mapper = (RedisStoreDataTypeMapper) storeProvider.getStoreDataTypeMapper();
	}
	
	@Override
	public boolean unlock(Object key) {
		logger.log(Level.DEBUG, "Redis store lock :: Attempting to unlock");
		boolean isUnlocked = false;
		
		Map<String, StoreColumnData> columnDataMap = new HashMap<String, StoreColumnData>();
		// key
		StoreColumnData column = StoreHelper.getColumn(mapper.getStringType(), key.toString());
		columnDataMap.put(RedisConstants.LOCKS_KEY_FIELD, column);
		//memberid
		column = StoreHelper.getColumn(mapper.getStringType(), memberId);
		columnDataMap.put(RedisConstants.LOCKS_MEMBERID_FIELD, column);

		StoreRowHolder rowHolder = new StoreRowHolder(RedisConstants.LOCKS_HASH_NAME, columnDataMap);
		
		try {
			storeProvider.delete(rowHolder);
			isUnlocked = true;
		} catch (Exception e) {
			logger.log(Level.ERROR, "Redis store lock :: Failed while trying to unlock", e);
			isUnlocked = false;
		}
		return isUnlocked;
	}

	/**
	 * This method will be used to release all the locks held by a member when it gets killed
	 * */
	@Override
	public void unlockOnMemberId(String memberId) {
		logger.log(Level.DEBUG, "Redis store lock :: Engine shutting down. Releasing all the locks held by " + memberId);
		Map<String, StoreColumnData> columnDataMap = new HashMap<String, StoreColumnData>();
		StoreColumnData column = StoreHelper.getColumn(mapper.getStringType(), memberId);
		columnDataMap.put(RedisConstants.LOCKS_MEMBERID_FIELD, column);
		StoreRowHolder rowHolder = new StoreRowHolder(RedisConstants.LOCKS_HASH_NAME, columnDataMap);
		
		try {
			storeProvider.delete(rowHolder);
		} catch (Exception e) {
			logger.log(Level.ERROR, "Redis store lock :: Failed while trying to unlockOnMemberId", e);
		}
	}

	@Override
	protected LockEntry getLockEntry(Object key) throws Exception {
		// Fetch entry from the locks hash based on the key		
		// Create StoreRowHolder
		Map<String, StoreColumnData> columnDataMap = new HashMap<String, StoreColumnData>();
		// key
		StoreColumnData column = StoreHelper.getColumn(mapper.getStringType(), key.toString(), null, true, false);
		columnDataMap.put(RedisConstants.LOCKS_KEY_FIELD, column);
		StoreRowHolder rowHolder = new StoreRowHolder(RedisConstants.LOCKS_HASH_NAME, columnDataMap);
		StoreRowHolder result = storeProvider.read(rowHolder);
		
		LockEntry entry = null;
		if (result != null) {
			entry = new LockEntry(result.getColDataMap().get(RedisConstants.LOCKS_KEY_FIELD).getColumnValue().toString(),
								result.getColDataMap().get(RedisConstants.LOCKS_MEMBERID_FIELD).getColumnValue().toString(),
								result.getColDataMap().get(RedisConstants.LOCKS_SOCKET_FIELD).getColumnValue().toString());
		}
		return entry;
	}

	@Override
	protected boolean addLockEntry(LockEntry entry) throws Exception {
		return insertOrUpdateLockEntry(entry, null);
	}

	@Override
	protected boolean updateLockEntry(LockEntry entry, String oldMemberId) throws Exception {
		return insertOrUpdateLockEntry(entry, oldMemberId);
	}
	
	private boolean insertOrUpdateLockEntry(LockEntry entry, String oldMemberId) throws Exception {
		boolean isSuccess = false;
		try {
			if (oldMemberId == null || (oldMemberId != null && 
					oldMemberId.equalsIgnoreCase(entry.getMemberId())) ) {
				// Create StoreRowHolder
				Map<String, StoreColumnData> columnDataMap = new HashMap<String, StoreColumnData>();
				// key
				StoreColumnData column = StoreHelper.getColumn(mapper.getStringType(), entry.getKey(), null, true, false);
				columnDataMap.put(RedisConstants.LOCKS_KEY_FIELD, column);
				//memberid
				column = StoreHelper.getColumn(mapper.getStringType(), entry.getMemberId(), null, false, true);
				columnDataMap.put(RedisConstants.LOCKS_MEMBERID_FIELD, column);
				//socket
				column = StoreHelper.getColumn(mapper.getStringType(), entry.getSocket());
				columnDataMap.put(RedisConstants.LOCKS_SOCKET_FIELD, column);
				
				StoreRowHolder rowHolder = new StoreRowHolder(RedisConstants.LOCKS_HASH_NAME, columnDataMap);
				
				if (oldMemberId == null) {
					isSuccess = storeProvider.addOrUpdateHashRowHolder(entry.getKey(), rowHolder, null, true);
				} else {
					isSuccess = storeProvider.addOrUpdateHashRowHolder(entry.getKey(), rowHolder, oldMemberId, false);
				}
			}
		} catch (Exception e) {
			logger.log(Level.ERROR, "Redis store lock :: Failed while trying to insert/update entry in Redis store", e);
			isSuccess = false;
		}
		return isSuccess;
	}

}
