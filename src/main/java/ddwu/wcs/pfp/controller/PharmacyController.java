package ddwu.wcs.pfp.controller;

import ddwu.wcs.pfp.domain.Prescription;
import ddwu.wcs.pfp.exception.EnvelopException;
import ddwu.wcs.pfp.service.EncryptedFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class PharmacyController {
    @Autowired
    private EncryptedFileService efService;

    @GetMapping("/pharmacy/{pharmacyId}")
    public ModelAndView checkPrescription(@PathVariable String pharmacyId, RedirectAttributes ra) {
        // 로그인 과정 생략 ("youngmin" 계정으로 접속했다 가정)
        try {
            List<Prescription> prescriptions = efService.openAndVerifyEncryptedFile(pharmacyId);

            ModelAndView mav = new ModelAndView("pharmacy");
            mav.addObject("prescriptions", prescriptions);

            if (prescriptions.size() == 0) {
                mav.addObject("msg", "업로드된 처방전이 없습니다.");
            }

            return mav;
        } catch (EnvelopException e) {
            ra.addFlashAttribute("msg", "처방전을 불러오는 동안 오류가 발생했습니다.");
            ra.addFlashAttribute("url", "/");

            ModelAndView mav = new ModelAndView();
            mav.setViewName("redirect:/pharmacy/alert");

            return mav;
        }
    }

    @GetMapping("/pharmacy/alert")
    public String alert(@ModelAttribute("msg") String msg, @ModelAttribute("url") String url) {
        return "alert";
    }
}