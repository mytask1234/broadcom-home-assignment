package com.broadcom.fs.terminal;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.function.Predicate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.broadcom.fs.model.FileImpl;
import com.broadcom.fs.model.FolderImpl;
import com.broadcom.fs.model.IFile;
import com.broadcom.fs.model.SymbolicLinkImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(Lifecycle.PER_CLASS)
public class TerminalImplTest {

	private final PrintStream standardOut = System.out;
	private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

	private ITerminal terminal;

	// see more here: https://www.baeldung.com/java-testing-system-out-println
	@BeforeEach
	void beforeEach() {

		outputStreamCaptor.reset();
		System.setOut(new PrintStream(outputStreamCaptor));
	}

	@AfterEach
	void afterEach() {

		System.setOut(standardOut);
	}

	@BeforeEach
	void initTerminal() {

		terminal = new TerminalImpl();

		terminal.createFile("file5", 5);
		terminal.createFolder("folder5");
		terminal.createFile("file4", 4);

		terminal.cd("/folder5");

		terminal.createFile("file8", 8);
		terminal.createFile("file7", 7);
		terminal.createFile("file9", 9);
		terminal.createFolder("folder4");
		terminal.createFolder("folder3");

		terminal.cd("/");
		terminal.createSymbolicLink("symfile3", "/folder5/file8");
	}

	@Test
	void testListFiles() {

		terminal.listFiles();

		StringBuilder sbExpected = new StringBuilder();

		sbExpected.append("file4 [File] 4"); sbExpected.append(System.lineSeparator());
		sbExpected.append("file5 [File] 5"); sbExpected.append(System.lineSeparator());
		sbExpected.append("symfile3 [SymbolicLink] 8"); sbExpected.append(System.lineSeparator());
		sbExpected.append("folder5 [Folder] 24"); sbExpected.append(System.lineSeparator());

		assertEquals(sbExpected.toString(), outputStreamCaptor.toString());
	}

	@Test
	void testListRecursiveFiles() {

		terminal.listRecursiveFiles();

		StringBuilder sbExpected = new StringBuilder();

		sbExpected.append("file4"); sbExpected.append(System.lineSeparator());
		sbExpected.append("file5"); sbExpected.append(System.lineSeparator());
		sbExpected.append("symfile3"); sbExpected.append(System.lineSeparator());
		sbExpected.append("folder5"); sbExpected.append(System.lineSeparator());
		sbExpected.append("    file7"); sbExpected.append(System.lineSeparator());
		sbExpected.append("    file8"); sbExpected.append(System.lineSeparator());
		sbExpected.append("    file9"); sbExpected.append(System.lineSeparator());
		sbExpected.append("    folder3"); sbExpected.append(System.lineSeparator());
		sbExpected.append("    folder4"); sbExpected.append(System.lineSeparator());

		assertEquals(sbExpected.toString(), outputStreamCaptor.toString());
	}
	
	@Test
	void testCountFolderSize() {
		
		long rootSize = terminal.countFolderSize();
		
		assertEquals(33, rootSize);
		
		//-----------------------------------------------------------
		
		terminal.cd("/folder5");
		
		long folder5Size = terminal.countFolderSize();
		
		assertEquals(24, folder5Size);
	}
	
	@Test
	void testCd() {
		
		String actualPath = terminal.getCurrFolderAbsolutePath();
		
		assertEquals("/", actualPath);
		
		//-----------------------------------------------------------
		
		terminal.cd("/folder5");
		
		actualPath = terminal.getCurrFolderAbsolutePath();
		
		assertEquals("/folder5", actualPath);
		
		//-----------------------------------------------------------
		
		terminal.cd("/folder5/folder4");
		
		actualPath = terminal.getCurrFolderAbsolutePath();
		
		assertEquals("/folder5/folder4", actualPath);
	}
	
	@Test
	void testPwd() {
		
		terminal.pwd();
		
		StringBuilder sbExpected = new StringBuilder();

		sbExpected.append("/"); sbExpected.append(System.lineSeparator());
		
		assertEquals(sbExpected.toString(), outputStreamCaptor.toString());
		
		//-----------------------------------------------------------
		
		outputStreamCaptor.reset();
		
		terminal.cd("/folder5");
		
		terminal.pwd();
		
		sbExpected = new StringBuilder();

		sbExpected.append("/folder5"); sbExpected.append(System.lineSeparator());
		
		assertEquals(sbExpected.toString(), outputStreamCaptor.toString());
	}
	
	//@Test
	void testMy() {
		
		afterEach();
		
		Predicate<Integer> noGreaterThan5 =  x -> x > 5;
		
		IFile file1 = new FileImpl("abc", 1);
		IFile file2 = new FolderImpl("abc");
		IFile file3 = new SymbolicLinkImpl("abc", null);
		
		//Predicate<IFile> fileTypePredicate =  obj -> FileImpl.class.isInstance(obj);
		//Predicate<IFile> fileTypePredicate =  obj -> FolderImpl.class.isInstance(obj);
		Predicate<IFile> fileTypePredicate =  obj -> SymbolicLinkImpl.class.isInstance(obj);
		
		if (fileTypePredicate.test(file1)) {
			
			System.out.println("file1 is instance of FileImpl");
			
		} else {
			
			System.out.println("file1 is not instance of FileImpl");
		}
		
		//-----------------------------------------------------------------
		
		if (fileTypePredicate.test(file2)) {
			
			System.out.println("file2 is instance of FileImpl");
			
		} else {
			
			System.out.println("file2 is not instance of FileImpl");
		}

		//-----------------------------------------------------------------
		
		if (fileTypePredicate.test(file3)) {
			
			System.out.println("file3 is instance of FileImpl");
			
		} else {
			
			System.out.println("file3 is not instance of FileImpl");
		}
	}
}
