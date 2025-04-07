package com.mysite.sbb.question;

import com.mysite.sbb.answer.AnswerForm;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

/*
 컨트롤러 -> 서비스 -> 리포지터리 -> 데이터를 템플릿에 전달
 */
@RequestMapping("/question")
@RequiredArgsConstructor //롬복(Lombok)이 제공하는 애너테이션으로, final이 붙은 속성을 포함하는 생성자를 자동으로 만들어 주는 역할
@Controller
public class QuestionController {
    //private final QuestionRepository questionRepository;
    private final QuestionService questionService;
    private final UserService userService;

    /*
        컨트롤러에서 리포지터리를 직접 호출하지 않고 서비스를 두어 데이터를 처리한다.
     */
//    @GetMapping("/question/list")
//    public String list(Model model) {
//        List<Question> questionList = this.questionRepository.findAll();
//        model.addAttribute("questionList", questionList);
//        return "question_list";
//    }

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page) {
        Page<Question> paging = this.questionService.getList(page);
        model.addAttribute("paging", paging);
        return "question_list";
    }

    /*
     해당 코드가 비활성화 되어 있지 않다면, 바로 위의 페이징 처리 메서드와 충동해 예외 발생함
     */
//    @GetMapping("/list")
//    public String list(Model model) {
//        List<Question> questionList = this.questionService.getList();
//        model.addAttribute("questionList", questionList);
//        return "question_list";
//    }

    @GetMapping(value = "/detail/{id}")
    public String detail(
        Model model,
        @PathVariable("id") Integer id,
        AnswerForm answerForm
    ) {
        Question question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "question_detail";
    }

    /**
     * @param questionForm
     * @param bindingResult: subject, content 항목을 지닌 폼이 전송되면 QuestionForm의 subject, content 속성이 자동으로 바인딩
     *
     * @PreAuthorize("isAuthenticated()") 로그인한 사용자만 호출 가능. 애너테이션이 붙은 메서드는 로그인한 경우에만 실행
     * BindingResult 매개변수는 항상 @Valid 매개변수 바로 뒤에 위치해야 한다. 만약 두 매개변수의 위치가 정확하지 않다면 @Valid만 적용되어 입력값 검증 실패 시 400 오류가 발생한다.
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String questionCreate(
        @Valid QuestionForm questionForm,
        BindingResult bindingResult,
        Principal principal
    ) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }

        //로그인 중인 사용자 데이터
        SiteUser siteUser = this.userService.getUser(principal.getName());

        this.questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(
        QuestionForm questionForm,
        @PathVariable("id") Integer id,
        Principal principal
    ) {
        //현재 로그인한 사용자와 질문의 작성자가 동일하지 않을 경우
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        //수정할 질문의 제목과 내용을 화면에 보여주기 위해 questionForm 객체에 id 값으로 조회한 subject, object 내용을 담아서 템플릿에 전달한다.
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());

        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(
        @Valid QuestionForm questionForm,
        BindingResult bindingResult,
        Principal principal,
        @PathVariable("id") Integer id
    ) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }

        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());

        return String.format("redirect:/question/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.questionService.delete(question);
        return "redirect:/";
    }
}
