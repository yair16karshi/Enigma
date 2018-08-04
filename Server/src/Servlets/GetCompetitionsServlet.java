package Servlets;

import DataTypes.Util.Competition;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/GetCompetitions")
public class GetCompetitionsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletUtils utils = new ServletUtils(req.getServletContext());
        resp.setContentType("application/json;charset = UTF-8");
        List<Competition> res = new ArrayList<>();
        for(Competition competition : utils.GetCompetitionListFromContext()){
            if(!competition.isActive()){
                res.add(competition);
            }
        }
        resp.getWriter().print(res);
    }
}
