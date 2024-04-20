package com.jenkins.repo;

import com.jenkins.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Itemrepo extends JpaRepository<Item,Long> {
}
