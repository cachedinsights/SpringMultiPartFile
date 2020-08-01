package com.stackfortech.fileUpload.repository;

import com.stackfortech.fileUpload.model.DBFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DbFileRepository extends JpaRepository<DBFileEntity,String> {
}
