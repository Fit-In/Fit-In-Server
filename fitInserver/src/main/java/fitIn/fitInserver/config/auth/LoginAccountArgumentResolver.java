package fitIn.fitInserver.config.auth;

import fitIn.fitInserver.config.auth.dto.SessionAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Component
public class LoginAccountArgumentResolver implements HandlerMethodArgumentResolver {

    private final HttpSession httpSession;


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginAccount.class) != null;
        boolean isAccountClass = SessionAccount.class.equals(parameter.getParameterType());
        return isLoginUserAnnotation && isAccountClass;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return httpSession.getAttribute("account");
    }
}
