package com.school.sprint1.Service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.school.sprint1.DButil.StaffDB;
import com.school.sprint1.DButil.StudentDB;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

import java.util.*;

@Service
public class GradesService {

    public String getIds(String Term){
        if(Term.length() == 0) return "ERROR";
        Map<String,String> mapStudents = new StudentDB().getStudents(Term);
        if(mapStudents == null || mapStudents.size()==0) return "ERROR";
        String res = getGson(mapStudents);
        return  res;
    }
    public String getTermRange(String ID){
        Gson gson = new Gson();
        Map<String,String> mapStudents = new StudentDB().getTermRange(ID);
        if(mapStudents == null || mapStudents.size()==0) return "ERROR";
        mapStudents.put("startyear",mapStudents.get("startyear").substring(0,2));
        mapStudents.put("currentyear",mapStudents.get("currentyear").substring(0,2));
        String res = gson.toJson(mapStudents);
        return  res;
    }
    public String showGrades(String input){
        if(input.length() == 0) return "ERROR";

        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> userData = gson.fromJson(input, type);
        Map<String, String> grades = new StudentDB().getTermGrades(userData.get("id"), userData.get("term"));

        if(grades == null || grades.size()==0) return "ERROR";

        String res = getGradesGson(grades);
        return res;
    }
    public boolean saveGrades(String input){
        if (input.length() == 0) return false;
        boolean res = getValues(input);
        return res;
    }
    private boolean getValues(String input){
        String subject;
        Gson gson = new Gson();
        Map<String,String> values = new HashMap<>();
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> userData = gson.fromJson(input, type);
        values.put("subject",(String) userData.get("subject"));
        ArrayList arr = (ArrayList) userData.get("students");

        for(int i=0; i<arr.size(); i++){
            Map<String, String> studentGrade = (Map<String, String>) arr.get(i);
            values.put("St_Id", studentGrade.get("id"));
            values.put("Exam_grade", studentGrade.get("grade"));
            Map<String,String> momo = new StudentDB().getTermRange(studentGrade.get("id"));
            values.put("Term_id",momo.get("currentyear"));
            values.put("yearWorks",(String) userData.get(""));
            values.put("gradeflag","1");
            int state = Integer.parseInt(studentGrade.get("grade"));
            if(state >= 60 ) state=1;
            else state=0;
            values.put("state", Integer.toString(state));
            String sqlValues = valuesToString(values);
            if (! new StaffDB().addGrade(sqlValues)) return false;
        }
        return true;
    }
    private String valuesToString(Map<String,String> values){
        String res=  "(\"" + values.get("subject") + "\"," +
                "\"" + values.get("St_Id") + "\"," +
                "\"" + values.get("Term_id") + "\"," +
                "0" + "," +
                values.get("Exam_grade") + "," +
                values.get("gradeflag") + "," +
                values.get("state") + ")";
        System.out.println(res);
        return  res;

    }
    private String getGson(Map<String,String> mapStudents){
        Map<String,String> map = new HashMap();
        String[] res = new String[mapStudents.size()];
        Gson gson = new Gson();
        int i=0;
        for (Map.Entry<String,String> entry : mapStudents.entrySet()) {
            map.put("id", entry.getKey());
            map.put("name", entry.getValue());
            map.put("grade","");
            res[i++] = gson.toJson(map);
        }
        return gson.toJson(res);
    }
    private String getGradesGson(Map<String,String> mapGrades){
        Map<String,String> map = new HashMap();
        String[] res = new String[mapGrades.size()];
        Gson gson = new Gson();
        int i=0;
        for (Map.Entry<String,String> entry : mapGrades.entrySet()) {
            map.put("subject", entry.getKey());
            map.put("grade", entry.getValue());
            res[i++] = gson.toJson(map);
        }
        return gson.toJson(res);
    }
}