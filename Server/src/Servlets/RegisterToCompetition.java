package Servlets;

import DataTypes.Util.Ally;
import DataTypes.Util.Competition;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns="/RegisterToCompetition")
public class RegisterToCompetition extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletUtils utils = new ServletUtils(req.getServletContext());

        String userName = null;
        Cookie cookies [] = req.getCookies();
        for (Cookie c : cookies) {
            if(c.getName().equals("userName"))
                userName = c.getValue();
        }
        Competition competition = utils.GetCompetitionByUBoatUserName(req.getParameter("name"));
        List<Ally> pendingAllies = utils.GetPendingAllies();
        if(pendingAllies != null){
            for(Ally pendingAlly : pendingAllies){
                if(pendingAlly.getName().equals(userName)){
                    synchronized (pendingAllies){
                        pendingAlly.setMachineWrapperToDM(competition.getuBoat().getMachineWrapper());
                        List<Ally> compAllies = competition.getAlies();
                        compAllies.add(pendingAlly);
                        pendingAllies.remove(pendingAlly);
                        break;
                    }
                }
            }
        }


    }
}
