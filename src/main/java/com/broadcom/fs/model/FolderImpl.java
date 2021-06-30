package com.broadcom.fs.model;

import java.util.Map;
import java.util.TreeMap;

public class FolderImpl extends AbstractIFile implements IFolder {

	private final Map<String, IFile> directSubFilesMap = new TreeMap<>(); // map file name to IFile 
	private final Map<String, IFolder> directSubFoldersMap = new TreeMap<>(); // map file name to IFolder

	public FolderImpl(final String name) {
		super(name);
	}

	@Override
	public IFile[] getDirectSubFiles() {
		return directSubFilesMap.values().toArray(new IFile[directSubFilesMap.size()]);
	}

	@Override
	public IFolder[] getDirectSubFolders() {
		return directSubFoldersMap.values().toArray(new IFolder[directSubFoldersMap.size()]);
	}

	@Override
	public void createFile(IFile file) {
		directSubFilesMap.put(file.getName(), file);
	}

	@Override
	public void createFolder(IFolder folder) {
		directSubFoldersMap.put(folder.getName(), folder);
	}

	@Override
	public IFile findFileByName(String name) {
		return directSubFilesMap.get(name);
	}

	@Override
	public IFolder findFolderByName(String name) {
		return directSubFoldersMap.get(name);
	}
	
	@Override
	public IFile findEntityByName(String name) {
		
		IFile entity = directSubFilesMap.get(name);
		
		if (entity != null) {
			
			return entity;
		}
		
		return directSubFoldersMap.get(name);
	}
	
	@Override
	public boolean isEntityExists(String name) {
		
		if (directSubFilesMap.containsKey(name) || directSubFoldersMap.containsKey(name)) {
			
			return true;
		}
		
		return false;
	}
}
