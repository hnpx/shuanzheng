package com.px.pa.api;

import com.px.basic.alone.core.base.BaseApiController;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
@Api(value = "api/task", tags = "积分任务相关的服务接口")
public class TaskApiController extends BaseApiController {


}
