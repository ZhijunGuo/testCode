package com.example.galaxy08.http.model;

/**
 * 
 */
@SuppressWarnings("serial")
public  class GalaxyResponse extends BaseResponse{
	
    private ResponseDataBean data;

    public GalaxyResponse(){
        
    }
    
    public ResponseDataBean getData() {
        return data;
    }

    public void setData(ResponseDataBean data) {
        this.data = data;
    }
}
