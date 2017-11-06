package com.sugarya.closet.Data;


/**
 * @author lsxiao
 * @date 2015-11-03 22:28
 */
public class DataLayer {

    private static DataLayer dataLayer;

    private HomeService mHomeService;
    private DataPool mDataPool;

    private DataLayer() {
        mHomeService = new HomeService();
        mDataPool = new DataPool();
    }

    public static DataLayer getInstance() {
        if (dataLayer == null) {
            dataLayer = new DataLayer();
        }
        return dataLayer;
    }


    public HomeService getHomeService() {
        return mHomeService;
    }

    public DataPool getDataPool() {
        return mDataPool;
    }

    public void setDataPool(DataPool dataPool) {
        mDataPool = dataPool;
    }
}

