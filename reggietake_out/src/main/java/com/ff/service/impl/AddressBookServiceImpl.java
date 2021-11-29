package com.ff.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ff.dao.AddressBookDao;
import com.ff.entity.AddressBook;
import com.ff.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @author FF
 * @date 2021/11/28
 * @TIME:9:12
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookDao, AddressBook> implements AddressBookService {
}
