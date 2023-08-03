package com.jgitproject.jgit.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jgitproject.jgit.entity.CommitDetails;
import com.jgitproject.jgit.entity.MapList;
import com.jgitproject.jgit.repository.CommitDetailsRepository;
import com.jgitproject.jgit.repository.JGitRepository;

@Service
public class JGitService {
	
	@Autowired
    private JGitRepository jGitRepository;
	private CommitDetailsRepository commitDetailsRepository;
	
	public List<MapList> getDataFromMapList() {
        // Call the repository to fetch data from the database
        return jGitRepository.findAll(); 
    }
	
	public List<CommitDetails> getCommitDetailsForMap(String classname){
		
		return commitDetailsRepository.findAll(); 
		
	}

}
