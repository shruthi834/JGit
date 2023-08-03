package com.jgitproject.jgit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CommitDetails {
	
	@Id
	private String commitId;
    private String author;
    private String email;
    private String date;
    private String message;
	
	

}
