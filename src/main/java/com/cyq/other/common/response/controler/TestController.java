package com.cyq.other.common.response.controler;

import com.cyq.other.common.response.common.ResultCode;
import com.cyq.other.common.response.common.ResultCommon;
import com.cyq.other.common.response.config.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试使用控制器
 */
@ResponseResult
@RestController
@RequestMapping("/api/test")
public class TestController {

    /**
     * 演示根据用户ID获取用户信息
     * @param userId
     * @return
     */
    @GetMapping("/getUser")
    public ResultCommon getUser(@RequestParam(value = "userId", required = false) String userId) {
        if (userId == null) {
            return ResultCommon.failure(ResultCode.PARAM_IS_BLANK, null);
        }
        return ResultCommon.success(ResultCode.SUCCESS, "用户ID为：" + userId);
    }

    /**
     * 模拟通过注解封装返回数据
     * @return
     */
    @GetMapping("/getStu")
    public Object getStudent() {
        return new String("Success");
    }

}
