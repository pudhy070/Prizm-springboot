package com.example.aigatewayservice.service;

import com.example.aigatewayservice.entity.User;
import com.example.aigatewayservice.entity.Role;
import com.example.aigatewayservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email;
        String name;

        if (registrationId.equals("google")) {
            email = (String) attributes.get("email");
            name = (String) attributes.get("name");
        } else if (registrationId.equals("kakao")) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            email = (String) kakaoAccount.get("email");
            name = (String) profile.get("nickname");
        } else {
            throw new OAuth2AuthenticationException("Unsupported provider: " + registrationId);
        }

        User user = userRepository.findByUsername(email)
                .orElseGet(() -> userRepository.save(User.builder().username(email).displayName(name).role(Role.USER).provider(registrationId).build()));
        return user;
    }
}