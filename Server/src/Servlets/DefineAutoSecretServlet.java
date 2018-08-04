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

@WebServlet(name = "DefineAutoSecretServlet", urlPatterns = "/DefineAutoSecretServlet")
public class DefineAutoSecretServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletUtils utils = new ServletUtils(getServletContext());
        String userName = null;
        Cookie cookies [] = request.getCookies();
        for (Cookie c : cookies) {
            if(c.getName().equals("userName"))
                userName = c.getValue();
        }
        Competition competition = utils.GetCompetitionByUBoatUserName(userName);
        synchronized (competition){
            competition.getuBoat().getMachineWrapper().setInitialSecretAutomatically();
        }
        competition.setReadyToRegister(true);
    }
}
