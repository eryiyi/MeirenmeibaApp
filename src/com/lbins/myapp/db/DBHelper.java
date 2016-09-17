package com.lbins.myapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.lbins.myapp.entity.City;
import com.lbins.myapp.entity.ShoppingCart;
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
    private ShoppingCartDao shoppingCartDao;

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
            instance.shoppingCartDao = daoMaster.newSession().getShoppingCartDao();
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


    /**
     * 插入数据
     * @param test
     */
    public void addShoppingToTable(ShoppingCart test){
        shoppingCartDao.insert(test);
    }

    //查询是否存在该商品
    public boolean isSaved(String ID)
    {
        QueryBuilder<ShoppingCart> qb = shoppingCartDao.queryBuilder();
        qb.where(ShoppingCartDao.Properties.Goods_id.eq(ID));
        qb.buildCount().count();
        return qb.buildCount().count() > 0 ? true : false;
    }
    //批量插入数据
    public void saveTestList(List<ShoppingCart> tests){
        shoppingCartDao.insertOrReplaceInTx(tests);
    }

    /**
     * 查询所有的购物车
     * @return
     */
    public List<ShoppingCart> getShoppingList(){
        return shoppingCartDao.loadAll();
    }

    /**
     * 插入或是更新数据
     * @param test
     * @return
     */
    public long saveShopping(ShoppingCart test){
        return shoppingCartDao.insertOrReplace(test);
    }

    /**
     * 更新数据
     * @param test
     */
    public void updateTest(ShoppingCart test){
        shoppingCartDao.update(test);
    }

    /**
     * 删除所有数据--购物车
     * */

    public void deleteShopping(){
        shoppingCartDao.deleteAll();
    }
    /**
     * 删除数据根据goods_id
     * */

    public void deleteShoppingByGoodsId(String cartid){
        QueryBuilder qb = shoppingCartDao.queryBuilder();
        qb.where(ShoppingCartDao.Properties.Cartid.eq(cartid));
        shoppingCartDao.deleteByKey(cartid);//删除
    }

}
