package com.broadcom.fs.model;

public class SymbolicLinkImpl extends AbstractIFile implements ISymbolicLink {

	private final IFile reference;
	
	public SymbolicLinkImpl(final String name, final IFile reference) {
		super(name);
		this.reference = reference;
	}

	@Override
	public IFile getReference() {
		return reference;
	}
}
