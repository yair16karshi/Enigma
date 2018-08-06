package Servlets;

import DataTypes.Util.Ally;
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

@WebServlet(name = "SetMissionSizeServlet", urlPatterns ="/SetMissionSize" )
public class SetMissionSizeServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        MissionSizeRespons res = new MissionSizeRespons();
        int num = 0;
        ServletUtils utils = new ServletUtils(getServletContext());

        String userName = null;
        Cookie cookies [] = request.getCookies();
        for (Cookie c : cookies) {
            if(c.getName().equals("userName"))
                userName = c.getValue();
        }

        String missionSize =  request.getParameter("missionSize");
        Ally ally = utils.GetAllyByUserName(userName);

        if(missionSize == null)
            res.setMsg("You Must Enter A Number.");
        else{
            try{
                num=Integer.parseInt(missionSize);
            }catch (NumberFormatException e){
                res.setMsg("The Input Must Be An Integer.");
            }

            if(num <= 0)
                res.setMsg("Mission Size Must Be Positive.");
            else{
                synchronized (ally){
                    ally.setMissionSize(num);
                }
                res.setSuccessParse(true);
            }


            try(PrintWriter out = response.getWriter()){
                Gson gson = new Gson();
                String json = gson.toJson(res);
                System.out.println(json);
                out.print(json);
            }

        }


    }

    private class MissionSizeRespons{
        private boolean successParse = false;
        private String msg;

        public void setSuccessParse(boolean successParse) {
            this.successParse = successParse;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
