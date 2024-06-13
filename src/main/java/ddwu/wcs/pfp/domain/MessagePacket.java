package ddwu.wcs.pfp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.*;
import java.security.PublicKey;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessagePacket implements Serializable {    // 전자봉투 생성을 위한 클래스
    private byte[] plainTxt;    // 평문
    private byte[] encryptedHashValue;   // 암호화된 해시 값
    private PublicKey publicKey;    // 송신자의 공개키

    public static byte[] convertToByteArray(MessagePacket mp) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos);) {
            oos.writeObject(mp);
            byte[] serialized = baos.toByteArray();
            return serialized;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MessagePacket convertToMessagePacketObj(byte[] byteArr) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(byteArr);
             ObjectInputStream ois = new ObjectInputStream(bais);) {
            return (MessagePacket) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
