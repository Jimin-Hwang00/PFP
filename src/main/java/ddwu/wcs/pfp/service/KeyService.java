package ddwu.wcs.pfp.service;

import ddwu.wcs.pfp.dao.KeyPairFileNameDao;
import ddwu.wcs.pfp.domain.KeyPairFileName;
import ddwu.wcs.pfp.exception.KeyMangementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.security.*;

@Service
class KeyService {
    @Autowired
    private KeyPairFileNameDao keyPairFileNameDao;

    // 비대칭키 만들어서 반환
    KeyPair createKeyPair(String algorithm, int keySize, File keyPairFile, String accountId) throws KeyMangementException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(keyPairFile))) {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
            keyPairGenerator.initialize(keySize);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            oos.writeObject(keyPair);

            KeyPairFileName accountKeyPair = new KeyPairFileName(accountId, keyPairFile.getName());
            keyPairFileNameDao.insertAccountKeyPair(accountKeyPair);

            return keyPair;
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            throw new KeyMangementException(e.getMessage());
        }
    }

    // 파일에 저장된 KeyPair 역직렬화 하여 반환
    KeyPair getKeyPairFromFile(File file) throws KeyMangementException {
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
             KeyPair keyPair = (KeyPair) ois.readObject();
             return keyPair;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new KeyMangementException(e.getMessage());
        }
    }

    // SecretKey 생성하여 반환
    SecretKey createSecretKey(String algorithm, int keySize) throws KeyMangementException {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
            keyGen.init(keySize);
            SecretKey secretKey = keyGen.generateKey();
            return secretKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new KeyMangementException(e.getMessage());
        }
    }
}
