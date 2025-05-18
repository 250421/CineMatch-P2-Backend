package com.revature.service;

import com.revature.entity.Genre;
import jakarta.ws.rs.core.GenericType;
import org.springframework.stereotype.Service;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Service
public class TraktService {

    private String refreshToken = System.getenv("REFRESH_TOKEN");
    private String clientId = System.getenv("CLIENT_ID");
    private String clientSecret = System.getenv("CLIENT_SECRET");

//    public String getTokenFromRefreshToken() {
//        Client client = ClientBuilder.newClient();
//        Entity payload = Entity.json(
//                "{  \"refresh_token\": \"" + refreshToken + "\",  " +
//                        "\"client_id\": \"" + clientId + "\",  " +
//                        "\"client_secret\": \"" + clientSecret + "\",  " +
//                        "\"redirect_uri\": \"\",  " +
//                        "\"grant_type\": \"refresh_token\"}");
//        Response response = client.target("https://api.trakt.tv/oauth/token")
//                .request(MediaType.APPLICATION_JSON_TYPE)
//                .post(payload);
//
//        System.out.println("status: " + response.getStatus());
//        System.out.println("headers: " + response.getHeaders());
//        System.out.println("body:" + response.readEntity(String.class));
//        return null;
//    }

    public List<Genre> getGenres() {
        Client client = ClientBuilder.newClient();
        Response response = client.target("https://api.trakt.tv/genres/movies")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("trakt-api-version", "2")
                .header("trakt-api-key", clientId)
                .get();

        return response.readEntity(new GenericType<List<Genre>>(){});
    }
}
