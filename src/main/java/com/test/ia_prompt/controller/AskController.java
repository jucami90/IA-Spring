package com.test.ia_prompt.controller;

import com.test.ia_prompt.record.Answer;
import com.test.ia_prompt.record.Question;
import com.test.ia_prompt.service.BoardService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AskController {

    private final BoardService boardGameService;

    public AskController(BoardService boardGameService) {
        this.boardGameService = boardGameService;
    }

    @PostMapping(path="/ask", produces="application/json")
    public Answer ask(@RequestBody Question question) {
        return boardGameService.askQuestion(question);
    }

}