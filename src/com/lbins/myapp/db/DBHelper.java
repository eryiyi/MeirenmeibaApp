package com.lbins.myapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.lbins.myapp.entity.City;
import de.greenrobot.dao.query.QueryBuilder;

import java.util.List;

/**
 * Created by liuzwei on 2015/3/13.
 */
public class DBHelper {
    private static Context mContext;
    private static DBHelper instance;
    private static DaoMaster.DevOpenHelper helper;
    private static SQLiteDatabase db;
    private static DaoMaster daoMaster;

    private CityDao cityDao;

    private DBHelper() {
    }

    public static DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper();
            if (mContext == null) {
                mContext = context;
            }
            helper = new DaoMaster.DevOpenHelper(context, "guiren_hm_db_t_001", null);
            db = helper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            instance.cityDao = daoMaster.newSession().getCityDao();
        }
        return instance;
    }


    //批量插入城市
    public void saveCityList(List<City> tests) {
        cityDao.insertOrReplaceInTx(tests);
    }

    /**
     * 查询城市列表
     *
     * @return
     */
    public List<City> getCityList() {
        return cityDao.loadAll();
    }

    /**
     * 插入或是更新城市
     *
     * @return
     */
    public long saveCity(City city) {
        return cityDao.insertOrReplace(city);
    }

    //查询会员信息
    public City getCityId(String id) {
        City emp = cityDao.load(id);
        return emp;
    }

}
