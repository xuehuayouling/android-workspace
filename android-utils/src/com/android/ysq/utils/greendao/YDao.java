package com.android.ysq.utils.greendao;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.internal.DaoConfig;

public abstract class YDao<T, K> extends AbstractDao<T, K> {

	protected DataSetObservable mDataSetObservable = new DataSetObservable();
	public YDao(DaoConfig config) {
		super(config);
	}

	public YDao(DaoConfig config, AbstractDaoSession daoSession) {
		super(config, daoSession);
	}
	
	public void registerObserver(DataSetObserver observer) {
		mDataSetObservable.registerObserver(observer);
	}
	
	@Override
	public long insert(T entity) {
		long id = super.insert(entity);
		if (id != -1) {
			mDataSetObservable.notifyChanged();
		}
		return id;
	}

	@Override
	public void insertInTx(Iterable<T> entities) {
		super.insertInTx(entities);
		mDataSetObservable.notifyChanged();
	}

	@Override
	public void insertInTx(T... entities) {
		super.insertInTx(entities);
		mDataSetObservable.notifyChanged();
	}

	@Override
	public void insertInTx(Iterable<T> entities, boolean setPrimaryKey) {
		super.insertInTx(entities, setPrimaryKey);
		mDataSetObservable.notifyChanged();
	}

	@Override
	public void insertOrReplaceInTx(Iterable<T> entities, boolean setPrimaryKey) {
		super.insertOrReplaceInTx(entities, setPrimaryKey);
		mDataSetObservable.notifyChanged();
	}

	@Override
	public void insertOrReplaceInTx(Iterable<T> entities) {
		super.insertOrReplaceInTx(entities);
		mDataSetObservable.notifyChanged();
	}

	@Override
	public void insertOrReplaceInTx(T... entities) {
		super.insertOrReplaceInTx(entities);
		mDataSetObservable.notifyChanged();
	}

	@Override
	public long insertWithoutSettingPk(T entity) {
		long id = super.insertWithoutSettingPk(entity);
		if (-1 != id) {
			mDataSetObservable.notifyChanged();
		}
		return id;
	}

	@Override
	public long insertOrReplace(T entity) {
		long id = super.insertOrReplace(entity);
		if (-1 != id) {
			mDataSetObservable.notifyChanged();
		}
		return id;
	}

	@Override
	public void deleteAll() {
		super.deleteAll();
		mDataSetObservable.notifyInvalidated();
	}

	@Override
	public void delete(T entity) {
		super.delete(entity);
		mDataSetObservable.notifyInvalidated();
	}

	@Override
	public void deleteByKey(K key) {
		super.deleteByKey(key);
		mDataSetObservable.notifyInvalidated();
	}

	@Override
	public void deleteInTx(Iterable<T> entities) {
		super.deleteInTx(entities);
		mDataSetObservable.notifyInvalidated();
	}

	@Override
	public void deleteInTx(T... entities) {
		super.deleteInTx(entities);
		mDataSetObservable.notifyInvalidated();
	}

	@Override
	public void deleteByKeyInTx(Iterable<K> keys) {
		super.deleteByKeyInTx(keys);
		mDataSetObservable.notifyInvalidated();
	}

	@Override
	public void deleteByKeyInTx(K... keys) {
		super.deleteByKeyInTx(keys);
		mDataSetObservable.notifyInvalidated();
	}

	@Override
	public void refresh(T entity) {
		super.refresh(entity);
		mDataSetObservable.notifyChanged();
	}

	@Override
	public void update(T entity) {
		super.update(entity);
		mDataSetObservable.notifyChanged();
	}

	@Override
	public void updateInTx(Iterable<T> entities) {
		super.updateInTx(entities);
		mDataSetObservable.notifyChanged();
	}

	@Override
	public void updateInTx(T... entities) {
		super.updateInTx(entities);
		mDataSetObservable.notifyChanged();
	}
	
	
	
}
