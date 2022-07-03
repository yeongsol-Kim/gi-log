package com.gilog.repository;

import com.gilog.dto.OrderDto;
import com.gilog.entity.Order;
import com.gilog.vo.OrderFilter;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.hibernate.metamodel.model.convert.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

import static com.gilog.entity.QOrder.order;

@Repository
public class OrderRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public OrderRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    public List<Order> getOrderList(OrderFilter filter) {
        return query
                .selectFrom(order)
                .where(
                        search(filter.getSearchKey(), filter.getSearchValue()),
                        stateEq(filter.getPayState()),
                        productEq(filter.getProduct()),
                        dateBf(filter.getBeforeDate()),
                        dateAf(filter.getAfterDate())
                )
                .fetch();
    }

    private BooleanExpression stateEq(Integer payStateCond) {
        if (payStateCond.equals(1)) return order.payState.eq(1);
        else if (payStateCond.equals(2)) return order.payState.eq(2);
        else if (payStateCond.equals(3)) return order.payState.eq(3);
        else if (payStateCond.equals(4)) return order.payState.eq(4);
        else if (payStateCond.equals(5)) return order.payState.eq(5);
        else if(payStateCond.equals(6)) return order.payState.goe(2);
        else return null;

    }

    private BooleanExpression search(String keyCond, String valueCond) {
        if (valueCond != "") {
            if (keyCond.equals("id")) return order.id.like("%" + valueCond + "%");
            else if (keyCond.equals("nickname")) return order.nickname.like("%" + valueCond + "%");
            else if (keyCond.equals("waybillNumber")) return order.waybillNumber.like("%" + valueCond + "%");
        }
        return null;
    }

    private BooleanExpression productEq(String productCond) {
        return productCond != null ? order.product.eq(productCond) : null;
    }

    private BooleanExpression dateBf(LocalDate beforeDateCond) {
        return beforeDateCond != null ? order.orderDate.before(beforeDateCond) : null;
    }

    private BooleanExpression dateAf(LocalDate afterDateCond) {
        return afterDateCond != null ? order.orderDate.after(afterDateCond) : null;
    }
}
