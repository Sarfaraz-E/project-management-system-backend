package com.example.ProjectManagementSystem.config;

/*
This class stores CONSTANT values related to JWT.
Constants are values that NEVER change.
*/
public class JwtConstant {

    /*
    SECRET_KEY
    ----------------
    This key is used to:
    - SIGN the JWT when creating it
    - VERIFY the JWT when validating it

    VERY IMPORTANT:
    The SAME key must be used at BOTH places
    (token generation & token validation)
    */
    public static final String SECRET_KEY =
            "asdfgksn aekehefhiefekfjeif enkdnekn ksndnkenkekekenknfjenfjr";

    /*
    JWT_HEADER
    ----------------
    This tells Spring Security:
    From which HTTP header should JWT be read?

    Standard header is:
    Authorization: Bearer <token>
    */
    public static final String JWT_HEADER = "Authorization";
}
