package com.strato.skylift.common.paging;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDtoWithPaging
{

    private Object data;
    private PagingButtonInfo pageInfo;

}