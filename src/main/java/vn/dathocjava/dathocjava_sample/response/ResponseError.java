package vn.dathocjava.dathocjava_sample.response;

public class ResponseError extends  ResponseData{
    private int status;
    private String message;

    public ResponseError(int status, String message) {
        super(status, message);
    }


}
