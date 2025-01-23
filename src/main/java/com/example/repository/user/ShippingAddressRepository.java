package com.example.repository.user;

import com.baomidou.mybatisplus.extension.repository.CrudRepository;
import com.example.entity.dto.ShippingAddress;
import com.example.mapper.user.ShippingAddressMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ShippingAddressRepository extends CrudRepository<ShippingAddressMapper,ShippingAddress> {

}
