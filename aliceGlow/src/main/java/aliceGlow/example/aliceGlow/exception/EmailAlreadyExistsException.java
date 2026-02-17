package aliceGlow.example.aliceGlow.exception;

public class EmailAlreadyExistsException extends RuntimeException{

    public EmailAlreadyExistsException(){
        super("Email already exists");
    }
}
