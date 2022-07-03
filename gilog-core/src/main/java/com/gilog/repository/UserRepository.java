package com.gilog.repository;

import com.gilog.entity.Order;
import com.gilog.entity.User;
import com.gilog.vo.OrderFilter;
import com.gilog.vo.UserFilter;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.gilog.entity.QUser.user;

@Repository
public class UserRepository {
    private final EntityManager em;
    private final JPAQueryFactory query;

    public UserRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    public List<User> getUserList(UserFilter filter) {
        return query
                .selectFrom(user)
                .where(
                        search(filter.getSearchKey(), filter.getSearchValue())
                )
                .fetch();
    }

    private BooleanExpression search(String keyCond, String valueCond) {
        if (valueCond != "") {
            if (keyCond.equals("username")) return user.username.like("%" + valueCond + "%");
        }
        return null;
    }
}
