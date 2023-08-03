package com.jgitproject.jgit.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jgitproject.jgit.entity.CommitDetails;

@Repository
public interface CommitDetailsRepository extends JpaRepository<CommitDetails, String> {

}
