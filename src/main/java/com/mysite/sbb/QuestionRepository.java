package com.mysite.sbb;

import org.springframework.data.jpa.repository.JpaRepository;

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

}