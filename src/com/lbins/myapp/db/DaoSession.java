package com.lbins.myapp.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import com.lbins.myapp.entity.City;
import com.lbins.myapp.entity.ShoppingCart;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig cityDaoConfig;

    private final CityDao cityDao;

    private final DaoConfig shoppingCartDaoConfig;

    private final ShoppingCartDao shoppingCartDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        cityDaoConfig = daoConfigMap.get(CityDao.class).clone();
        cityDaoConfig.initIdentityScope(type);

        cityDao = new CityDao(cityDaoConfig, this);

        registerDao(City.class, cityDao);

        shoppingCartDaoConfig = daoConfigMap.get(ShoppingCartDao.class).clone();
        shoppingCartDaoConfig.initIdentityScope(type);

        shoppingCartDao = new ShoppingCartDao(shoppingCartDaoConfig, this);

        registerDao(ShoppingCart.class, shoppingCartDao);
    }
    
    public void clear() {
        cityDaoConfig.getIdentityScope().clear();
        shoppingCartDaoConfig.getIdentityScope().clear();
    }

    public CityDao getCityDao() {
        return cityDao;
    }

    public ShoppingCartDao getShoppingCartDao() {
        return shoppingCartDao;
    }
}
