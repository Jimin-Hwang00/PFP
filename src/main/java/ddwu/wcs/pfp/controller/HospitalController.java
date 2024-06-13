package ddwu.wcs.pfp.controller;

import ddwu.wcs.pfp.controller.command.UploadPrescription;
import ddwu.wcs.pfp.domain.Account;
import ddwu.wcs.pfp.exception.EnvelopException;
import ddwu.wcs.pfp.exception.TypeNotFoundException;
import ddwu.wcs.pfp.service.AccountService;
import ddwu.wcs.pfp.service.EncryptedFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class HospitalController {
    @Autowired
    private AccountService acService;
    @Autowired
    private EncryptedFileService efService;

    @GetMapping("/hospital/{hospitalId}")
    public ModelAndView form(@ModelAttribute("c") UploadPrescription c) {
        ModelAndView mav = new ModelAndView("hospital");
        try {
            List<Account> acList = acService.getAccountByUserTypeName("pharmacy");
            mav.addObject("pAccounts", acList);
        } catch (TypeNotFoundException e) {
            mav.addObject("msg", "계정이 없습니다.");
        } finally {
            return mav;
        }
    }

    @PostMapping("/hospital/{hospitalId}")
    public String uploadPrescription(@PathVariable String hospitalId, @ModelAttribute("c") UploadPrescription c, RedirectAttributes ra) {
        // 로그인 과정 생략 (ewhamokong 계정으로 접속했다 가정)
        try {
            efService.insertEncryptedFile(hospitalId, c.getPharmacyId(), c.getPrescription());
        } catch (EnvelopException e) {
            ra.addFlashAttribute("msg", "처방전을 업로드하는 동안 오류가 발생했습니다.");
            ra.addFlashAttribute("url", "/");
            return "redirect:/hospital/alert";
        }
        return "redirect:/";
    }

    @GetMapping("/hospital/alert")
    public String alert(@ModelAttribute("msg") String msg, @ModelAttribute("url") String url) {
        return "alert";
    }
}
