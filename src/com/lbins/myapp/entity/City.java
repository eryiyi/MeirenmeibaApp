package com.lbins.myapp.entity;


// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END

import com.lbins.myapp.db.CityDao;
import com.lbins.myapp.db.DaoSession;
import de.greenrobot.dao.DaoException;

/**
 * Entity mapped to table CITY.
 */
public class City {

    /** Not-null value. */
    private String cid;
    private String cityid;
    private String areaid;
    private String cityName;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient CityDao myDao;


    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public City() {
    }

    public City(String cid) {
        this.cid = cid;
    }

    public City(String cid, String cityid, String areaid, String cityName) {
        this.cid = cid;
        this.cityid = cityid;
        this.areaid = areaid;
        this.cityName = cityName;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCityDao() : null;
    }

    /** Not-null value. */
    public String getCid() {
        return cid;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getAreaid() {
        return areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
