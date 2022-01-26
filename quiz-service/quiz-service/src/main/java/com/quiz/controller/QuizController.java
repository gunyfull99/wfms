package com.quiz.controller;

import com.quiz.Dto.*;
import com.quiz.entity.Category;
import com.quiz.entity.Nominee;
import com.quiz.entity.Quiz;
import com.quiz.entity.QuizQuestion;
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
    public List<QuestionRequest> getQuestionByCategory(@PathVariable("category") String name) {
        return quesTionService.getQuestionByCategory(name);
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

    //http://localhost:8080/quiz/getAllCategory
    @GetMapping("/getAllCategory")
    public List<CategoryRequest> getAllCategory() {
        return categoryService.getAllCategory();
    }
    //http://localhost:8080/quiz/cate/list
    @GetMapping("/cate/list")
    public List<Category> getAllCate() {
        return categoryService.getAllCate();
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
    // get list quiz by user
    // http://localhost:8080/quiz/listbyuser/2
    @GetMapping("/listbyuser/{id}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Add success", response = Quiz.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<List<Quiz>> getListQuizByUser(@PathVariable("id") long id) throws ResourceBadRequestException {
        return ResponseEntity.ok().body(quizService.getListQuizByUser(id));
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
    // start quiz( có thể là bắt đầu làm bài mới hoặc có thể dùng làm xem lịch sử quiz đã làm )
    // http://localhost:8080/quiz/detailbyuser/2
    @GetMapping("/detailbyuser/{qizid}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Add success", response = Quiz.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public ResponseEntity<List<QuizQuestionForm>> getDetailQuizByUser(@PathVariable("qizid") long id) throws ResourceBadRequestException {
        return ResponseEntity.ok().body(quizService.getDetailByUser1(id));
    }

    // calculate quiz
    // http://localhost:8080/quiz/calculate
    @PostMapping("/calculate")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Add success", response = Quiz.class),
            @ApiResponse(code = 400, message = "Bad Request", response = BaseResponse.class),
            @ApiResponse(code = 401, message = "Unauthorization", response = BaseResponse.class),
            @ApiResponse(code = 403, message = "Forbidden", response = BaseResponse.class),
            @ApiResponse(code = 500, message = "Failure", response = BaseResponse.class)})
    public int calculate(@RequestBody Quiz quiz) throws ResourceBadRequestException {
        return quizService.calculateScore(quiz);
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
