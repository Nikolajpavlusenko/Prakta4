package one.controller.command.user;




import one.controller.command.Command;

import one.model.service.UtilityService;
import javax.servlet.http.HttpServletRequest;

public class PaymentCommand implements Command {

    private UtilityService utilityService;

    public PaymentCommand(UtilityService utilityService) {
        this.utilityService = utilityService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        request.setAttribute("items", utilityService.getAll());
        return "/WEB-INF/user/utilityList.jsp";
    }
}
