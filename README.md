# File System Terminal -  Broadcom Home Assignment

To build:
```
mvn clean package
```

To run:
```
java -jar target/broadcom-home-assignment-0.0.1-SNAPSHOT.jar
```

# broadcom-hw (The Assignment instructions)

We would like to build a terminal application, which will be able to interact with the file system.

The filesystem is composed of the following entities:
* File
* Folder - which can contain multiple files, folders, and/or symbolic links.
* Symbolic link - a reference to either a file, folder or symbolic link.

We can open a terminal in a specific folder.

Terminal lets us run the following functions:
1. **listFiles** - should print the following line (file name, type, size) for each entity in the folder. (For symbolic links, you should present the size of the real file it references). 
2. **listRecursiveFiles** - should print a tree of entities in the current folder. To visualize subfolder depth, an indentation of 4 spaces should be used. Files are presented first, then sub-folders. For example:
<pre>
                                folder
                                    file
                                folder
                                    file
                                    folder
                                        file
                                        file
                                        
</pre>

3. **calculateFolderSize** - returns the size of everything contained in the current folder, including subfolders. Here, the symbolic links are counted as having no size.
4. **listFiles** and **listRecursiveFiles** could get a following filters:
   * Start with text
   * End with text 
   * Contains text 
   * Type
   * Greater than size
5. **cd**  - change the current folder (absolute)
6. **pwd** - should print the current folder
7. **createFile**  (name and size) - create a file under the current folder
8. **createFolder** (name) - create a folder under the current folder
9. **createSymbolicLink** (name, reference) - create a symbolic link under the current folder
10. Validation - entity name in specific folder is unique.
11. Write unit tests 
