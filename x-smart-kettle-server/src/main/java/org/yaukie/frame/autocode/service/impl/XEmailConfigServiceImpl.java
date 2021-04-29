
package org.yaukie.frame.autocode.service.impl;
import org.yaukie.core.base.service.BaseService;
import org.springframework.stereotype.Service;
 import org.yaukie.frame.autocode.dao.mapper.XEmailConfigMapper;
import org.yaukie.frame.autocode.model.XEmailConfig;
import org.yaukie.frame.autocode.model.XEmailConfigExample;
import org.yaukie.frame.autocode.service.api.XEmailConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

        /**
        * @author: yuenbin
        * @create: 2021/04/23 09/51/39
        **/
        @Service
        @Transactional
        public class XEmailConfigServiceImpl extends BaseService<XEmailConfigMapper,XEmailConfig,XEmailConfigExample> implements XEmailConfigService {

        @Autowired
        private XEmailConfigMapper xEmailConfigMapper;

        }
