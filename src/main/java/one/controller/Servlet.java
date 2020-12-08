package one.controller;


import one.controller.command.Command;
import one.controller.command.LogoutCommand;
import one.controller.command.admin.AddNewUtilityCommand;
import one.controller.command.admin.AdminUtilityListCommand;
import one.controller.command.admin.post.AddNewUtilityPostCommand;
import one.controller.command.admin.post.DeleteUtilityCommand;
import one.controller.command.admin.post.UpdateUtilityCommand;
import one.controller.command.admin.post.UpdateUtilitySubmitCommand;
import one.controller.command.guest.LoginPageCommand;
import one.controller.command.guest.MainCommand;
import one.controller.command.guest.RegistrationPageCommand;
import one.controller.command.guest.post.LoginCommand;
import one.controller.command.guest.post.RegistrationCommand;
import one.controller.command.user.PaymentCommand;
import one.controller.command.user.post.AddUtilityToPaymentCommand;
import one.controller.command.user.post.CreateOrderCommand;
import one.model.entity.Utility;
import one.model.service.AsyncEJB;
import one.model.service.PaymentService;
import one.model.service.UserService;
import one.model.service.UtilityService;

import javax.ejb.EJB;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@WebServlet("/")
public class Servlet extends HttpServlet {
    private Map<String, Command> commands = new HashMap<>();

    @EJB
    private UserService userService;
    @EJB
    private UtilityService utilityService;
    @EJB
    private PaymentService paymentService;
    @EJB
    AsyncEJB ejb;

    public void init(ServletConfig servletConfig){
        servletConfig.getServletContext()
                .setAttribute("loggedUsers", new HashSet<String>());

        commands.put("logout",new LogoutCommand());
        commands.put("login",new LoginPageCommand());
        commands.put("log", new LoginCommand(userService));
        commands.put("reg", new RegistrationCommand(userService));
        commands.put("registration",new RegistrationPageCommand());
        commands.put("",new MainCommand(ejb));

        commands.put("admin/add_new_utility",new AddNewUtilityCommand());
        commands.put("admin/adminUtilityList", new AdminUtilityListCommand(utilityService));
        commands.put("admin/deleteUtility", new DeleteUtilityCommand(utilityService));
        commands.put("admin/updateUtility", new UpdateUtilityCommand(utilityService));
        commands.put("admin/updateUtilitySubmit",new UpdateUtilitySubmitCommand(utilityService));
        commands.put("admin/addNewUtilityPost", new AddNewUtilityPostCommand(utilityService));

        commands.put("user/order_utility", new PaymentCommand(utilityService));
        commands.put("user/order_create", new CreateOrderCommand(utilityService));
        commands.put("user/addOrderToPayment", new AddUtilityToPaymentCommand(paymentService));

    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws IOException, ServletException {
        processRequest(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        path = path.replaceAll(".*/web/" , "");
        Command command = commands.getOrDefault(path ,
                (req)->"/error.jsp");
        String page = command.execute(request);
        if (page.contains("redirect"))
            response.sendRedirect(request.getContextPath() + page.replace("redirect:", ""));
        else
            request.getRequestDispatcher(page).forward(request,response);
    }

}
