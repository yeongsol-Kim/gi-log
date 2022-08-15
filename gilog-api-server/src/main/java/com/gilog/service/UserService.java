package com.gilog.service;

import com.gilog.dto.UserDto;
import com.gilog.entity.User;
import com.gilog.exception.BadRequestException;
import com.gilog.jwt.JwtProvider;
import com.gilog.repository.UserRepositoryInt;
import com.google.gson.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Objects;

@Service
@Transactional
@Slf4j
public class UserService {

    private final UserRepositoryInt userRepositoryInt;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Value("${kakao.redirect-url}")
    private String kakaoRedirectUrl;


    public UserService(UserRepositoryInt userRepositoryInt, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.userRepositoryInt = userRepositoryInt;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }


    public String login(String email, String password) {
        User account = userRepositoryInt
                .findByUsername(email).orElseThrow(() -> new BadRequestException("아이디 혹은 비밀번호를 확인하세요."));
        checkPassword(password, account.getPassword());
        return jwtProvider.createToken(account.getUsername(), account.getRole());
    }

    public Long getUserGiLogCountByUsername(String username) {
        User user = userRepositoryInt.findByUsername(username).orElseThrow();
        Long count = user.getGilogCount();
        user.setGilogCount(count +1);
        userRepositoryInt.save(user);
        return count;
    }

    public Long setUserGiLogCountByUsername(String username) {
        return userRepositoryInt.findByUsername(username).orElseThrow().getGilogCount();
    }

    private void checkPassword(String password, String encodedPassword) {
        boolean isSame = passwordEncoder.matches(password, encodedPassword);
        if(!isSame) {
            throw new BadRequestException("아이디 혹은 비밀번호를 확인하세요.");
        }
    }

    public UserDto getByUsername(String username) {
        User user = userRepositoryInt.findByUsername(username).orElseThrow();

        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .age(user.getAge())
                .gender(user.getGender())
                .regDatetime(user.getRegDatetime())
                .gilogCount(user.getGilogCount())
                .build();

        return userDto;
    }

    // 액세스 토큰 발급
    public String getAccessToken (String authorize_code) {
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // POST 요청을 위해 기본값이 false인 setDoOutput을 true로

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            // POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=ee4ee61f1ea69f5a8d5f5924343083f7"); //본인이 발급받은 key
//            sb.append("&client_id=a5566d5455e615f046de006fd9973957"); //본인이 발급받은 admin key
            sb.append("&redirect_uri=" + kakaoRedirectUrl); // 본인이 설정한 주소
            sb.append("&code=" + authorize_code);
            bw.write(sb.toString());
            bw.flush();

            // 결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
//            System.out.println("responseCode : " + responseCode);

            // 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
//            System.out.println("response body : " + result);

            // Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

//            System.out.println("access_token : " + access_Token);
//            System.out.println("refresh_token : " + refresh_Token);

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return access_Token;
    }

    //
    public String createKakaoUser(String token)  {

        String reqURL = "https://kapi.kakao.com/v2/user/me";

        //access_token을 이용하여 사용자 정보 조회
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token); //전송할 header 작성, access_token전송

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
//            System.out.println("responseCode : " + responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
//             System.out.println("response body : " + result);

            //Gson 라이브러리로 JSON파싱
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            String id = element.getAsJsonObject().get("id").getAsString();

//            boolean hasEmail = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean();
//            boolean emailNeedsAgreement = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email_needs_agreement").getAsBoolean();
//            String uid = "";
//            if (hasEmail && !emailNeedsAgreement) {
//                // 이메일이 있고 접근 허용 했다면 이메일을 고유 아이디로 사용
//                uid = element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString();
//            } else {
//                // 카카오 고유 user id로 사용
//                uid = element.getAsJsonObject().get("id").getAsString();
//            }



//            System.out.println("id : " + id);
//            System.out.println("email : " + email);


            User isUser = userRepositoryInt.findByUsername(id).orElse(null);

            // 카카오 정보로 회원가입
            if (isUser == null) {
                // 패스워드 인코딩
                // String encodedPassword = passwordEncoder.encode(password);
                // ROLE = 사용자


                User user = User.builder()
                        .username(id)
                        .password(id)
                        .nickname("이름")
                        .age(0)
                        .gender("남자")
                        .regDatetime(LocalDateTime.now())
                        .gilogCount(1L)
                        .role("[ROLE_USER]")
                        .build();
                userRepositoryInt.save(user);
            }


//            // 로그인 처리
//            Authentication kakaoUsernamePassword = new UsernamePasswordAuthenticationToken(username, password);
//            //  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { 로 진행됨
//
//            Authentication authentication = authenticationManager.authenticate(kakaoUsernamePassword);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            return username;


