package ddwu.wcs.pfp.sampledata;

import ddwu.wcs.pfp.domain.*;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class SampleDataProvider {
    private List<AccountType> acTypeList;
    private List<Account> acList;
    private List<EncryptedFileName> efList;
    private List<DirectoryPath> dpList;
    private List<KeyPairFileName> kpList;

    private static String changePathSeparator(String path) {
        return path.replace("/", File.separator);
    }

    private SampleDataProvider() {
        acTypeList = new ArrayList<>();
        acList = new ArrayList<>();
        efList = new ArrayList<>();
        dpList = new ArrayList<>();
        kpList = new ArrayList<>();

        // UserType Sample Data 추가
        acTypeList.add(new AccountType(0, "hospital"));
        acTypeList.add(new AccountType(1, "pharmacy"));

        // Account Sample Data 추가
        acList.add(new Account("ewhamokdong", "ewhamokdong", 0, "이대목동병원", "서울특별시 양천구 안양천로 1071", 37.536543, 126.886192));
        acList.add(new Account("sevrancesincheon", "sevrancesincheon", 0, "신촌세브란스병원", "서울특별시 서대문구 신촌동 연세로 50-1", 37.562313, 126.940936));
        acList.add(new Account("youngmin", "youngmin", 1, "영민약국", "서울 양천구 목동동로 430 단지상가", 37.536707, 126.884973));
        acList.add(new Account("woori", "woori", 1, "주차편한우리약국", "서울 서대문구 성산로 492", 37.561177, 126.941704));

        // DirectoryPath Sample Data 추가
        dpList.add(new DirectoryPath(0, "cipher", changePathSeparator("/src/main/resources/storage/encrypted/cipher")));
        dpList.add(new DirectoryPath(1, "envelop", changePathSeparator("/src/main/resources/storage/encrypted/envelop")));
        dpList.add(new DirectoryPath(2, "keypair", changePathSeparator("/src/main/resources/storage/keypair")));
    }
}
