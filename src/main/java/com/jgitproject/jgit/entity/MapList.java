package com.jgitproject.jgit.entity;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MapList {
	@Id
	private String memeberId;
	private String pledgeType;
	private String className;
	

}
