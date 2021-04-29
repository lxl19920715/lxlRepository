
package org.yaukie.frame.autocode.service.impl;
import org.yaukie.core.base.service.BaseService;
import org.springframework.stereotype.Service;
 import org.yaukie.frame.autocode.dao.mapper.XJobMailMapper;
import org.yaukie.frame.autocode.model.XJobMail;
import org.yaukie.frame.autocode.model.XJobMailExample;
import org.yaukie.frame.autocode.service.api.XJobMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

        /**
        * @author: yuenbin
        * @create: 2021/04/23 11/14/935
        **/
        @Service
        @Transactional
        public class XJobMailServiceImpl extends BaseService<XJobMailMapper,XJobMail,XJobMailExample> implements XJobMailService {

        @Autowired
        private XJobMailMapper xJobMailMapper;

        }
