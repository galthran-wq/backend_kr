package com.resitplatform.infrastructure.db.jpa;

import com.resitplatform.domain.model.Resit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DataResitRepository extends CrudRepository<Resit, UUID> {

    Optional<Resit> findBySlug(String slug);

    Optional<Resit> findByName(String name);

//    @Query("SELECT DISTINCT resit_ FROM Resit resit_ " +
//            "WHERE " +
//            "resit_.name = :name AND " +
//            "resit_.teacherName = :teacherName")
//    List<Resit> findByFilters(@Param("name") String name,
//                              @Param("teacherName") String TeacherName,
//                              Pageable pageable);

    @Query("SELECT resit_ FROM Resit resit_ " +
            "JOIN resit_.participants participant " +
            "WHERE participant.id IN :participants")
    List<Resit> findByParticipants(List<UUID> participants, Pageable pageable);

}
