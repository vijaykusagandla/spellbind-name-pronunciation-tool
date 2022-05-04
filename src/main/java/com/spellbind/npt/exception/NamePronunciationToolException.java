package com.spellbind.npt.exception;

public class NamePronunciationToolException extends RuntimeException {

	private static final long serialVersionUID = 7455097697384280129L;

	public NamePronunciationToolException(String mesage) {
		super(mesage);
	}

	public NamePronunciationToolException(String mesage, Throwable t) {
		super(mesage, t);
	}

}
