package com.jgitproject.jgit.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jgitproject.jgit.entity.MapList;

public interface JGitRepository extends JpaRepository<MapList, String> {

}
