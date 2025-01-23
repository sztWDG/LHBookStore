package com.example.service.user;

import cn.dev33.satoken.stp.StpUtil;
import com.example.entity.dto.ShippingAddress;
import com.example.entity.vo.request.user.AddressSaveReq;
import com.example.entity.vo.response.user.ShippingAddressResp;
import com.example.repository.user.ShippingAddressRepository;
import io.github.linpeilie.Converter;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ShippingAddressService {


    @Resource
    private ShippingAddressRepository addressRepository;

    @Resource
    private Converter converter;

    public List<ShippingAddressResp> getAllShippingAddressByUserId(long userId) {

        return addressRepository.lambdaQuery()
                .eq(ShippingAddress::getUserId, userId)
                .list()
                .stream()
                .map(item -> converter.convert(item, ShippingAddressResp.class))
                .toList();
    }

    public void createAddress(AddressSaveReq req) {

        ShippingAddress address = converter.convert(req, ShippingAddress.class);
        address.setUserId(StpUtil.getLoginIdAsLong());
        addressRepository.save(address);

    }

    public void deleteAddress(long id) {
        addressRepository.removeById(id);
    }


    public void updateAddress(long id, @Valid AddressSaveReq req) {
        ShippingAddress address =converter.convert(req, ShippingAddress.class);
        //Question：需要插入userId
        address.setId(id).setUserId(StpUtil.getLoginIdAsLong());
        addressRepository.updateById(address);
    }
}
