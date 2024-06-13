package ddwu.wcs.pfp.dao;

import ddwu.wcs.pfp.domain.AccountType;
import ddwu.wcs.pfp.exception.TypeNotFoundException;
import ddwu.wcs.pfp.sampledata.SampleDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AccountTypeDao {
    @Autowired
    private SampleDataProvider sampleData;

    public int findTypeIdByTypeName(String typeName) throws TypeNotFoundException {      // TypeName으로 TypeID 찾기
        List<AccountType> allAcTypes = sampleData.getAcTypeList();
        for (AccountType acType : allAcTypes) {
            if (acType.getTypeName().equals(typeName)) {
                return acType.getTypeId();
            }
        }
        throw new TypeNotFoundException();
    }
}
