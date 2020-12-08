package one.controller.command.admin;


import one.controller.command.Command;
import one.model.entity.Utility;
import one.model.service.UtilityService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class AdminUtilityListCommand  implements Command {

    private UtilityService utilityService;

    public AdminUtilityListCommand(UtilityService utilityService) {
        this.utilityService = utilityService;
    }

    @Override
    public String execute(HttpServletRequest request) {
        request.setAttribute("items", utilityService.getAll());

        return "/WEB-INF/admin/adminUtilityList.jsp";
    }
}
