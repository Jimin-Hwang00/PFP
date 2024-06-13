package ddwu.wcs.pfp.service;

import ddwu.wcs.pfp.dao.KeyPairFileNameDao;
import ddwu.wcs.pfp.dao.DirectoryPathDao;
import ddwu.wcs.pfp.dao.EncryptedFileNameDao;
import ddwu.wcs.pfp.domain.*;
import ddwu.wcs.pfp.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.io.*;
import java.security.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class EncryptedFileService {
    private static final String esAlgo = "SHA1withRSA";    // 전자서명 알고리즘
    private static final String symmetricAlgo = "AES";    // 대칭 암호화 알고리즘
    private static final String asymmetricAlgo = "RSA";     // 비대칭 암호화 알고리즘
    private static final int keyPairSize = 2048;  // 비대칭키 사이즈

    @Autowired
    private EncryptedFileNameDao encryptedFileNameDao;
    @Autowired
    private DirectoryPathDao directoryPathDao;
    @Autowired
    private KeyPairFileNameDao keyPairFileNameDao;
    @Autowired
    private KeyService keyService;
    @Autowired
    private CryptoService cryptoService;
    @Autowired
    private DigitalSignatureService digitalSignatureService;

    private String workingDirPath = System.getProperty("user.dir");
    private String fileSeparator = File.separator;

    private static String makeFileName(String senderId) {   // 파일 이름 생성
        StringBuilder fileNameSb = new StringBuilder();
        fileNameSb.append(senderId);
        fileNameSb.append("_");
        fileNameSb.append(new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()));

        return fileNameSb.toString();
    }

    public EncryptedFileName insertEncryptedFile(String senderId, String receiverId, Prescription prescription)
            throws EnvelopException {   // 전자봉투 생성 메소드
        int pListSize = encryptedFileNameDao.findSizeEncryptedFileList();
        prescription.setPrescriptionId(pListSize);

        String keyPairPath = directoryPathDao.findDirPathByDirectoryName("keypair");    // KeyPair 파일이 있는 디렉토리명 찾기

        KeyPair senderKeyPair = null;
        KeyPairFileName senderKeyPairFileName = keyPairFileNameDao.findKeyPairByAccountId(senderId);   // 송신자의 KeyPair 파일 이름 찾기
        if (senderKeyPairFileName != null) {    // 송신자의 KeyPair가 존재하는 경우
            String senderKeyPairPath = keyPairPath + fileSeparator + senderKeyPairFileName.getKeyPairFileName();
            File senderKeyPairFile = new File(workingDirPath + senderKeyPairPath);

            try {
                senderKeyPair = keyService.getKeyPairFromFile(senderKeyPairFile);
            } catch (KeyMangementException e) {
                e.printStackTrace();
                throw new EnvelopException(e.getMessage());
            }
        } else {    // 송신자의 KeyPair가 존재하지 않는 경우 (생성 필요)
            String newKeyPairFileName = makeFileName(senderId);

            File newKeyPairFile = new File((workingDirPath + keyPairPath), newKeyPairFileName);

            try {
                senderKeyPair = keyService.createKeyPair(asymmetricAlgo, keyPairSize, newKeyPairFile, senderId);
            } catch (KeyMangementException e) {
                e.printStackTrace();
                throw new EnvelopException(e.getMessage());
            }
        }

        KeyPair receiverKeyPair = null;
        KeyPairFileName receiverKeyPairFileName = keyPairFileNameDao.findKeyPairByAccountId(receiverId);    // 수신자의 KeyPair 파일 이름 찾기
        if (receiverKeyPairFileName != null) {  // 수신자의 KeyPair가 존재하는 경우
            String receiverKeyPairPath = keyPairPath + fileSeparator + receiverKeyPairFileName.getKeyPairFileName();
            File receiverKeyPairFile = new File(workingDirPath + receiverKeyPairPath);

            try {
                receiverKeyPair = keyService.getKeyPairFromFile(receiverKeyPairFile);
            } catch (KeyMangementException e) {
                e.printStackTrace();
                throw new EnvelopException(e.getMessage());
            }
        } else {    // 수신자의 KeyPair가 존재하지 않는 경우 (생성 필요)
            String newKeyPairFileName = makeFileName(receiverId);
            File newKeyPairFile = new File((workingDirPath + keyPairPath), newKeyPairFileName);

            try {
                receiverKeyPair = keyService.createKeyPair(asymmetricAlgo, keyPairSize, newKeyPairFile, receiverId);
            } catch (KeyMangementException e) {
                e.printStackTrace();
                throw new EnvelopException(e.getMessage());
            }
        }

        String cipherDirPath = directoryPathDao.findDirPathByDirectoryName("cipher");   // "cipher" directory 경로 찾기
        String envelopDirPath = directoryPathDao.findDirPathByDirectoryName("envelop"); // "envelop" directory 경로 찾기

        String fileName = makeFileName(senderId);

        File cipherFile = new File((workingDirPath + cipherDirPath), fileName);
        File envelopFile = new File((workingDirPath + envelopDirPath), fileName);

        SecretKey secretKey = null;
        try {
            int secretKeySize = 128;   // 대칭키 사이즈
            secretKey = keyService.createSecretKey(symmetricAlgo, secretKeySize);   // 대칭키 생성
        } catch (KeyMangementException e) {
            e.printStackTrace();
            throw new EnvelopException(e.getMessage());
        }


        byte[] pByteArr = Prescription.convertToByteArray(prescription);    // Prescription 객체 byte 배열로 직렬화
        byte[] signature;
        try {
            signature = digitalSignatureService.sign(esAlgo, senderKeyPair.getPrivate(), pByteArr);     // 전자서명 생성
        } catch (DigitalSignatureException e) {
            e.printStackTrace();
            throw new EnvelopException(e.getMessage());
        }

        MessagePacket messagePacket = new MessagePacket(pByteArr, signature, senderKeyPair.getPublic());    // 평문(Prescription), 전자서명, 송신자의 공개키 담은 객체 생성
        byte[] mpByteArr = MessagePacket.convertToByteArray(messagePacket); // MessagePacket 객체 byte 배열로 직렬화
        try {
            cryptoService.encryptWithSecretKey(symmetricAlgo, secretKey, mpByteArr, cipherFile);    // 대칭 암호화 및 파일 저장
        } catch (CryptoException e) {
            e.printStackTrace();
            throw new EnvelopException(e.getMessage());
        }

        try {
            cryptoService.encryptSecretKeyWithPublicKey(asymmetricAlgo, receiverKeyPair.getPublic(), secretKey, envelopFile);   // 전자봉투 생성 및 파일 저장
        } catch (CryptoException e) {
            e.printStackTrace();
            throw new EnvelopException(e.getMessage());
        }

        EncryptedFileName ef = new EncryptedFileName(senderId, receiverId, fileName);
        encryptedFileNameDao.insertEncryptedFile(ef);
        System.out.println("생성된 EncryptedFile " + ef);

        Arrays.fill(prescription.getRegNumber().getSecondRegNo(), '\0');  // 주민등록번호 뒷자리 버퍼에서 지우기

        return ef;
    }

    public List<Prescription> openAndVerifyEncryptedFile(String receiverId)
            throws EnvelopException {   // 전자봉투 복호화 메소드
        String keyPairPath = directoryPathDao.findDirPathByDirectoryName("keypair");
        KeyPairFileName receiverAcKeyPair = keyPairFileNameDao.findKeyPairByAccountId(receiverId);  // 수신자의 KeyPair 찾기

        List<Prescription> pList = new ArrayList<>();
        KeyPair receiverKeyPair;
        if (receiverAcKeyPair != null) {    // 수신자의 KeyPair가 존재하는 경우
            String receiverKeyPairPath = keyPairPath + fileSeparator + receiverAcKeyPair.getKeyPairFileName();

            File receiverKeyPairFile = new File(workingDirPath + receiverKeyPairPath);
            try {
                receiverKeyPair = keyService.getKeyPairFromFile(receiverKeyPairFile);
            } catch (KeyMangementException e) {
                e.printStackTrace();
                throw new EnvelopException(e.getMessage());
            }
        } else {    // 수신자의 KeyPair가 존재하지 않는 경우
            String keyPairFileName = makeFileName(receiverId);
            File keyPairFile = new File((workingDirPath + keyPairPath), keyPairFileName);

            try {
                receiverKeyPair = keyService.createKeyPair(asymmetricAlgo, keyPairSize, keyPairFile, receiverId);
            } catch (KeyMangementException e) {
                e.printStackTrace();
                throw new EnvelopException(e.getMessage());
            }
        }

        List<EncryptedFileName> efList = encryptedFileNameDao.findEncryptedFileByReceiverId(receiverId);    // 수신자가 받은 처방전 찾기
        for (EncryptedFileName encryptedFileName : efList) {
            String cipherDirPath = directoryPathDao.findDirPathByDirectoryName("cipher");   // "cipher" directory 경로 찾기
            String envelopDirPath = directoryPathDao.findDirPathByDirectoryName("envelop"); // "envelop" directory 경로 찾기

            String fileName = encryptedFileName.getFileName();

            File cipherFile = new File((workingDirPath + cipherDirPath), fileName);
            File envelopFile = new File((workingDirPath + envelopDirPath), fileName);

            SecretKey secretKey;
            byte[] messagePacketBytes;
            try {
                secretKey = cryptoService.decryptSecretKeyWithPrivateKey(asymmetricAlgo, receiverKeyPair.getPrivate(), envelopFile);  // 전자봉투 복호화
                messagePacketBytes = cryptoService.decryptWithSecretKey(symmetricAlgo, secretKey, cipherFile);
            } catch (CryptoException e) {
                e.printStackTrace();
                throw new EnvelopException(e.getMessage());
            }
            MessagePacket messagePacket = MessagePacket.convertToMessagePacketObj(messagePacketBytes);  // MessagePacket 객체

            try {
                boolean verifyResult = digitalSignatureService.verify(esAlgo, messagePacket.getPublicKey(), messagePacket.getPlainTxt(), messagePacket.getEncryptedHashValue());    // 전자서명 검증
                if (verifyResult) {
                    Prescription prescription = Prescription.convertToPrescriptionObj(messagePacket.getPlainTxt());
                    System.out.println(prescription.getPrescriptionId() + " : 검증 성공");
                    pList.add(prescription);
                } else {
                    System.out.println("검증 실패");
                }
            } catch (DigitalSignatureException e) {
                e.printStackTrace();
                throw new EnvelopException(e.getMessage());
            }
        }
        return pList;
    }
}
