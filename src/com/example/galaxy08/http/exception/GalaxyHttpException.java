package com.example.galaxy08.http.exception;


public class GalaxyHttpException extends Exception{

    /**
     * 
     */
    private static final long serialVersionUID = 7145276808700063584L;

    private String mExtra;
    
    public GalaxyHttpException(String message) {
        super(message);
    }

    public GalaxyHttpException(String message, String extra) {
        super(message);
        mExtra = extra;
    }
    
    public String getExtra() {
        return mExtra;
    }
}
