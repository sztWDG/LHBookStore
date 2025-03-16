package com.example.service.cart;

import cn.dev33.satoken.stp.StpUtil;
import com.example.entity.dto.Order;
import com.example.entity.dto.ShoppingItem;
import com.example.entity.enums.BookStatusEnum;
import com.example.entity.enums.OrderStatus;
import com.example.entity.vo.request.cart.ShoppingAddReq;
import com.example.entity.vo.request.cart.ShoppingBuyAllReq;
import com.example.entity.vo.request.cart.ShoppingBuyReq;
import com.example.entity.vo.response.cart.ShoppingCartResp;
import com.example.exception.book.BookIsbnIsExistsException;
import com.example.repository.book.BookRepository;
import com.example.repository.order.OrderRepository;
import com.example.utils.Const;
import io.github.linpeilie.Converter;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class ShoppingCartService {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private Converter converter;
    @Resource
    private BookRepository bookRepository;
    @Resource
    private OrderRepository orderRepository;

    private String getKey() {
        return Const.SHOPPING_CART_PRE + StpUtil.getLoginIdAsLong();
    }

    private Map<String, ShoppingItem> getShoppingCart() {
        @SuppressWarnings("unchecked")
        Map<String, ShoppingItem> shoppingCart =
                (Map<String, ShoppingItem>) redisTemplate.opsForValue().get(getKey());
        if (shoppingCart == null) {
            return new HashMap<>();
        }
        return shoppingCart;
    }


    //QxkQuestion：加入购物车单价不改变
    public void addCart(ShoppingAddReq req) {
        //获取已有购物车数据
        Map<String, ShoppingItem> shoppingCart = getShoppingCart();


        //生成itemId
        if (!bookRepository.isBookExists(req.getBook().getId())) {
            throw new BookIsbnIsExistsException();
        }

        String itemId = String.valueOf(req.getBook().getId());
        //判断商品是否存在购物车
        if (shoppingCart.containsKey(itemId)) {
            //商品已经存在购物车,仅需要修改数量
            ShoppingItem oldItem = shoppingCart.get(itemId);
            oldItem.setNum(oldItem.getNum() + req.getNum());
            //要加这一句？
            oldItem.setItemPrice(oldItem.getNum() * oldItem.getBook().getPrice());
            //Bug:忘记把oldItem加入到购物车
//            shoppingCart.put(itemId, oldItem);
            //加入购物车
            redisTemplate.opsForValue().set(getKey(), shoppingCart);
        } else {
            //如果商品不存在购物车,需要新开一个item
            ShoppingItem item = converter.convert(req, ShoppingItem.class);
            item.setId(itemId);
            shoppingCart.put(itemId, item);
            redisTemplate.opsForValue().set(getKey(), shoppingCart);
        }
    }

    public ShoppingCartResp getCart() {
        Map<String, ShoppingItem> shoppingCart = getShoppingCart();

        List<ShoppingItem> items = new ArrayList<>(shoppingCart.values());

        //这边期望实现商品状态展示
        List<ShoppingItem> enListItems = items.stream()
                .filter(shoppingItem ->
                        shoppingItem.getBook().getStatus() == BookStatusEnum.En_List)
                .toList();
        // 计算总和
        double totalPrice = enListItems.stream()
                .mapToDouble(ShoppingItem::getItemPrice) // 将每个 ShoppingItem 的价格转换为 double
                .sum();
        return new ShoppingCartResp(totalPrice, enListItems);
    }


    public void updateCart(String id, Integer num) {
        if (num < 0) {
            //不能小于一，修改数据别太夸张
            throw new BookIsbnIsExistsException();
        }
        Map<String, ShoppingItem> shoppingCart = getShoppingCart();


        if (!shoppingCart.containsKey(id)) {
            throw new BookIsbnIsExistsException();
        } else {
            if (num == 0) {
                shoppingCart.remove(id);
            } else {
                ShoppingItem oldItem = shoppingCart.get(id);
                //商品已经存在购物车,仅需要修改数量
                oldItem.setNum(num);
                oldItem.setItemPrice(oldItem.getBook().getPrice() * num);
            }
            checkCart(shoppingCart);
        }
    }

    public void removeCart(String id) {
        Map<String, ShoppingItem> shoppingCart = getShoppingCart();


        shoppingCart.remove(id);
        checkCart(shoppingCart);
    }


    public void deleteAll(List<String> ids) {
        Map<String, ShoppingItem> shoppingCart = getShoppingCart();


        if (ids.size() == shoppingCart.size()) {
            // 如果删除的数量等于购物车大小，直接清空购物车
            redisTemplate.delete(getKey());
        } else {
            //ids.forEach(id -> removeCart(id, shoppingCart));
            // QxkQuestion:验证频繁
            // 批量移除商品
            ids.forEach(shoppingCart::remove);
            // 最后检查一次购物车状态
            checkCart(shoppingCart);
        }
    }

    private void checkCart(Map<String, ShoppingItem> shoppingCart) {
        if (shoppingCart.isEmpty()) {
            redisTemplate.delete(getKey());
        } else {
            // 更新购物车数据
            redisTemplate.opsForValue().set(getKey(), shoppingCart);
        }
    }

    //QxkQuestion:购物车removeCart的checkCart验证是否太频繁？
    private void removeCart(String id, Map<String, ShoppingItem> shoppingCart) {
        shoppingCart.remove(id);
        checkCart(shoppingCart);
    }

    public void buy(ShoppingBuyReq req) {
        Map<String, ShoppingItem> shoppingCart = getShoppingCart();

        if (!shoppingCart.containsKey(req.getItemId())) {
            throw new BookIsbnIsExistsException();
        }
        ShoppingItem item = shoppingCart.get(req.getItemId());

        generateOrder(item,req.getShoppingAddressId());

        removeCart(req.getItemId(), shoppingCart);
    }


    public void buyAll(@Valid ShoppingBuyAllReq req) {

        Map<String, ShoppingItem> shoppingCart = getShoppingCart();

        List<ShoppingItem> items =
                shoppingCart.values()
                        .stream()
                        .filter(it ->req.getItemIds().contains(it.getId()))
                        .toList();

        items.forEach(it -> generateOrder(it,req.getShoppingAddressId()));

        deleteAll(req.getItemIds());
    }

    private String generateOrderId() {
        // 获取当前时间戳
        long timestamp = System.currentTimeMillis();
        // 生成UUID并获取前8个字符
        String uuidPart = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        // 将时间戳转换为字符串，并获取后12位
        String timestampPart = String.valueOf(timestamp).substring(String.valueOf(timestamp).length() - 12);
        return uuidPart + timestampPart;
    }

    private void generateOrder(ShoppingItem item,long addressId) {
        long userId = StpUtil.getLoginIdAsLong();

        //构建订单
        Order order = Order.builder()
                .OrderId(generateOrderId())
                .userId(userId)
                .status(OrderStatus.ORDERED)
                .bookId(item.getBook().getId())
                .boughtNumber(item.getNum())
                .totalPrice(item.getItemPrice())
                .shippingAddressId(addressId)
                .orderTime(LocalDateTime.now())
                .createdBy(userId)
                .build();

        orderRepository.save(order);
    }

}
