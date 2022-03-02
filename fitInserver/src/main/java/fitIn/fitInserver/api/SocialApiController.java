package fitIn.fitInserver.api;

import fitIn.fitInserver.config.auth.LoginAccount;
import fitIn.fitInserver.config.auth.dto.SessionAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class SocialApiController {

    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model, @LoginAccount SessionAccount account) {

        if(account != null) {
            model.addAttribute("accountName", account.getName());
        }

        return "index";
    }
}
