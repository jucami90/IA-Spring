package com.test.ia_prompt.service;

import com.test.ia_prompt.record.Answer;
import com.test.ia_prompt.record.Question;

public interface BoardService {
    Answer askQuestion(Question question);
}
