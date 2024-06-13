package ddwu.wcs.pfp.domain;

import lombok.*;

import java.io.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Prescription implements Serializable {
    private int prescriptionId; // 처방전 id
    private String medicalInsurance;    // 의료보험
    private String patientName; // 환자이름
    private PhoneNumber phoneNumber;   // 환자번호
    private RegisterNumber regNumber;   // 주민등록번호
    private LocalDate date;  // 작성일
    private Medicine medicine;    // 약품 리스트
    private String patientAddress;  // 환자 주소
    private String patientPhone;    // 환자 전화 번호

    public static byte[] convertToByteArray(Prescription prescription) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos);) {
            oos.writeObject(prescription);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Prescription convertToPrescriptionObj(byte[] byteArr) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(byteArr);
            ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (Prescription) ois.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
