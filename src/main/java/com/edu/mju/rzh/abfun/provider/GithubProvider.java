package com.edu.mju.rzh.abfun.provider;

import com.alibaba.fastjson.JSON;
import com.edu.mju.rzh.abfun.dto.AccessTokenDTO;
import com.edu.mju.rzh.abfun.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;


import java.io.IOException;

@Component
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
            Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token?client_id=486155bcfa8268ae4825&client_secret=8665a3f6e4c59ded28ca5fe307e16565e7d024e2" +
                        "&code="+accessTokenDTO.getCode()+"&redirect_uri=http://localhost:8082/callback&state=1")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String token = string.split("&")[0].split("=")[1];
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public GithubUser getuser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GithubUser githubUser = (GithubUser) JSON.parseObject(string,GithubUser.class);
            return githubUser;
        } catch (IOException e) {
        }
        return null;
    }
}
