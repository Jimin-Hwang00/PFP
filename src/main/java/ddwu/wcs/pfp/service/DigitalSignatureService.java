package ddwu.wcs.pfp.service;

import ddwu.wcs.pfp.exception.DigitalSignatureException;
import org.springframework.stereotype.Service;

import java.security.*;

@Service
class DigitalSignatureService {
    byte[] sign(String algorithm, PrivateKey privateKey, byte[] data)
            throws DigitalSignatureException {    // 전자서명 sign
        try {
            Signature sig = Signature.getInstance(algorithm);
            sig.initSign(privateKey);
            sig.update(data);
            byte[] signature = sig.sign();
            return signature;
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
            throw new DigitalSignatureException(e.getMessage());
        }
    }

    boolean verify(String algorithm, PublicKey facilityPublicKey, byte[] data, byte[] signature)
            throws DigitalSignatureException {  // 전자서명 verify
        try {
            Signature sig = Signature.getInstance(algorithm);
            sig.initVerify(facilityPublicKey);
            sig.update(data);
            boolean rslt = sig.verify(signature);
            if (rslt) {
                System.out.println("검증 성공");
            } else {
                System.out.println("검증 실패");
            }
            return rslt;
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            e.printStackTrace();
            throw new DigitalSignatureException(e.getMessage());
        }
    }
}
