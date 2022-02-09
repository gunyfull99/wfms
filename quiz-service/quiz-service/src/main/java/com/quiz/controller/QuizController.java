package com.quiz.controller;

import com.quiz.Dto.*;
import com.quiz.entity.*;
import com.quiz.exception.ResourceBadRequestException;
import com.quiz.service.CategoryService;
import com.quiz.service.NomineeService;
import com.quiz.service.QuesTionService;
import com.quiz.service.QuizService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/quiz")
public class QuizController {


    @Autowired
    private QuizService quizService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    QuesTionService quesTionService;

    @Autowired
    NomineeService nomineeService;

    //http://localhost:8080/quiz/createquestion
    @PostMapping("/createquestion")
    public void createQuestion(@RequestBody QuestionRequest request) {
        quesTionService.createQuestion(request);
    }

    //http://localhost:8080/quiz/editquestion
    @PutMapping("/editquestion")
    public void editQuestion(@RequestBody QuestionEditRequest request) {
        quesTionService.editQuestion(request);
    }

    //http://localhost:8080/quiz/getquestionbycategory/category
    @GetMapping("/getquestionbycategory/{category}")
    public List<QuestDTO> getQuestionByCategory(@PathVariable("category") long  id) {
        return quesTionService.getQuestionByCategory(id);
    }
    //http://localhost:8080/quiz/getAllQuestion
    @GetMapping("/getAllQuestion")
    public List<QuestionRequest> getAllQuestion() {
        return quesTionService.getAllQuestion();
    }
    //http://localhost:8080/quiz/createCategory
    @PostMapping("/createCategory")
    public void createCategory(@RequestBody CategoryRequest category) {
        categoryService.createCategory(category);
    }

    //http://localhost:8080/quiz/editCategory
    @PutMapping("/editCategory")
    public void editCategory(@RequestBody CategoryEditRequest category) {
        categoryService.editCategory(category);
    }

//    //http://localhost:8080/quiz/getAllCategory
//    @GetMapping("/getAllCategory")
//    public List<CategoryRequest> getAllCategory() {
//        return categoryService.getAllCategory();
//    }
    //http://localhost:8080/quiz/cate/list
    @GetMapping("/cate/list")
    public List<Category> getAllCate() {
        return categoryService.getAllCate();
    }

    //http://localhost:8080/quiz/getAllQuestionType
    @GetMapping("/getAllQuestionType")
    public List<QuestionType> getAllQuestionType() {
        return quesTionService.getAllQuestionType();
    }
    //http://localhost:8080/quiz/createquesiontype
    @PostMapping("/createquesiontype")
    public void createCategory(@RequestBody QuestionTypeRequest category) {
        quesTionService.createQuestionType(category);
    }

    //http://localhost:8080/quiz/createnominee
    @PostMapping("/createnominee")
    public void createNominee(@RequestBody NomineeRequest nomineeRequest) {
        nomineeService.createNominee(nomineeRequest);
    }

    //http://localhost:8080/quiz/editnominee
    @PutMapping("/editnominee")
    public void editNominee(@RequestBody NomineeEditRequest nomineeRequest) {
        nomineeService.editNominee(nomineeRequest);
    }

    //http://localhost:8080/quiz/deletenominee
    @PutMapping("/deletenominee/{id}")
    public void deleteNominee(@PathVariable("id") Long id) {
        nomineeService.deleteNominee(id);
    }

    //http://localhost:8080/quiz/nominee/list
    @GetMapping("/nominee/list")
    public List<Nominee> getAllNominee() {
      return   nomineeService.getAll();
    }

    // Create quiz
    // http://localhost:8080/quiz
    @PostMapping("")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Add success", response = Quiz.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<Quiz> createQuiz(@Valid @RequestBody CreateQuizForm form) throws ResourceBadRequestException {
        return new ResponseEntity<Quiz>(quizService.addQuesToQuiz(form), HttpStatus.CREATED);
    }

    // get detail quiz
    // http://localhost:8080/quiz/1
    @GetMapping("/{id}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Add success", response = Quiz.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<Quiz> getDetailQuiz(@PathVariable("id") long id) throws ResourceBadRequestException {
        return ResponseEntity.ok().body(quizService.getDetailQuiz(id));
    }
    // get list quiz by user when done
    // http://localhost:8080/quiz/listbyuser/2
    @GetMapping("/listbyuser/{id}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Add success", response = Quiz.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<List<Quiz>> getListQuizByUserWhenDone(@PathVariable("id") long id) throws ResourceBadRequestException {
        return ResponseEntity.ok().body(quizService.getListQuizByUserWhenDone(id));
    }

    // get all quiz by user
    // http://localhost:8080/quiz/all/2
    @GetMapping("/all/{id}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Add success", response = Quiz.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<List<Quiz>> getAllQuizByUser(@PathVariable("id") long id) throws ResourceBadRequestException {
        return ResponseEntity.ok().body(quizService.getAllQuizByUser(id));
    }
    // get list quiz not start by user
    // http://localhost:8080/quiz/notstart/2
    @GetMapping("/notstart/{id}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Add success", response = Quiz.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<List<Quiz>> getListQuizNotStartByUser(@PathVariable("id") long id) throws ResourceBadRequestException {
        return ResponseEntity.ok().body(quizService.getListQuizNotStart(id));
    }
    // start quiz
    // http://localhost:8080/quiz/startquiz/1952
    @GetMapping("/startquiz/{qizid}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Add success", response = Quiz.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<List<QuestDTO>> getListQuestionByQuizId(@PathVariable("qizid") long id) throws ResourceBadRequestException {
        return ResponseEntity.ok().body(quesTionService.getListQuestionByQuizId(id));
    }
    // get list choice by questionId
    // http://localhost:8080/quiz/listchoice/54
    @GetMapping("/listchoice/{questionid}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Add success", response = Quiz.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<List<QuestionChoice>> getListChoiceByQuestionId(@PathVariable("questionid") long id) throws ResourceBadRequestException {
        return ResponseEntity.ok().body(quizService.getListChoiceByQuestionId(id));
    }
    // calculate quiz
    // http://localhost:8080/quiz/calculate
    @PostMapping("/calculate")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Add success", response = Quiz.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public Quiz calculate(@RequestBody List<QuestDTO>  questDTO) throws ResourceBadRequestException {
        return quizService.calculateScore(questDTO);
    }
    // http://localhost:8080/quiz/getname
    @PostMapping("/getname")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Add success", response = Quiz.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public List<Object> getName() throws ResourceBadRequestException {
        return quizService.getName();
    }
}
