package com.gilog.repository;

import com.gilog.entity.GiLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface GiLogRepository extends JpaRepository<GiLog, Long> {
    GiLog findOneByUserIdAndWriteDate(Long userid, LocalDate date);
    List<GiLog> findByUserId(Long userid);

    @Query("SELECT g FROM GiLog g WHERE g.userId = (:userId) AND g.writeDate IN (:dateList)")
    List<GiLog> findByUserIdAndWriteDateIn(@Param("userId") Long userId, @Param("dateList") List<LocalDate> dateList);
}
