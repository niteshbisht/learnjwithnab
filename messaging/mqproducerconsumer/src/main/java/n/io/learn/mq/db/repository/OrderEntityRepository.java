package n.io.learn.mq.db.repository;

import n.io.learn.mq.db.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderEntityRepository extends JpaRepository<OrderEntity, Long> {
    boolean existsByOrderId(String orderId);
}
