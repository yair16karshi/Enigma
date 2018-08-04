package Servlets;

import DataTypes.Util.Competition;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "UBoatCandidatesPollServlet" ,urlPatterns = "/uboatCandidatesPoll")
public class AllyGameStartedPollServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletUtils servletUtils = new ServletUtils(getServletContext());

        String competitionName = req.getParameter("competitionName");

        Competition competition = servletUtils.GetCompetitionByCompetitionName(competitionName);
        if(competition != null){
            resp.getWriter().print(competition.isActive());
        }

    }
}
