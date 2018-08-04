package Servlets;

import DataTypes.GeneratedMachineDataTypes.Enigma;
import DataTypes.GeneratedMachineDataTypes.Machine;
import DataTypes.Util.Competition;
import InputValidation.XMLParser;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

/**
 * Created by yairk on Aug, 2018
 */

@WebServlet(name = "FileUploadServlet", urlPatterns = "/uploadFile")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileUploadServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset = UTF-8");
        String errorMsg = null;
        boolean success = true;
        FileUploadDetails res = null;

        Collection<Part> parts = request.getParts();

        String userName = null;
        Cookie cookies [] = request.getCookies();
        for (Cookie c : cookies) {
            if(c.getName().equals("userName"))
                userName = c.getValue();
        }

        StringBuilder fileContent = new StringBuilder();

        for (Part part : parts) {
            fileContent.append(readFromInputStream(part.getInputStream()));

            //Enigma enigma =
            XMLParser xmlParser = new XMLParser();
            try {
                if(xmlParser.XMLIsValid(fileContent.toString())){
                    if (!isFileExist(xmlParser.machine.getBattlefield().getBattleName())) {
                        addXMLMachineToMatchCompetition(xmlParser.machine, userName);
                    }
                    else {
                        errorMsg = " The Battlefield : " + xmlParser.machine.getBattlefield().getBattleName() + ". Already exists";
                    }
                }
            } catch (Exception e) {
                errorMsg = e.getMessage();
            }
        }

        if(success){
            if(errorMsg == null){
                res = new FileUploadDetails(true, "competitionSettings.html");
            }
            else {
                res = new FileUploadDetails(false, errorMsg);
            }
        }

        try(PrintWriter out = response.getWriter()){
            Gson gson = new Gson();
            String json = gson.toJson(res);
            System.out.println(json);
            out.print(json);
        }
    }

    private void addXMLMachineToMatchCompetition(Enigma machine, String userName) {
        ServletUtils utils = new ServletUtils(getServletContext());
        List<Competition> competitions = utils.GetCompetitionListFromContext();
        for(Competition competition: competitions){
            if(competition.getuBoat().getUserName().equals(userName)){
                competition.getuBoat().createMachineWrapper(machine);
                break;
            }
        }
        utils.SetCompetitionList(competitions);
    }

    private boolean isFileExist(String battleName) {
        List<Competition> competitions = (List<Competition>) getServletContext().getAttribute("competitions");
        for(Competition competition: competitions){
            if(competition.getBattlefield().getBattleName().equals(battleName)){
                return true;
            }
        }

        return false;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //doPost(request,response);
        System.out.println("on get");

    }

    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }

    private class FileUploadDetails{
        private boolean isUrl;
        private String msg;

        public FileUploadDetails(boolean isUrl, String msg) {
            this.isUrl = isUrl;
            this.msg = msg;
        }
    }
}
