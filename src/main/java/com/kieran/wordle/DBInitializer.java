package com.kieran.wordle;

import com.kieran.wordle.entity.User;
import com.kieran.wordle.entity.Wordle;
import com.kieran.wordle.repository.UserRepository;
import com.kieran.wordle.repository.WordleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DBInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final WordleRepository wordleRepository;

    @Override
    public void run(String... args) throws Exception {
        User user1 = User.builder()
                .firstName("Kieran")
                .lastName("Mueller")
                .username("YungMuleSack")
                .password("Japs")
                .email("kieran98mueller@gmail.com")
                .phone("612-508-9457")
                .confirmedEmail(true)
                .build();
        User savedUser1 = userRepository.save(user1);

        Wordle wordle1 = Wordle.builder()
                .ownerId(savedUser1.getId())
                .word("tamper")
                .build();
        wordleRepository.save(wordle1);
    }
}
