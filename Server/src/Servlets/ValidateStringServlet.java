package Servlets;

import DataTypes.Util.Competition;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by yairk on Aug, 2018
 */

@WebServlet(name = "ValidateStringServlet", urlPatterns = "/ValidateStringServlet")
public class ValidateStringServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        ServletUtils utils = new ServletUtils(getServletContext());
        String msg = request.getParameter("msg");
        System.out.println("Start " + msg);
        String userName = null;
        Cookie cookies [] = request.getCookies();
        for (Cookie c : cookies) {
            if(c.getName().equals("userName"))
                userName = c.getValue();
        }
        Competition competition = utils.GetCompetitionByUBoatUserName(userName);

        boolean res = competition.checkInDictionary(msg.toUpperCase());
        if(res){
            String encString = competition.getuBoat().getMachineWrapper().process(msg);
            synchronized (competition){
                competition.getuBoat().setMsgBeforeEnc(msg);
                competition.getuBoat().setEncryptedMsg(encString);
            }
        }

        try(PrintWriter out = response.getWriter()){
            Gson gson = new Gson();
            String json = gson.toJson(res);
            System.out.println(json);
            out.print(json);
        }
    }
}
