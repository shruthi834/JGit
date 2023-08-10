package com.jgitproject.jgit.entity;

import java.util.Date;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileDetails {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	private String userName;
	private String mapName;
	private Date createDate;
	
}
