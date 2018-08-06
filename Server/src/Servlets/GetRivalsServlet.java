package Servlets;

import DataTypes.Util.Ally;
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

@WebServlet(urlPatterns = "/GetRivals")
public class GetRivalsServlet extends HttpServlet {
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

        List<Ally> alliesList = utils.GetAlliesCopyInCompetitionByAllyUserName(userName);
        if (alliesList != null) {
            String finalUserName = userName;
            alliesList.removeIf(ally -> ally.getName().equals(finalUserName));

            List<rivalAlly> res = new ArrayList();
            for (Ally ally : alliesList) {
                res.add(new rivalAlly(ally.getName(), ally.getNumOfAgents()));
            }
            resp.getWriter().print(new Gson().toJson(res));

        }
    }


    private class rivalAlly {
        String userName;
        int numOfAgents;
        public rivalAlly(String userName, int numOfAgents) {
            this.userName = userName;
            this.numOfAgents = numOfAgents;
        }
    }
}
