package Servlets;

import Consumer.Agent;
import DataTypes.Util.Ally;
import Producer.AgentResults;
import com.google.gson.Gson;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/GetAgents")
public class GetAgentsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletUtils utils = new ServletUtils(req.getServletContext());
        resp.setContentType("application/json;charset=UTF-8");

        String userName = null;
        Cookie cookies[] = req.getCookies();
        for (Cookie c : cookies) {
            if (c.getName().equals("userName"))
                userName = c.getValue();
        }

        Ally ally = utils.GetAllyByUserName(userName);
        List<DisplayAgent> agentsToDisplay = new ArrayList();

        List<AgentResults> agentsOfAlly = (ally != null ? ally.getAgentsResults() : null);
        if(agentsOfAlly != null) {
            for (AgentResults agent : agentsOfAlly) {
                agentsToDisplay.add(new DisplayAgent(
                        agent.getId(),
                        agent.getLeftMissions(),
                        agent.getCandidates()
                ));
            }
        }
        resp.getWriter().print(new Gson().toJson(agentsToDisplay));

    }

    private class DisplayAgent {
        long agentId;
        int numOfLeftMissions;
        int numOfOptionalResults;

        public DisplayAgent(long agentId, int numOfLeftMissions,int numOfOptionalResults){
            this.agentId= agentId;
            this.numOfLeftMissions= numOfLeftMissions;
            this.numOfOptionalResults= numOfOptionalResults;
        }
    }
}
