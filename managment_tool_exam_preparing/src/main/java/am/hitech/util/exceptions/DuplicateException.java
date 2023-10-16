package am.hitech.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateException extends Exception{

    public DuplicateException(){}
    public DuplicateException(String message){
        super(message);
    }
}
