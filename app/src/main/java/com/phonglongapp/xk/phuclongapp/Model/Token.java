package com.phonglongapp.xk.phuclongapp.Model;

public class Token {
    private String token;
    private boolean tokenServer;

    public Token(){

    }

    public Token(String token, boolean tokenServer) {
        this.token = token;
        this.tokenServer = tokenServer;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isTokenServer() {
        return tokenServer;
    }

    public void setTokenServer(boolean tokenServer) {
        this.tokenServer = tokenServer;
    }
}
