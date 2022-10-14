package hr.kraljic.web.module.core.authentication;

public enum TokenType {
    BEARER("Bearer");

    private String tokenType;

    TokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getTokenType() {
        return tokenType;
    }

    @Override
    public String toString() {
        return tokenType;
    }
}