package com.example.service.cart;

import cn.dev33.satoken.stp.StpUtil;
import com.example.entity.dto.ShoppingItem;
import com.example.entity.enums.BookStatusEnum;
import com.example.entity.vo.request.cart.ShoppingAddReq;
import com.example.entity.vo.response.cart.ShoppingCartResp;
import com.example.exception.book.BookIsbnIsExistsException;
import com.example.repository.book.BookRepository;
import com.example.utils.Const;
import io.github.linpeilie.Converter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ShoppingCartService {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private Converter converter;
    @Resource
    private BookRepository bookRepository;

    private String getKey() {
        return Const.SHOPPING_CART_PRE + StpUtil.getLoginIdAsLong();
    }

    private Map<String, ShoppingItem> getShoppingCart() {
        @SuppressWarnings("unchecked")
        Map<String, ShoppingItem> shoppingCart =
                (Map<String, ShoppingItem>) redisTemplate.opsForValue().get(getKey());
        return shoppingCart;
    }


    public void addCart(ShoppingAddReq req) {
        //获取已有购物车数据
        Map<String, ShoppingItem> shoppingCart = getShoppingCart();
        if (shoppingCart == null) {
            shoppingCart = new HashMap<>();
        }

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
        if (shoppingCart == null) {
            return new ShoppingCartResp();
        }
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
        if (shoppingCart == null) {
            throw new BookIsbnIsExistsException();
        }

        if (!shoppingCart.containsKey(id)) {
            throw new BookIsbnIsExistsException();
        } else {
            if (num == 0 ){
                shoppingCart.remove(id);
            }else {
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
        if (shoppingCart == null) {
            throw new BookIsbnIsExistsException();
        }

        shoppingCart.remove(id);
        checkCart(shoppingCart);
    }

    private void removeCart(String id,Map<String, ShoppingItem> shoppingCart) {
        shoppingCart.remove(id);
        checkCart(shoppingCart);
    }

    public void deleteAll(List<String> ids){
        Map<String, ShoppingItem> shoppingCart = getShoppingCart();
        if (shoppingCart == null) {
            throw new BookIsbnIsExistsException();
        }

        if (ids.size() == shoppingCart.size()) {
            redisTemplate.delete(getKey());
        }else {
            ids.forEach(id -> removeCart(id, shoppingCart));
        }
    }



    private void checkCart( Map<String, ShoppingItem> shoppingCart) {

        if (shoppingCart.isEmpty()) {
            redisTemplate.delete(getKey());
        }else {
            //修改购物车数据
            redisTemplate.opsForValue().set(getKey(), shoppingCart);
        }
    }
}
