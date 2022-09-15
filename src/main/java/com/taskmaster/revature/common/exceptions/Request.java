package com.taskmaster.revature.common.exceptions;

public interface Request<T> {
    T extractEntity();
}
