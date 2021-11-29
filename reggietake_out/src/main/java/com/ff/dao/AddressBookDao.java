package com.ff.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ff.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author FF
 * @date 2021/11/28
 * @TIME:9:11
 */
@Mapper
public interface AddressBookDao extends BaseMapper<AddressBook> {
}
