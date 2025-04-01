package com.mysite.sbb.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; //빈으로 등록한 패스워드 객체를 주입받아 사용할 수 있도록 수정

    public SiteUser create(String username, String email, String password) {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setEmail(email);
        //비크립트는 해시 함수의 하나로 주로 비밀번호롸 같은 보안 정보를 안정하게 저장하고 검증할 때 사용하는 암호화 기술
        //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); //@Bean 으로 등록해서 사용
        user.setPassword(passwordEncoder.encode(password));
        this.userRepository.save(user);
        return user;
    }
}
