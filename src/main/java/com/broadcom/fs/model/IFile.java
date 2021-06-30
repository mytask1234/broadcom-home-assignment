package com.broadcom.fs.model;

public interface IFile {

	String getName();
	default long getSize() {
		return 0;
	}
}
