package ddwu.wcs.pfp.controller;

import ddwu.wcs.pfp.domain.Account;
import ddwu.wcs.pfp.exception.TypeNotFoundException;
import ddwu.wcs.pfp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/")
    public ModelAndView home() {
        ModelAndView mav = new ModelAndView("index");
        try {
            List<Account> hospitalAccounts = accountService.getAccountByUserTypeName("hospital");
            List<Account> pharmacyAccounts = accountService.getAccountByUserTypeName("pharmacy");

            mav.addObject("hospitalAccounts", hospitalAccounts);
            mav.addObject("pharmacyAccounts", pharmacyAccounts);
        } catch (TypeNotFoundException e) {
            mav.addObject("msg", "계정이 없습니다.");
        } finally {
            return mav;
        }
    }
}
