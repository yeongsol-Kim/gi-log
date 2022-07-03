package com.gilog.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserFilter {

    private String searchKey;

    private String searchValue;
}
