package com.zanhd.trivia.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.zanhd.trivia.controller.AppController;
import com.zanhd.trivia.model.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuestionBank {

    ArrayList<Question> questionArrayList = new ArrayList<>();
    //TODO : chance below url to genuine quiz url (hard to find for me)
    String url = "https://jsonplaceholder.typicode.com/todos";//https://opentdb.com/api.php?amount=50&type=boolean";

    public List<Question> getQuestions(final questionListAsyncResponce callBack){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("JSON Stuff", "onResponse: " + response);

                        for(int i=0 ; i <response.length() ; i++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);

                                //now creating objects of class Question and then we will insert all in ArrayList<Question>
                                Question question = new Question();
                                question.setQuestion(jsonObject.getString("title"));
                                question.setAnswer(jsonObject.getBoolean("completed"));

                                //Add question to ArrayList<Question>
                                questionArrayList.add(question);
                                //Log.d("Loop", "onResponse: " + jsonObject.getInt("id"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        //after creating interface(quesionListAsyncResponce) add below line
                        if(callBack != null) callBack.processFinished(questionArrayList);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("JSON Error", "onErrorResponse: " + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        return questionArrayList;
    }
}
