package ddwu.wcs.pfp.dao;

import ddwu.wcs.pfp.domain.DirectoryPath;
import ddwu.wcs.pfp.sampledata.SampleDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DirectoryPathDao {
    @Autowired
    private SampleDataProvider sampleData;

    public String findDirPathByDirectoryName(String name) {     // Directory 이름으로 경로 찾기
        List<DirectoryPath> dpList = sampleData.getDpList();
        for (DirectoryPath directoryPath : dpList) {
            if (directoryPath.getDirPathName().equals(name)) {
                return directoryPath.getDirPath();
            }
        }

        return null;
    }
}
