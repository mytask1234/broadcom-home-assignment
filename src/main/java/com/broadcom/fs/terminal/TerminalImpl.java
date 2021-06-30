package com.broadcom.fs.terminal;

import java.util.function.Predicate;

import org.springframework.stereotype.Component;

import com.broadcom.fs.exception.InvalidInputException;
import com.broadcom.fs.model.FileImpl;
import com.broadcom.fs.model.FolderImpl;
import com.broadcom.fs.model.IFile;
import com.broadcom.fs.model.IFolder;
import com.broadcom.fs.model.ISimpleFile;
import com.broadcom.fs.model.ISymbolicLink;
import com.broadcom.fs.model.SymbolicLinkImpl;

@Component
public class TerminalImpl implements ITerminal {

	private static final String FILE_SEPARATOR = "/";
	private static final String INDENTATION = "    ";
	
	private final IFolder rootFolder;
	
	private String currFolderAbsolutePath;
	private IFolder currFolder;
	
	private final Predicate<String> allFilesPredicate = name -> true;
	
	public TerminalImpl() {
		
		rootFolder = new FolderImpl(FILE_SEPARATOR);
		
		currFolder = rootFolder;
		currFolderAbsolutePath = currFolder.getName();
	}
	
	/**
	 * listFiles - should print the following line (file name, type, size) for each entity in the folder. 
	 * (For symbolic links, you should present the size of the real file it references).
	 */
	@Override
	public void listFiles() {

		listFiles(currFolder, allFilesPredicate);
	}
	
	private void listFiles(IFolder folder, Predicate<String> namePredicate) {
		
		IFile[] files = folder.getDirectSubFiles();
		
		for (IFile file : files) {
			
			if (namePredicate.test(file.getName())) {
				
				System.out.print(file.getName());
				
				if (file instanceof ISimpleFile) {
					
					System.out.print(" [File] ");
					
				} else { // // if file instanceof ISymbolicLink
					
					System.out.print(" [SymbolicLink] ");
				}
				
				long size = countEntitySize(file);
				
				System.out.println(size);
			}
		}
		
		IFolder[] subFolders = folder.getDirectSubFolders();
		
		for (IFolder subFolder : subFolders) {
			
			if (namePredicate.test(subFolder.getName())) {
				
				System.out.print(subFolder.getName());
				
				System.out.print(" [Folder] ");
				
				long size = countEntitySize(subFolder);
				
				System.out.println(size);
			}
		}
	}
	
	private long countEntitySize(IFile entity) {
		
		if(entity instanceof IFolder) {
			
			return countFolderSize((IFolder)entity);
			
		} else if (entity instanceof ISimpleFile) {
			
			return entity.getSize();
			
		} else { // if entity instanceof ISymbolicLink
			
			ISymbolicLink symbolicLink = (ISymbolicLink)entity;
			
			IFile reference = symbolicLink.getReference();
			
			while (reference instanceof ISymbolicLink) {
				
				reference = ((ISymbolicLink)reference).getReference();
			}
			
			return countEntitySize(reference);
		}
	}

	@Override
	public void listRecursiveFiles() {
		
		listRecursiveFiles(currFolder, "", allFilesPredicate);
	}
	
	private void listRecursiveFiles(IFolder folder, String indentation, Predicate<String> namePredicate) {

		IFile[] files = folder.getDirectSubFiles();
		
		for (IFile file : files) {
			
			if (namePredicate.test(file.getName())) {
				
				System.out.print(indentation);
				System.out.println(file.getName());
			}
		}
		
		IFolder[] subFolders = folder.getDirectSubFolders();
		
		for (IFolder subFolder : subFolders) {
			
			if (namePredicate.test(subFolder.getName())) {
				
				System.out.print(indentation);
				System.out.println(subFolder.getName());
			}
			
			listRecursiveFiles(subFolder, indentation + INDENTATION, namePredicate);
		}
	}

	@Override
	public long countFolderSize() {
		
		return countFolderSize(currFolder);
	}
	
	public long countFolderSize(IFolder folder) {
		
		TotalSize totalSize = new TotalSize();
		
		countFolderSize(folder, totalSize);
		
		return totalSize.getTotalSize();
	}
	
	private void countFolderSize(IFolder folder, TotalSize totalSize) {

		IFile[] files = folder.getDirectSubFiles();
		
		for (IFile file : files) {
			
			totalSize.add(file.getSize());
		}
		
		IFolder[] subFolders = folder.getDirectSubFolders();
		
		for (IFolder subFolder : subFolders) {
			
			countFolderSize(subFolder, totalSize);
		}
	}
	
	private static class TotalSize {
		
		private long totalSize = 0;
		
		private void add(long size) {
			
			totalSize += size;
		}

