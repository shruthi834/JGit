package com.jgitproject.jgit.controller;
import org.eclipse.jgit.revwalk.RevWalk;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.EmptyTreeIterator;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.AndTreeFilter;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.eclipse.jgit.treewalk.filter.TreeFilter;
import org.eclipse.jgit.util.io.DisabledOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevObject;
import org.eclipse.jgit.revwalk.RevTree;

import com.jgitproject.jgit.entity.CommitDetails;
import com.jgitproject.jgit.entity.MapList;
import com.jgitproject.jgit.entity.ModifiedList;
import com.jgitproject.jgit.service.JGitService;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;

import java.io.IOException;
@RestController
public class JGitController {
	
	@Autowired
	JGitService jgitService=new JGitService();
	
	 @Value("${file.path.name}")
	 private String filePath;
	 
	 @GetMapping("/maps")
		 public ResponseEntity<?> getMapList(){
				ResponseEntity<?> response = new ResponseEntity<>(jgitService.getDataFromMapList(), HttpStatus.OK);
					return response;
				}
		

	@GetMapping("/gitLog")
	@ResponseBody
	public List<CommitDetails> getCommitDetails( @RequestParam String className) throws IOException {
		 List<CommitDetails> commitDetailsList = new ArrayList<>();
		 String repositoryPath = "D:\\git-repo-bookstore\\book-store-project";
		 String filePathVariable =filePath+className;
		 Repository repository = Git.open(new File(repositoryPath)).getRepository();
	     try {
	    	 try (Git git = new Git(repository)) {
	                Iterable<RevCommit> logs = git.log().addPath(filePathVariable).call();
	                System.out.println("logs"+filePathVariable);
	                for (RevCommit commit : logs) {
				        CommitDetails commitDetails = new CommitDetails();
				        commitDetails.setCommitId(commit.getId().getName());
				        commitDetails.setAuthor(commit.getAuthorIdent().getName());
				        commitDetails.setEmail(commit.getAuthorIdent().getEmailAddress());
				        commitDetails.setDate(commit.getAuthorIdent().getWhen().toString());
				        commitDetails.setMessage(commit.getFullMessage());
				        System.out.println("details"+commitDetails);
				        commitDetailsList.add(commitDetails);
				       
		         }
	    	 }
		        
	     } 
	     catch (Exception e) {
	          e.printStackTrace();
	     }
		 return commitDetailsList;
	}
	
	@GetMapping("/modifiedFileList")
	@ResponseBody
	public List<ModifiedList> getModifiedFileList(@RequestParam String commitId) throws NoHeadException, GitAPIException{
		List<ModifiedList> response = new ArrayList<>();
		ModifiedList modifiedFile =new ModifiedList();
		  String repositoryPath = "D:\\git-repo-bookstore\\book-store-project";
	        String filePathVariable="";
	        try (Repository repository = Git.open(new File(repositoryPath)).getRepository()) {
	        	Git git = new Git(repository);
//	            RevCommit initialCommitId = git.log().all().setMaxCount(1).call().iterator().next();
//	            System.out.println("initial commit"+initialCommitId);
//	            ObjectId head = repository.resolve(commitId);
	            ObjectId head = ObjectId.fromString(commitId);
	            RevWalk rw = new RevWalk(repository);
	            RevCommit revCommitId = rw.parseCommit(head);
	            System.out.println("treeId"+revCommitId);
//	           
                
                DiffFormatter df = new DiffFormatter(DisabledOutputStream.INSTANCE);
	            df.setRepository(repository);
	            df.setDiffComparator(RawTextComparator.DEFAULT);
	            df.setDetectRenames(true);
	            int count = revCommitId.getParentCount();
	            if(count > 0) {
	            	RevCommit parent = rw.parseCommit(revCommitId.getParent(0).getId());
		            List<DiffEntry> diffs = df.scan(parent.getTree(), revCommitId.getTree());
		            for (DiffEntry diff : diffs) {
		            	System.out.println("diffs"+diff);
		            	filePathVariable = diff.getNewPath();
		            	modifiedFile.setClassName(filePathVariable);
		            	ObjectLoader loader = repository.open(head);
		                String fileContent = new String(loader.getBytes());
		                modifiedFile.setClassContent(fileContent);
		                response.add(modifiedFile);
		            }
	            }
	            else
	            {
	            	 TreeWalk treeWalk = new TreeWalk(repository);
	            	 RevWalk revWalk = new RevWalk(repository);
	            	 RevCommit firstCommit = revWalk.parseCommit(repository.resolve(Constants.HEAD));
	            	 System.out.println(""+firstCommit);
	            	 RevTree firstTree = firstCommit.getTree();
	            	 treeWalk.addTree(firstTree);
	            	 treeWalk.setRecursive(true);
//	            	 treeWalk.addTree(firstTree.getTree());
//	                 treeWalk.setRecursive(true);

//	            	 if(true) {
//	            		 String path = treeWalk.getPathString();
//	                     System.out.println("File Path: " + path);
//	            	 }
//	                 while (treeWalk.next()) {
//	                     String path = treeWalk.getPathString();
//	                     System.out.println("File Path: " + path);
//	                 }
//	 	            RevTree tree = revCommitId.getTree();
//	                 ObjectId treeId = tree.getId();
//	                 System.out.println("treeId"+treeId);
	            }

	        	   
	        } catch (IOException  e) {
	            e.printStackTrace();
	        }
		return response;
	}
	
