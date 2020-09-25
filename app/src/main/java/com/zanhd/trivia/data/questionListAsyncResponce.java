package com.zanhd.trivia.data;

import com.zanhd.trivia.model.Question;

import java.util.ArrayList;

public interface questionListAsyncResponce {
    void processFinished(ArrayList<Question> questionArrayList);
}
