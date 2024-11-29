package com.doan.AppTuyenDung.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doan.AppTuyenDung.entity.Note;
@Repository
public interface NoteReponsitory extends JpaRepository<Note,Integer> {
    Page<Note> findAllByPostId(Integer postId, Pageable pageable);   
    
}
