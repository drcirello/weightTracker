package com.drcir.weighttracker;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;

public class TestData {

    public static List<WeightEntry> getTestData(){
        Gson gson = new Gson();
        String test1 = "[\n";

        Long baseline = System.currentTimeMillis();
        int baseweight = 200;
        int loops = 200;
        Random rand = new Random();
        for(int i = 0; i <= loops; i++) {
            test1 += "{\n" + "\"date\": \"";
            test1 += baseline - (Long)(i * 3 * 86400000L);
            test1 += "\",\n" + "\"weight\": ";

            int n = rand.nextInt(4) + 1;
            baseweight += 1;
            /*
            if(baseweight < 150)
                baseweight += n;
            else if (baseweight > 250)
                baseweight -= n;
            else if (n % 2 == 0)
                 baseweight += n;
            else
                baseweight -= n;
            */
            test1 += baseweight;
            test1 += ",\n" + "\"dateEntered\": \"";
            test1 += baseline - (Long)(i * 86400000L);
            test1 += "\",\n" + "\"active\": ";
            if (n % 4 == 0)
                test1 += "false";
            else
                test1 +="true";
            test1 += "\n";
            if(i != loops)
                test1 += "},\n";
            else
                test1 += "}\n" +
                        "]\n";
        }

        String test = "[\n" +
                "{\n" +
                "\"date\": \"1232254800000\",\n" +
                "\"weight\": 200,\n" +
                "\"dateEntered\": \"1232254800000\",\n" +
                "\"active\": true\n" +
                "},\n" +
                "{\n" +
                "\"date\": \"1232341200000\",\n" +
                "\"weight\": 204,\n" +
                "\"dateEntered\": \"1232341200000\",\n" +
                "\"active\": false\n" +
                "},\n" +
                "{\n" +
                "\"date\": \"1232427600000\",\n" +
                "\"weight\": 204,\n" +
                "\"dateEntered\": \"1232427600000\",\n" +
                "\"active\": true\n" +
                "},\n" +
                "{\n" +
                "\"date\": \"1232514000000\",\n" +
                "\"weight\": 193,\n" +
                "\"dateEntered\": \"1232514000000\",\n" +
                "\"active\": true\n" +
                "},\n" +
                "{\n" +
                "\"date\": \"1232600400000\",\n" +
                "\"weight\": 203,\n" +
                "\"dateEntered\": \"1232600400000\",\n" +
                "\"active\": true\n" +
                "},\n" +
                "{\n" +
                "\"date\": \"1232686800000\",\n" +
                "\"weight\": 200,\n" +
                "\"dateEntered\": \"1232686800000\",\n" +
                "\"active\": true\n" +
                "},\n" +
                "{\n" +
                "\"date\": \"1232773200000\",\n" +
                "\"weight\": 205,\n" +
                "\"dateEntered\": \"1232773200000\",\n" +
                "\"active\": true\n" +
                "},\n" +
                "{\n" +
                "\"date\": \"1232859600000\",\n" +
                "\"weight\": 199,\n" +
                "\"dateEntered\": \"1232859600000\",\n" +
                "\"active\": true\n" +
                "},\n" +
                "{\n" +
                "\"date\": \"1232946000000\",\n" +
                "\"weight\": 196,\n" +
                "\"dateEntered\": \"1232946000000\",\n" +
                "\"active\": false\n" +
                "},\n" +
                "{\n" +
                "\"date\": \"1233032400000\",\n" +
                "\"weight\": 195,\n" +
                "\"dateEntered\": \"1233032400000\",\n" +
                "\"active\": true\n" +
                "},\n" +
                "{\n" +
                "\"date\": \"1233118800000\",\n" +
                "\"weight\": 198,\n" +
                "\"dateEntered\": \"1233118800000\",\n" +
                "\"active\": true\n" +
                "},\n" +
                "{\n" +
                "\"date\": \"1233205200000\",\n" +
                "\"weight\": 210,\n" +
                "\"dateEntered\": \"1233205200000\",\n" +
                "\"active\": false\n" +
                "},\n" +
                "{\n" +
                "\"date\": \"1233291600000\",\n" +
                "\"weight\": 206,\n" +
                "\"dateEntered\": \"1233291600000\",\n" +
                "\"active\": true\n" +
                "}\n" +
                "]\n";

        Type listType = new TypeToken<List<WeightEntry>>(){}.getType();
        List<WeightEntry> we = gson.fromJson(test1, listType);
        return we;
    }

}
