package com.broadcom.fs.model;

public abstract class AbstractIFile implements IFile {

	private final String name;

	public AbstractIFile(final String name) {
		super();
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
