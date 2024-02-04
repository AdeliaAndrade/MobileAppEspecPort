package com.example.mobile_espec_port;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProcessAnalysesItems {
    public ArrayList<ProcessAnalysesItem> ProcessAnalysesItems;

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();

        try {
            JSONArray jsonArray = new JSONArray();

            // Para cada item em ProcessAnalysesItems, crie um JSONObject
            for (ProcessAnalysesItem item : ProcessAnalysesItems) {
                JSONObject itemObject = new JSONObject();
                itemObject.put("Index", item.Index);
                itemObject.put("Value", item.Value);

                jsonArray.put(itemObject);
            }

            // Adicione o array ao objeto principal
            jsonObject.put("ProcessAnalysesItems", jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}
