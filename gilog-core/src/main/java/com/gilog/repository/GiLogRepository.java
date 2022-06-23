package com.gilog.repository;

import com.gilog.entity.GiLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface GiLogRepository extends JpaRepository<GiLog, Long> {
    GiLog findOneByUserIdAndWriteDate(Long userid, LocalDate date);


}
