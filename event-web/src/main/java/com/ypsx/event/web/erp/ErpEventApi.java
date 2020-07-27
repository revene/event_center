package com.ypsx.event.web.erp;

import com.ypsx.event.cache.EventTypeCache;
import com.ypsx.event.error.ExceptionConstant;
import com.ypsx.event.model.Event;
import com.ypsx.event.sevice.EventPublishService;
import com.ypsx.event.web.request.ErpSynchronizedEventRequest;
import com.ypsx.event.web.request.SynchronizedItem;
import com.ypsx.util.model.BatchResult;
import com.ypsx.util.model.ExceptionInfo;
import com.ypsx.util.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.ypsx.event.util.ParamUtil.isEmpty;

/**
 * @author chuchengyi
 */
@Api(description = "ERP数据同步通知接口", tags = "ERP数据同步")
@RestController
@RequestMapping(value = "erp")
public class ErpEventApi {

    @Resource
    private EventPublishService eventPublishService;

    /**
     * 功能：数据类型的缓存信息
     */
    private EventTypeCache eventTypeCache = EventTypeCache.getInstance();
    /**
     * 功能：日志信息打印
     */
    private final static Logger logger = LoggerFactory.getLogger("eventLog");

    @ApiOperation("ERP数据同步通知")
    @RequestMapping(value = "submitEvent", method = RequestMethod.POST)
    public BatchResult<String> submitEvent(@RequestBody ErpSynchronizedEventRequest eventVO) {
        BatchResult<String> batchResult = new BatchResult<String>();
        Result<String> result = null;
        try {
            //校验输入参数
            ExceptionInfo exceptionCode = checkParam(eventVO);
            if (exceptionCode != ExceptionConstant.OK) {
                batchResult.setSuccess(false);
                batchResult.setException(exceptionCode);
                return batchResult;
            }
            //解析整个事件的信息
            List<Event> eventList = parseEvent(eventVO);
            //挨个处理相关的事件信息
            for (Event event : eventList) {
                //发布事件信息
                result = eventPublishService.publishEvent(event);
                if (result.isSuccess()) {
                    batchResult.addSuccess(event.getBizId());
                } else {
                    //构造错误信息
                    ExceptionInfo exception = new ExceptionInfo();
                    exception.setErrorCode(result.getErrorCode());
                    exception.setErrorMessage(result.getErrorMessage());
                    //添加失败的数据
                    batchResult.addFail(event.getBizId());
                    //添加失败的原因
                    batchResult.addException(event.getBizId(), exception);
                }
            }
            batchResult.setSuccess(true);
        } catch (Throwable throwable) {
            batchResult.setSuccess(false);
            batchResult.setErrorMessage(throwable.getMessage());
            logger.error("EventApi[add] is error :" + throwable.getMessage());
        }
        return batchResult;
    }


    /**
     * 功能：检测输入的参数信息
     *
     * @param eventVO
     * @return
     */
    private ExceptionInfo checkParam(ErpSynchronizedEventRequest eventVO) {
        //判断应用标识是否为空
        if (isEmpty(eventVO.getEventType())) {
            return ExceptionConstant.EVENT_TYPE_IS_NULL;
        }
        if (isEmpty(eventVO.getBizList())) {
            return ExceptionConstant.BIZ_LIST_IS_NULL;
        }
        return ExceptionConstant.OK;
    }

    /**
     * 功能：把对象解析成事件
     *
     * @param eventVO
     * @return
     */
    private List<Event> parseEvent(ErpSynchronizedEventRequest eventVO) {
        List<Event> eventList = new ArrayList<>();
        String eventType = eventVO.getEventType();
        List<SynchronizedItem> bizList = eventVO.getBizList();
        for (SynchronizedItem synItem : bizList) {
            Event event = new Event();
            event.setEventType(eventType);
            event.setBizId(synItem.getId());
            event.setVersion(synItem.getVersion());
            event.setAppCode("test");
            eventList.add(event);
        }
        return eventList;
    }

}
