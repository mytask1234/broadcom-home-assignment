package com.broadcom.fs.service;

import java.util.Scanner;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.broadcom.fs.exception.InvalidInputException;
import com.broadcom.fs.terminal.ITerminal;

@Service
public class TerminalServiceImpl implements ITerminalService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TerminalServiceImpl.class);

	private final ITerminal terminal;
	
	private final Scanner scanner;

	public TerminalServiceImpl(@Autowired ITerminal terminal) {

		this.terminal = terminal;
		this.scanner = new Scanner(System.in);
	}

	@PreDestroy
	private void preDestroy() {

		LOGGER.info("close scanner");

		scanner.close();
	}
	
	@Override
	public void startTerminal() {
		
		String inputCommand;
		
		while (true) {

			try {
				
				System.out.print(terminal.getCurrFolderAbsolutePath());
				System.out.print(" # ");

				inputCommand = scanner.nextLine();

				inputCommand = inputCommand.trim();
				
				if (inputCommand.isEmpty()) {
					
					continue;
				}
				
				String[] commandParts = inputCommand.split("\\s+");
				
				switch (commandParts[0]) {
				case "listFiles":
					terminal.listFiles();
					continue;
				case "listFilesStartWith":
					validateCorrectNumOfParams(commandParts, 1);
					terminal.listFilesStartWith(commandParts[1]);
					continue;
				case "listFilesEndWith":
					validateCorrectNumOfParams(commandParts, 1);
					terminal.listFilesEndWith(commandParts[1]);
					continue;
				case "listFilesContains":
					validateCorrectNumOfParams(commandParts, 1);
					terminal.listFilesContains(commandParts[1]);
					continue;
					
				case "listRecursiveFiles":
					terminal.listRecursiveFiles();
					continue;
				case "listRecursiveFilesStartWith":
					validateCorrectNumOfParams(commandParts, 1);
					terminal.listRecursiveFilesStartWith(commandParts[1]);
					continue;
				case "listRecursiveFilesEndWith":
					validateCorrectNumOfParams(commandParts, 1);
					terminal.listRecursiveFilesEndWith(commandParts[1]);
					continue;
				case "listRecursiveFilesContains":
					validateCorrectNumOfParams(commandParts, 1);
					terminal.listRecursiveFilesContains(commandParts[1]);
					continue;
					
				case "calculateFolderSize":
					System.out.println(terminal.countFolderSize());
					continue;
				case "cd":
					validateCorrectNumOfParams(commandParts, 1);
					terminal.cd(commandParts[1]);
					continue;
				case "pwd":
					terminal.pwd();
					continue;
				case "createFile":
					validateCorrectNumOfParams(commandParts, 2);
					long size = convertToLong(commandParts[2]);
					terminal.createFile(commandParts[1], size);
					continue;
				case "createFolder":
					validateCorrectNumOfParams(commandParts, 1);
					terminal.createFolder(commandParts[1]);
					continue;
				case "createSymbolicLink":
					validateCorrectNumOfParams(commandParts, 2);
					terminal.createSymbolicLink(commandParts[1], commandParts[2]);
					continue;
				case "exit":
					System.exit(0);
					break;
				default:
					String message = String.format("%s: command not found", commandParts[0]);
					System.out.println(message);
				}
				
			} catch (InvalidInputException e) {
				
				System.out.println(e.getMessage());
				
			} catch (Throwable t) {

				LOGGER.error(t.getMessage(), t);
				break;
			}
		}
	}
	
	private void validateCorrectNumOfParams(String[] commandParts, int expectedNumOfParams) {
		
		int numOfParams = commandParts.length - 1;
		
		if (numOfParams < expectedNumOfParams) {
			
			String message = String.format("the command '%s' should have %d parameters (not %d)", commandParts[0], expectedNumOfParams, numOfParams);

			throw new InvalidInputException(message);
		}
	}
	
	private long convertToLong(String sizeStr) {

		try {

			return Long.parseLong(sizeStr);

		} catch (NumberFormatException e) {

			String message = String.format("size parameter is not a valid number: %s", sizeStr);

			throw new InvalidInputException(message);
		}
	}
}
