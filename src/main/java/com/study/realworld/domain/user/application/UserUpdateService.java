package com.study.realworld.domain.user.application;

import com.study.realworld.domain.user.domain.Email;
import com.study.realworld.domain.user.domain.User;
import com.study.realworld.domain.user.domain.UserRepository;
import com.study.realworld.domain.user.exception.AlreadyExistEmailException;
import com.study.realworld.domain.user.exception.EmailNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class UserUpdateService {

    private final UserRepository userRepository;

    public UserUpdateService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User update(final User updateRequestUser, final Email principal) {
        final User user = findUserByEmail(principal);
        final Email email = updateRequestUser.email();
        validateDuplicatedEmail(email);
        return user.changeEmail(email)
                .changeBio(updateRequestUser.bio())
                .changeImage(updateRequestUser.image());
    }

    private void validateDuplicatedEmail(final Email email) {
        if (userRepository.existsByEmail(email)) {
            throw new AlreadyExistEmailException(email.email());
        }
    }

    private User findUserByEmail(final Email email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException(email.email()));
    }

}