	@GetMapping("/commitRestore")
	@ResponseBody
	public String getDesiredCommitVersion() throws GitAPIException {
		 String repositoryPath = "D:\\git-repo-bookstore\\book-store-project";
		 try (Repository repository = Git.open(new File(repositoryPath)).getRepository()) {
//			 try(Git git = new Git(repository)){
			 Git git = new Git(repository);
			 ObjectId currentCommitId = repository.resolve("HEAD");
			 ObjectId previousCommitId = repository.resolve("4565014be167eaf098f5dd9936f601cc699696c6");
			 String username = "shruthi.ek834";
			 String password = "rupagowda834#";
//			 previousCommitId = 4565014be167eaf098f5dd9936f601cc699696c6;
			 
			 git.checkout().setName(previousCommitId.getName()).call();
				 git.commit()
	                .setMessage("Reverting to previous commit")
	                .call();
				 CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(username, password);

	        // Push the changes to the remote branch
				 git.push().setRemote("origin").setRefSpecs(new RefSpec("HEAD:" + "main")).setCredentialsProvider(credentialsProvider).call();
//			 }
		 }
		 catch (Exception e) {
	            e.printStackTrace();
	        }
		return "Reverting to previous commit successfully ";
	}
	@GetMapping("/listsFile")
	public List<FileDetails> getFileList() {
		List<FileDetails> fileDetailsList = new ArrayList<>();
		 String repositoryPath = "D:\\git-repo-bookstore\\book-store-project";
		 try(Repository repository = Git.open(new File(repositoryPath)).getRepository())  {
			 	
	            Git git = new Git(repository);
			 	String folderPath="src/main/java/com/bittercode/model";
			 	ObjectId headId = repository.resolve("HEAD");
			 	RevWalk walk = new RevWalk(repository);
			 	RevCommit commit = walk.parseCommit(headId);
		        RevTree tree = commit.getTree();
		        System.out.println("Having tree: " + tree);
		        // now use a TreeWalk to iterate over all files in the Tree recursively
		        // you can set Filters to narrow down the results if needed
		        TreeWalk treeWalk = new TreeWalk(repository);
		        treeWalk.addTree(tree);
		        treeWalk.setRecursive(true);
		        while (treeWalk.next()) {
		        	String path = treeWalk.getPathString();
		        	if (path.startsWith(folderPath)) {
		        		FileDetails fileDetails = new FileDetails();
		        		String fileName = treeWalk.getNameString();
				        fileDetails.setUserName(commit.getAuthorIdent().getName());
				        fileDetails.setMapName(fileName.substring(0,fileName.lastIndexOf('.')));
				        fileDetails.setCreateDate(new Date(commit.getCommitTime() * 1000L));
				        fileDetailsList.add(fileDetails);
						
		            }
		        }
			 	 
                        
	}
		 catch (Exception e) {
	            e.printStackTrace();
	      }
		 return fileDetailsList;
	}
}
