package n.io.learn.mq.service;

import lombok.extern.slf4j.Slf4j;
import n.io.learn.mq.db.entities.OrderEntity;
import n.io.learn.mq.db.repository.OrderEntityRepository;
import n.io.learn.mq.pojo.Order;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class OrderCreationService {
    @Autowired
    private OrderEntityRepository orderEntityRepository;

    @Transactional(rollbackFor = Exception.class)
    public void createOrder(@NonNull Order order) {
        if(orderEntityRepository.existsByOrderId(order.getOrderId())) {
            log.warn("Order with orderId={} already exists", order.getOrderId());
            return;
        }
        OrderEntity orderEntity = new OrderEntity();
        BeanUtils.copyProperties(order, orderEntity);
        OrderEntity savedOrder = orderEntityRepository.save(orderEntity);
        log.info("Saved order by rowId={} and orderId={}", savedOrder.getId(), savedOrder.getOrderId());
    }
}
