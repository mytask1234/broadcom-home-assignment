package com.broadcom.fs.model;

public interface ISymbolicLink extends IFile {

	IFile getReference();
}
