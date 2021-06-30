package com.broadcom.fs.model;

public interface IFolder extends IFile {

	IFile[] getDirectSubFiles();
	IFolder[] getDirectSubFolders();
	void createFile(IFile file);
	void createFolder(IFolder folder);
	IFile findFileByName(String name);
	IFolder findFolderByName(String name);
	IFile findEntityByName(String name);
	boolean isEntityExists(String name);
}
