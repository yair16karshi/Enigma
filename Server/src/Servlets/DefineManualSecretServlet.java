package Servlets;

import DataTypes.Util.Competition;
import InputValidation.Util;
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

@WebServlet(name = "DefineManualSecretServlet", urlPatterns = "/DefineManualSecretServlet")
public class DefineManualSecretServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        ServletUtils util = new ServletUtils(getServletContext());
        boolean continueCheck = true;
        String msg = null;
        String userName = null;
        Cookie cookies [] = request.getCookies();
        for (Cookie c : cookies) {
            if(c.getName().equals("userName"))
                userName = c.getValue();
        }

        Competition competition = util.GetCompetitionByUBoatUserName(userName);

        int rotorsCount = competition.getRotorsCount();

        StringBuilder rotorsStr = new StringBuilder();
        StringBuilder positionsStr = new StringBuilder();

        for (int i = 0; i < rotorsCount; i++) {
            String rotor = request.getParameter("rotor" + i);
            if(rotor == null || rotor.equals("choose")){
                response.setStatus(400);
                continueCheck = false;

            }

            rotorsStr.append(rotor);
        }

        for (int i = 0; i < rotorsCount; i++) {
            String position = request.getParameter("position" + i);
            if(position == null|| position.equals("choose")){
                response.setStatus(400);
                continueCheck = false;
            }
            positionsStr.append(position);
        }

        String reflector = request.getParameter("reflector");
        if(reflector == null|| reflector.equals("choose")){
            response.setStatus(400);
            continueCheck = false;
        }

        if(continueCheck)
            msg = competition.checkInputAndInitSecret(rotorsStr.toString(),positionsStr.toString(),reflector);

        ManualConfigurationsDetails res;
        if(msg == null) {
            res = new ManualConfigurationsDetails(true, null);
            competition.setReadyToRegister(true);
        } else {
            res = new ManualConfigurationsDetails(false, msg);
        }

        try(PrintWriter out = response.getWriter()){
            Gson gson = new Gson();
            String json = gson.toJson(res);
            System.out.println(json);
            out.print(json);
        }
    }

    private class ManualConfigurationsDetails{
        private boolean isSuccess;
        private String errorMsg;

        public ManualConfigurationsDetails(boolean isSuccess, String msg) {
            this.isSuccess = isSuccess;
            this.errorMsg = msg;
        }
    }
}