		private long getTotalSize() {
			return totalSize;
		}
	}

	@Override
	public void listFilesStartWith(String filter) {

		Predicate<String> startWithNamesPredicate = name -> name.startsWith(filter);
		
		listFiles(currFolder, startWithNamesPredicate);
	}

	@Override
	public void listFilesEndWith(String filter) {
		
		Predicate<String> endWithNamesPredicate = name -> name.endsWith(filter);
		
		listFiles(currFolder, endWithNamesPredicate);
	}

	@Override
	public void listFilesContains(String filter) {
		
		Predicate<String> containsNamesPredicate = name -> name.contains(filter);
		
		listFiles(currFolder, containsNamesPredicate);
	}

	@Override
	public void listFilesByType(String filter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void listFilesGreaterThanSize(String filter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void listRecursiveFilesStartWith(String filter) {
		
		Predicate<String> startWithNamesPredicate = name -> name.startsWith(filter);
		
		listRecursiveFiles(currFolder, "", startWithNamesPredicate);
	}

	@Override
	public void listRecursiveFilesEndWith(String filter) {
		
		Predicate<String> endWithNamesPredicate = name -> name.endsWith(filter);
		
		listRecursiveFiles(currFolder, "", endWithNamesPredicate);
	}

	@Override
	public void listRecursiveFilesContains(String filter) {
		
		Predicate<String> containsNamesPredicate = name -> name.contains(filter);
		
		listRecursiveFiles(currFolder, "", containsNamesPredicate);
	}

	@Override
	public void listRecursiveFilesByType(String filter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void listRecursiveFilesGreaterThanSize(String filter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cd(String absolute) {
		
		IFile folder = findEntityByAbsolutePath(absolute, true);
		
		currFolder = (IFolder)folder;
		currFolderAbsolutePath = absolute;
	}

	@Override
	public void pwd() {
		
		System.out.println(currFolderAbsolutePath);
	}

	@Override
	public IFile createFile(String name, long size) {
		
		validateEntityNotExists(name);
		
		IFile file = new FileImpl(name, size);
		
		currFolder.createFile(file);
		
		return file;
	}

	@Override
	public IFile createFolder(String folderName) {
		
		validateEntityNotExists(folderName);
		
		IFolder folder = new FolderImpl(folderName);
		
		currFolder.createFolder(folder);
		
		return folder;
	}

	@Override
	public IFile createSymbolicLink(String name, String absoluteReference) {
		
		validateEntityNotExists(name);
		
		IFile reference;
		
		try {
			
			reference = findEntityByAbsolutePath(absoluteReference, false);
			
		} catch (InvalidInputException e) {

			String message = String.format("invalid reference:  %s", e.getMessage());
			
			throw new InvalidInputException(message);
		}
		
		IFile file = new SymbolicLinkImpl(name, reference);
		
		currFolder.createFile(file);
		
		return file;
	}
	
	@Override
	public String getCurrFolderAbsolutePath() {
		return currFolderAbsolutePath;
	}

	private void validateEntityNotExists(String entityName) {
		
		if (currFolder.isEntityExists(entityName)) {
			
			String message = String.format("entity with name '%s' already exists in the current folder", entityName);
			
			throw new InvalidInputException(message);
		}
	}

	private IFile findEntityByAbsolutePath(final String absolutePath, final boolean expectedFolderOnly) {
		
		if (absolutePath.equals(FILE_SEPARATOR)) {
			
			return rootFolder;
		}
		
		if (!absolutePath.startsWith(FILE_SEPARATOR)) {
			
			String message = String.format("absolute path should start with '/' ; invalid absolute path: %s", absolutePath);
			
			throw new InvalidInputException(message);
		}
		
		IFolder theFolder = rootFolder;
		
		String[] folders = absolutePath.split(FILE_SEPARATOR);
		
		int i = 1;
		
		for ( ; i < folders.length-1 ; i++) {
			
			String folder = folders[i];
			theFolder = theFolder.findFolderByName(folder);
			
			if (theFolder == null) {
				
				String message = String.format("folder '%s' not exists in absolute path: %s", folder, absolutePath);
				
				throw new InvalidInputException(message);
			}
		}
		
		// last part entity in absolute path
		String lastEntityName = folders[i];
		IFile lastEntity = theFolder.findEntityByName(lastEntityName);
		
		if (lastEntity == null) {
			
			String message = String.format("last entity '%s' not exists in absolute path: %s", lastEntityName, absolutePath);
			
			throw new InvalidInputException(message);
		}
		
		if (expectedFolderOnly && (lastEntity instanceof IFolder) == false) {
			
			String message = String.format("last folder '%s' not exists in absolute path: %s", lastEntityName, absolutePath);
			
			throw new InvalidInputException(message);
		}
		
		return lastEntity;
	}
}
