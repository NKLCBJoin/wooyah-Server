package com.wooyah.service;

import com.wooyah.dto.user.UserSignUpDto;
import com.wooyah.entity.User;
import com.wooyah.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    //이메일 db 조회후 처리하기
    public String emailcheck(UserSignUpDto userSignUpDto) {


        if (userRepository.findByEmail(userSignUpDto.getEmail()).isPresent()) {
            return "이메일 정보가 있습니다.";
        }
        else {
            User user = User.builder()
                    .email(userSignUpDto.getEmail())
                    .nickname(userSignUpDto.getNickname())
                    .phone(userSignUpDto.getPhone())
                    .deviceNumber("1234") //넣어둠
                    .location("예시 위치") //넣어둠
                    //필요한거 추가해서 db에 저장
                    .build();

            userRepository.save(user);
            return "회원가입 완료";
        }
    }
}
