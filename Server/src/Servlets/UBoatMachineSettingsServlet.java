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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yairk on Aug, 2018
 */

@WebServlet(name = "UboatConfigurationServlet", urlPatterns = {"/UboatMachineSettingsServlet"})
public class UBoatMachineSettingsServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        ServletUtils utils = new ServletUtils(getServletContext());
        String userName = null;
        Cookie cookies [] = request.getCookies();
        for (Cookie c : cookies) {
            if(c.getName().equals("userName"))
                userName = c.getValue();
        }

        Competition competition = utils.GetCompetitionByUBoatUserName(userName);

        int rotorsCount = competition.getRotorsCount();
        ArrayList<Integer> reflectorsId = competition.getReflectorsIDs();
        int numOfAvailableRotos = competition.getNumOfRotors();
        ArrayList<Character> ABC = competition.getABC();
        MachineDetailsResponse machineDetails = new MachineDetailsResponse(rotorsCount , numOfAvailableRotos,reflectorsId,ABC);

        try(PrintWriter out = response.getWriter()){
            Gson gson = new Gson();
            String json = gson.toJson(machineDetails);
            System.out.println(json);
            out.print(json);
            out.flush();
        }
    }

    private class MachineDetailsResponse{
        private int rotorsCount;
        private int numOfRotors;
        private ArrayList<Integer> ReflectorsID;
        private ArrayList<Character> ABC;


        public MachineDetailsResponse(int rotorsCount, int numOfRotors, ArrayList<Integer> ReflectorsID, ArrayList<Character> ABC) {
            this.rotorsCount = rotorsCount;
            this.numOfRotors = numOfRotors;
            this.ReflectorsID = ReflectorsID;
            this.ABC = ABC;
        }
    }
}
