package ddwu.wcs.pfp.dao;

import ddwu.wcs.pfp.domain.KeyPairFileName;
import ddwu.wcs.pfp.sampledata.SampleDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class KeyPairFileNameDao {
    @Autowired
    private SampleDataProvider sampleData;

    public void insertAccountKeyPair(KeyPairFileName keyPairFileName) { // KeyPair 추가
        sampleData.getKpList().add(keyPairFileName);
    }

    public KeyPairFileName findKeyPairByAccountId(String accountId) {   // 계정 ID로 KeyPair 파일 찾기
        List<KeyPairFileName> kpList = sampleData.getKpList();
        for (KeyPairFileName acKeyPair : kpList) {
            if (acKeyPair.getAccountId().equals(accountId)) {
                return acKeyPair;
            }
        }
        return null;
    }
}

