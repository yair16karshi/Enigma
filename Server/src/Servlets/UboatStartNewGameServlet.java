package Servlets;

import DataTypes.Util.Competition;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by yairk on Aug, 2018
 */

@WebServlet(name = "UboatStartNewGameServlet", urlPatterns = "/uboatStartNewGame")
public class UboatStartNewGameServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletUtils utils = new ServletUtils(getServletContext());

        String userName = null;
        Cookie cookies [] = request.getCookies();
        for (Cookie c : cookies) {
            if(c.getName().equals("userName"))
                userName = c.getValue();
        }

        Competition competition = utils.GetCompetitionByUBoatUserName(userName);

        competition.startNewCompetition();
    }
}
