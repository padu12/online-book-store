package com.kaziamyr.onlinebookstore.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ShippingAddressDto {
    private String shippingAddress;
}
