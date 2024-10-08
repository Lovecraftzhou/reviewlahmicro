package com.reviewlah.db.dao;

import com.reviewlah.db.pojo.BrowseHistory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;

public interface BrowseHistoryDao {
    ArrayList<BrowseHistory> selectAllBrowseHistory();
    BrowseHistory selectBrowseHistoryByHistoryID(BigInteger history_id);
    ArrayList<Integer>selectTop3CategoryFromBrowseHistory(BigInteger customer_id,Date time_his);
    ArrayList<BrowseHistory>selectBrowseHistoryByTimeHis(Date time_his);
    ArrayList<BrowseHistory>selectBrowseHistoryByCustomerId(BigInteger customer_id);
    void deleteBrowseHistoryByHistoryID(BigInteger history_id);
    void insertBrowseHistory(BrowseHistory browseHistory);
}
