package cn.jiang.jboot.controller;

import cn.jiang.jboot.common.annotation.RateLimiter;
import cn.jiang.jboot.common.lock.Callback;
import cn.jiang.jboot.common.lock.RedisDistributedLockTemplate;
import cn.jiang.jboot.common.utils.ResultUtil;
import cn.jiang.jboot.common.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author jiang
 */
@Slf4j
@RestController
@Api(description = "测试接口")
public class TestController {

    @Autowired
    private RedisDistributedLockTemplate lockTemplate;

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    @RateLimiter(limit = 1, timeout = 5000)
    @ApiOperation(value = "同步锁限流测试")
    public Result<Object> test(){

        lockTemplate.execute("订单流水号", 5000, new Callback() {
            @Override
            public Object onGetLock() throws InterruptedException {
                //TODO 获得锁后要做的事
                log.info("生成订单流水号");
                return null;
            }

            @Override
            public Object onTimeout() throws InterruptedException {
                //TODO 获得锁超时后要做的事
                return null;
            }
        });
        return new ResultUtil<Object>().setData(null);
    }

}
