//package com.kieran.wordle;
//
//import com.kieran.wordle.entity.User;
//import com.kieran.wordle.entity.Wordle;
//import com.kieran.wordle.repository.UserRepository;
//import com.kieran.wordle.repository.WordleRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class DBInitializer implements CommandLineRunner {
//
//    private final UserRepository userRepository;
//    private final WordleRepository wordleRepository;
//    private final BCryptPasswordEncoder encoder;
//
//    @Override
//    public void run(String... args) throws Exception {
//        User user1 = User.builder()
//                .firstName("Kieran")
//                .lastName("Mueller")
//                .username("test")
//                .password("test")
//                .email("kieran@gmail.com")
//                .phone("612-508-9457")
//                .confirmedEmail(true)
//                .currentWordlesOwned(1)
//                .build();
//        user1.setPassword(encoder.encode(user1.getPassword()));
//        User savedUser1 = userRepository.save(user1);
//
//        Wordle wordle1 = Wordle.builder()
//                .ownerId(savedUser1.getId())
//                .word("tamper")
//                .build();
//        wordleRepository.save(wordle1);
//    }
//}
