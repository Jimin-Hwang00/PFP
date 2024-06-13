package ddwu.wcs.pfp.service;

import ddwu.wcs.pfp.exception.CryptoException;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import java.io.*;
import java.security.*;

@Service
class CryptoService {
    // 공개키로 암호화
    boolean encryptSecretKeyWithPublicKey(String algorithm, PublicKey publicKey, SecretKey secretKey, File file)
            throws CryptoException {    // PublicKey로 SecretKey 암호화
        Cipher cipher = null;

        try {
            cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            throw new CryptoException(e.getMessage());
        }

        try (FileOutputStream fos = new FileOutputStream(file);
             CipherOutputStream cos = new CipherOutputStream(fos, cipher);
             ObjectOutputStream oos = new ObjectOutputStream(cos)) {
            oos.writeObject(secretKey);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new CryptoException(e.getMessage());
        }

        return true;
    }

    // 비공개키로 복호화
    SecretKey decryptSecretKeyWithPrivateKey(String algorithm, PrivateKey privateKey, File file)
            throws CryptoException {    // PrivateKey로 SecretKey 복호화
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            e.printStackTrace();
            throw new CryptoException(e.getMessage());
        }

        try (FileInputStream fis = new FileInputStream(file);
             CipherInputStream cis = new CipherInputStream(fis, cipher);
             ObjectInputStream ois = new ObjectInputStream(cis)) {
            SecretKey secretKey = (SecretKey) ois.readObject();
            return secretKey;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new CryptoException(e.getMessage());
        }
    }

    // 대칭키로 암호화
    boolean encryptWithSecretKey(String algorithm, Key key, byte[] data, File file)
            throws CryptoException {    // SecretKey로 데이터 암호화
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            throw new CryptoException(e.getMessage());
        }

        try (FileOutputStream fos = new FileOutputStream(file);
             CipherOutputStream cos = new CipherOutputStream(fos, cipher);){
            cos.write(data);
            cos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new CryptoException(e.getMessage());
        }

        return true;
    }

    // 대칭키로 복호화
    byte[] decryptWithSecretKey(String algorithm, Key key, File file)
        throws CryptoException {    // SecretKey로 데이터 복호화
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, key);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            throw new CryptoException(e.getMessage());
        }

        try (FileInputStream fis = new FileInputStream(file);
             CipherInputStream cis = new CipherInputStream(fis, cipher);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[8192];
            int len;
            while ((len = cis.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();

            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw new CryptoException(e.getMessage());
        }
    }
}
