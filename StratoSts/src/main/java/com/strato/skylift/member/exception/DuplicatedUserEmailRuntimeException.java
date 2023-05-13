package com.strato.skylift.member.exception;

public class DuplicatedUserEmailRuntimeException extends RuntimeException {

	public DuplicatedUserEmailRuntimeException(String msg) {
		super(msg);
	}
}