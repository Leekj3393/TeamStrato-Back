package com.strato.skylift.common.paging;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDtoWithPaging
{

    private Object data;
    private PagingButtonInfo pageInfo;

}