package com.broadcom.fs.model;

public class FileImpl extends AbstractIFile implements ISimpleFile {

	private final long size;
	
	public FileImpl(final String name, final long size) {
		super(name);
		this.size = size;
	}

	@Override
	public long getSize() {
		return size;
	}
}
