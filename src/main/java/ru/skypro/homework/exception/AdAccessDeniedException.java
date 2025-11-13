package ru.skypro.homework.exception;

public class AdAccessDeniedException extends RuntimeException {
    public AdAccessDeniedException(String message) {
        super(message);
    }
}
