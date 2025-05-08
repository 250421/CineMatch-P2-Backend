package com.revature.service;

public class UsernamePasswordCheck {

    private String username;
    private String password;

    public UsernamePasswordCheck(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * At least 4 characters
     * No whitespace allowed
     * No special characters allowed
     * @return true if valid username, else false
     */
    public boolean isValidUsername() {
        if (this.username == null)
            return false;
        String pattern = "^[a-zA-Z0-9]{4,}$";
        return this.username.matches(pattern);
    }

    /**
     * A number must occur at least once
     * A lower-case letter must occur at least once
     * An upper-case letter must occur at least once
     * A special character must occur at least once
     * At least 8 characters
     * @return true if valid password, else false
     */
    public boolean isValidPassword() {
        if (this.password == null)
            return false;
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$";
        return this.password.matches(pattern);
    }
}
