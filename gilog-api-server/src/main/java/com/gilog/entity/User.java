//package com.gilog.entity;
//
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.springframework.data.domain.Auditable;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//
//import javax.persistence.Entity;
//import javax.persistence.EntityListeners;
//import javax.persistence.Table;
//import java.time.temporal.TemporalAccessor;
//import java.util.Optional;
//
//@Entity
//@Getter
//@NoArgsConstructor
//@EntityListeners(AuditingEntityListener.class)
//
//@Table(name = "user")
//public class User implements Auditable {
//
//
//
//    @Override
//    public Optional getCreatedBy() {
//        return Optional.empty();
//    }
//
//    @Override
//    public void setCreatedBy(Object createdBy) {
//
//    }
//
//    @Override
//    public Optional getCreatedDate() {
//        return Optional.empty();
//    }
//
//    @Override
//    public void setCreatedDate(TemporalAccessor creationDate) {
//
//    }
//
//    @Override
//    public Optional getLastModifiedBy() {
//        return Optional.empty();
//    }
//
//    @Override
//    public void setLastModifiedBy(Object lastModifiedBy) {
//
//    }
//
//    @Override
//    public Optional getLastModifiedDate() {
//        return Optional.empty();
//    }
//
//    @Override
//    public void setLastModifiedDate(TemporalAccessor lastModifiedDate) {
//
//    }
//
//    @Override
//    public Object getId() {
//        return null;
//    }
//
//    @Override
//    public boolean isNew() {
//        return false;
//    }
//}
