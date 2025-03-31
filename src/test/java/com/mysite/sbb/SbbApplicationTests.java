package com.mysite.sbb;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerRepository;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SbbApplicationTests {

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;

	@Transactional //메서드가 종료될 때까지 DB 세션 유지
	@Test
	void testJpa() {
		//질문 데이터 저장하기(save)
//		Question q1 = new Question();
//		q1.setSubject("sbb가 무엇인가요?");
//		q1.setContent("sbb에 대해서 알고 싶습니다.");
//		q1.setCreateDate(LocalDateTime.now());
//		this.questionRepository.save(q1);  // 첫번째 질문 저장
//
//		Question q2 = new Question();
//		q2.setSubject("스프링부트 모델 질문입니다.");
//		q2.setContent("id는 자동으로 생성되나요?");
//		q2.setCreateDate(LocalDateTime.now());
//		this.questionRepository.save(q2);  // 두번째 질문 저장

		/* 질문 데이터 조회하기(findByAll)
		 * Optional : findById 로 호출한 값이 존재할 수도 있고, 존재하지 않을 수도 있다.
		 */
//		Optional<Question> oq1 = this.questionRepository.findById(1);
//		if(oq1.isPresent()) {
//			Question q1 = oq1.get();
//			assertEquals("sbb가 무엇인가요?", q1.getSubject());
//		}
//
//		Question q1 = this.questionRepository.findBySubject("sbb가 무엇인가요?");
//		assertEquals(1, q1.getId());
//
//		Question q2 = this.questionRepository.findBySubjectAndContent("sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");
//		assertEquals(1, q2.getId());

		//질문 데이터 수정하기(set -> save)
//		Optional<Question> oq2 = this.questionRepository.findById(1);
//		assertTrue(oq2.isPresent());
//		Question q3 = oq2.get();
//		q3.setSubject("수정된 제목");
//		this.questionRepository.save(q3);

		//질문 데이터 삭제하기(delete)
//		assertEquals(2, this.questionRepository.count());
//		Optional<Question> oq = this.questionRepository.findById(1);
//		assertTrue(oq.isPresent());
//		Question q = oq.get();
//		this.questionRepository.delete(q);
//		assertEquals(1, this.questionRepository.count());

		//답변 데이터 저장하기(save)
//		Optional<Question> oq = this.questionRepository.findById(2);
//		assertTrue(oq.isPresent());
//		Question q = oq.get();
//
//		Answer a = new Answer();
//		a.setContent("네 자동으로 생성됩니다.");
//		a.setQuestion(q);  // 어떤 질문의 답변인지 알기위해서 Question 객체가 필요하다.
//		a.setCreateDate(LocalDateTime.now());
//		this.answerRepository.save(a);

		//답변 조회하기(get)
//		Optional<Answer> oa = this.answerRepository.findById(1);
//		assertTrue(oa.isPresent());
//		Answer a1 = oa.get();
//		assertEquals(2, a1.getQuestion().getId());

		//질문에 해당하는 답변 조회
		Optional<Question> oq = this.questionRepository.findById(2); //**테스트 코드에서 findById 메서드를 통해 객체를 조회하고 나면 DB 세션이 끊긴다. -> @Transactional 적용
		assertTrue(oq.isPresent());
		Question q = oq.get();

		List<Answer> answerList = q.getAnswerList();

		assertEquals(1, answerList.size());
		assertEquals("네 자동으로 생성됩니다.", answerList.get(0).getContent());
	}
}