            br.close();
            return jwtProvider.createToken(id, "USER");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Asd";
    }

    public String userIdFromApple(String idToken) {
        StringBuffer result = new StringBuffer();
        try {
            URL url = new URL("https://appleid.apple.com/auth/keys");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";

            while ((line = br.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            System.out.println("error");
        }

        JsonParser parser = new JsonParser();
        JsonObject keys = (JsonObject) parser.parse(result.toString());
        JsonArray keyArray = (JsonArray) keys.get("keys");


        //클라이언트로부터 가져온 identity token String decode
        String[] decodeArray = idToken.split("\\.");
        String header = new String(Base64.getDecoder().decode(decodeArray[0]));

        //apple에서 제공해주는 kid값과 일치하는지 알기 위해
        JsonElement kid = ((JsonObject) parser.parse(header)).get("kid");
        JsonElement alg = ((JsonObject) parser.parse(header)).get("alg");

        //써야하는 Element (kid, alg 일치하는 element)
        JsonObject avaliableObject = null;
        for (int i = 0; i < keyArray.size(); i++) {
            JsonObject appleObject = (JsonObject) keyArray.get(i);
            JsonElement appleKid = appleObject.get("kid");
            JsonElement appleAlg = appleObject.get("alg");

            if (Objects.equals(appleKid, kid) && Objects.equals(appleAlg, alg)) {
                avaliableObject = appleObject;
                break;
            }
        }

        //일치하는 공개키 없음
        if (ObjectUtils.isEmpty(avaliableObject))
            throw new IllegalArgumentException("맞는 키를 찾을 수 없음");

        PublicKey publicKey = this.getPublicKey(avaliableObject);
        //--> 여기까지 검증

        Claims userInfo = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(idToken).getBody();
        JsonObject userInfoObject = (JsonObject) parser.parse(new Gson().toJson(userInfo));
        JsonElement appleAlg = userInfoObject.get("sub");
        String userId = appleAlg.getAsString();


        User isUser = userRepositoryInt.findByUsername(userId).orElse(null);

        // 카카오 정보로 회원가입
        if (isUser == null) {
            // 패스워드 인코딩
            // String encodedPassword = passwordEncoder.encode(password);
            // ROLE = 사용자


            User user = User.builder()
                    .username(userId)
                    .password(userId)
                    .nickname("이름")
                    .age(0)
                    .gender("남자")
                    .regDatetime(LocalDateTime.now())
                    .gilogCount(1L)
                    .role("[ROLE_USER]")
                    .build();
            userRepositoryInt.save(user);

        }


        return jwtProvider.createToken(userId, "USER");
    }

    public PublicKey getPublicKey(JsonObject object) {
        String nStr = object.get("n").toString();
        String eStr = object.get("e").toString();

        byte[] nBytes = Base64.getUrlDecoder().decode(nStr.substring(1, nStr.length() - 1));
        byte[] eBytes = Base64.getUrlDecoder().decode(eStr.substring(1, eStr.length() - 1));

        BigInteger n = new BigInteger(1, nBytes);
        BigInteger e = new BigInteger(1, eBytes);

        try {
            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
            return publicKey;
        } catch (Exception exception) {
            throw new IllegalArgumentException("키값  없음");
        }
    }

    public Long getIdByUsername(String username) {
        return userRepositoryInt.findByUsername(username).orElse(null).getId();
    }

    // 로그인 유저 정보
    public UserDto getMyInfo(String username) {
        User user = userRepositoryInt.findByUsername(username).orElse(null);

        return UserDto.builder()
                .gender(user.getGender())
                .age(user.getAge())
                .regDatetime(user.getRegDatetime())
                .nickname(user.getNickname())
                .username(user.getUsername())
                .gilogCount(1L)
                .build();
    }

    // 유저 정보 등록/수정
    public Long setUserInfo(UserDto userDto) {
        User user = userRepositoryInt.findByUsername(userDto.getUsername()).orElse(null);
        user.setAge(userDto.getAge());
        user.setGender(userDto.getGender());
        user.setNickname(userDto.getNickname());
        if (user == null) {
            return 0L;
        }

        return userRepositoryInt.save(user).getId();
    }

    public void deleteUser(String username) {
        User user = userRepositoryInt.findByUsername(username).orElseThrow();
        userRepositoryInt.delete(user);
    }

}

