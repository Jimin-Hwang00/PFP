package ddwu.wcs.pfp.service;

import ddwu.wcs.pfp.dao.AccountDao;
import ddwu.wcs.pfp.dao.AccountTypeDao;
import ddwu.wcs.pfp.domain.Account;
import ddwu.wcs.pfp.exception.TypeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private AccountTypeDao accountTypeDao;

    public List<Account> getAccountByUserTypeName(String typeName) throws TypeNotFoundException {    // Type 이름으로 계정 찾기
        int typeId = accountTypeDao.findTypeIdByTypeName(typeName);
        List<Account> acList = accountDao.findAccountList();

        List<Account> result = new ArrayList<>();
        for (Account account : acList) {
            if (account.getTypeId() == typeId) {
                result.add(account);
            }
        }

        return result;
    }
}
