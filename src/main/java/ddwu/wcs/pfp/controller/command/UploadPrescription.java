package ddwu.wcs.pfp.controller.command;

import ddwu.wcs.pfp.domain.Prescription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UploadPrescription {
    private Prescription prescription;
    private String pharmacyId;
}
