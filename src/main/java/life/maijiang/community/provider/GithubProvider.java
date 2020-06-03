package life.maijiang.community.provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import life.maijiang.community.dto.AccessTokenDTO;
import life.maijiang.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {

    /**
     * OKHTTP3 POST请求
     * @param accessTokenDTO
     * @return access_token(GitHUb授权码)
     */
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON.toJSONString(accessTokenDTO),mediaType);
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String acessToken = string.split("&")[0].split("=")[1];
            System.out.println(acessToken);
            return acessToken;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 携带accessToken访问github user api得到用户信息
     * @param accessToken
     * @return
     */
    public GithubUser getGithubUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                    .url("https://api.github.com/user?access_token=" + accessToken)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String json = response.body().string();
                System.out.println(json);
                GithubUser githubUser = JSON.parseObject(json,GithubUser.class);
                return githubUser;
            } catch (IOException e) {
                e.printStackTrace();
            }
        return null;
    }
}
