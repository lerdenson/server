package exceptions;

/**
 * Вызывается когда ID солдата не уникален
 */
public class CommandException extends Exception{
    public CommandException(String message) {
        super(message);
    }
}
