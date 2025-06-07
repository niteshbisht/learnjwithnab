package n.io.learn.mq.pojo;

import lombok.Data;

@Data
public class Order {
    private String orderId;
    private int quantity;
    private double quotedPrice;
    private String customerName;
}
