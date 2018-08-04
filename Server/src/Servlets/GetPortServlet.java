package Servlets;

import DataTypes.Util.Ally;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/GetPort")
public class GetPortServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletUtils utils = new ServletUtils(req.getServletContext());
        Ally ally = utils.CreateNewAlly(req.getParameter("userName"));
        resp.getWriter().print(ally.GetPort());

    }
}
