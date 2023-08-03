package com.jgitproject.jgit.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ModifiedList {
	private String className;
	private String classContent;
	private String author;
	private String message;
	private Date date;

}
