package com.systouch.repository;

import com.systouch.domain.Published;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublishedRepository extends JpaRepository<Published, Long> {


}
