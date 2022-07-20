package com.pado.idleworld.account;

import com.pado.idleworld.domain.Account;
import com.pado.idleworld.domain.AccountRole;
import com.pado.idleworld.exception.DuplicationElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void accountUpdate(String email, AccountUpdateRequest request) {
        Account findAccount = accountRepository.findByEmail(email);
        findAccount.setPassword(request.getPassword());
        findAccount.setImageUrl(request.getImageUrl());
        findAccount.setNickname(request.getNickname());
    }

    @Transactional
    public void accountCreate(SignUpForm request) {
        duplicationCheck(request.getEmail());

        String encPw = passwordEncoding(request.getPassword());
        request.setPassword(encPw);

        Account account = Account.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .nickname(request.getNickname())
                .imageUrl(request.getImageUrl())
                .agree(request.isAgree())
                .role(AccountRole.ROLE_USER) //기본생성 USER로
                .provider(request.getProvider())
                .providerId(request.getProviderId())
                .build();

        accountRepository.save(account);
    }

    public void duplicationCheck(String email) {
        if(accountRepository.existsByEmail(email)) {
            throw new DuplicationElementException();
        }
    }

    public String passwordEncoding(String rawPassword) {
        return bCryptPasswordEncoder.encode(rawPassword);
    }

    public AccountInfoResponse accountRead(String email) {
        Account findAccount = accountRepository.findByEmail(email);
        return AccountInfoResponse.builder()
                .email(findAccount.getEmail())
                .nickname(findAccount.getNickname())
                .imageUrl(findAccount.getImageUrl())
                .agree(findAccount.isAgree())
                //.playListId(findAccount.getPlayList().getId())
                .build();
    }
}
