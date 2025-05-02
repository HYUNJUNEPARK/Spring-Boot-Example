package com.mysite.sbb.question;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository : 생성된 DB 테이블의 데이터들을 저장, 조회, 수정, 삭제 등을 할 수 있도록 도와주는 인터페이스. 테이블에 접근하고 데이터를 관리하는 메서드를 제공
 * JpaRepository: JPA 가 제공하는 인터페이스 중 하나로 CRUD 작업을 처리하는 메서드들을 이미 내장하고 있다.
 * JpaRepository<Question, Integer> : Question 의 기본키가 Integer
 */
public interface QuestionRepository extends JpaRepository<Question, Integer> {

    /*
    JPA 에 리포지터리의 메서드명을 분석하여 쿼리를 만들고 실행하는 기능이 있다. findBy + 엔티티의 속성명 ->  입력한 속성의 값으로 데이터 조회
     */
    Question findBySubject(String subject);
    Question findBySubjectAndContent(String subject, String content);
    List<Question> findBySubjectLike(String subject);
    Page<Question> findAll(Pageable pageable);
    Page<Question> findAll(Specification<Question> spec, Pageable pageable); //Specification과 Pageable 객체를 사용하여 DB에서 Question 엔티티를 조회한 결과를 페이징하여 반환

    @Query("select "
            + "distinct q "
            + "from Question q "
            + "left outer join SiteUser u1 on q.author=u1 "
            + "left outer join Answer a on a.question=q "
            + "left outer join SiteUser u2 on a.author=u2 "
            + "where "
            + "   q.subject like %:kw% "
            + "   or q.content like %:kw% "
            + "   or u1.username like %:kw% "
            + "   or a.content like %:kw% "
            + "   or u2.username like %:kw% ")
    Page<Question> findAllByKeyword(@Param("kw") String kw, Pageable pageable);
}