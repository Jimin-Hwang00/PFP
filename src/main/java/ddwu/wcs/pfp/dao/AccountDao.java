package ddwu.wcs.pfp.dao;

import ddwu.wcs.pfp.domain.Account;
import ddwu.wcs.pfp.sampledata.SampleDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AccountDao {
    @Autowired
    private SampleDataProvider sampleData;

    public List<Account> findAccountList() {    // 모든 Account 반환
        return sampleData.getAcList();
    }
}
