package com.cong.wego.sse;

import cn.hutool.core.map.MapUtil;
import com.cong.wego.common.ErrorCode;
import com.cong.wego.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * SseServer业务封装类来操作SEE
 *
 * @author cong
 * @date 2024/03/05
 */
@Slf4j
public class SseServer {

    /**
     * 当前连接总数
     */
    private static final AtomicInteger CURRENT_CONNECT_TOTAL = new AtomicInteger(0);

    /**
     * messageId的 SseEmitter对象映射集
     */
    private static final Map<String, SseEmitter> SSE_EMITTER_MAP = new ConcurrentHashMap<>();

    /**
     * 创建sse连接
     *
     * @param messageId - 消息id（唯一）
     * @return {@link SseEmitter}
     */
    public static SseEmitter createConnect(String messageId) {
        //设置连接超时时间。0表示不过期，默认是30秒，超过时间未完成会抛出异常
        SseEmitter sseEmitter = new SseEmitter();
        try {
            sseEmitter.send(
                    SseEmitter.event()
                            .reconnectTime(1000L)
                    //.data("前端重连成功") // 重连成功的提示信息
            );
        } catch (IOException e) {
            log.error("前端重连异常 ==> messageId={}, 异常信息：{}", messageId, e.getMessage());
        }
        // 注册回调
        sseEmitter.onCompletion(completionCallBack(messageId));
        sseEmitter.onTimeout(timeOutCallBack(messageId));
        sseEmitter.onError(errorCallBack(messageId));
        SSE_EMITTER_MAP.put(messageId, sseEmitter);

        //记录一下连接总数。数量+1
        int count = CURRENT_CONNECT_TOTAL.incrementAndGet();
        log.info("创建sse连接成功 ==> 当前连接总数={}， messageId={}", count, messageId);
        return sseEmitter;
    }


    public static boolean containUser(String messageId) {
        return SSE_EMITTER_MAP.containsKey(messageId);
    }

    /**
     * 给指定 messageId发消息
     *
     * @param messageId - 消息id（唯一）
     * @param message   - 消息文本
     */
    public static void sendMessage(String messageId, String message) {
        if (SSE_EMITTER_MAP.containsKey(messageId)) {
            try {
                SSE_EMITTER_MAP.get(messageId).send(message);
            } catch (IOException e) {
                log.error("发送消息异常 ==> messageId={}, 异常信息：{}", messageId, e.getMessage());
            }
        } else {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"连接不存在或者超时， messageId=" + messageId);
        }
    }

    /**
     * 批量全部发送消息
     * 给所有 messageId广播发送消息
     *
     * @param message 消息
     */
    public static void batchAllSendMessage(String message) {
        SSE_EMITTER_MAP.forEach((messageId, sseEmitter) -> {
            try {
                sseEmitter.send(message, MediaType.APPLICATION_JSON);
            } catch (IOException e) {
                log.error("广播发送消息异常 ==> messageId={}, 异常信息：{}", messageId, e.getMessage());
                removeMessageId(messageId);
            }
        });
    }

    /**
     * 批量发送消息
     * 给指定 messageId集合群发消息
     *
     * @param messageIds 消息 ID
     * @param message    消息
     */
    public static void batchSendMessage(List<String> messageIds, String message) {
        if (CollectionUtils.isEmpty(messageIds)) {
            return;
        }
        // 去重
        messageIds = messageIds.stream().distinct().collect(Collectors.toList());
        messageIds.forEach(userId -> sendMessage(userId, message));
    }


    /**
     * 群发消息
     * 给指定组群发消息（即组播，我们让 messageId满足我们的组命名确定即可）
     *
     * @param groupId 组 ID
     * @param message 消息
     */
    public static void groupSendMessage(String groupId, String message) {
        if (MapUtil.isEmpty(SSE_EMITTER_MAP)) {
            return;
        }
        SSE_EMITTER_MAP.forEach((messageId, sseEmitter) -> {
            try {
                // 这里 groupId作为前缀
                if (messageId.startsWith(groupId)) {
                    sseEmitter.send(message, MediaType.APPLICATION_JSON);
                }
            } catch (IOException e) {
                log.error("组播发送消息异常 ==> groupId={}, 异常信息：{}", groupId, e.getMessage());
                removeMessageId(messageId);
            }
        });
    }

    /**
     * 删除邮件 ID
     * 移除 MessageId
     *
     * @param messageId 消息 ID
     */
    public static void removeMessageId(String messageId) {
        SSE_EMITTER_MAP.remove(messageId);
        //数量-1
        CURRENT_CONNECT_TOTAL.getAndDecrement();
        log.info("remove messageId={}", messageId);
    }

    /**
     * 获取消息 ID
     * 获取所有的 MessageId集合
     *
     * @return {@link List}<{@link String}>
     */
    public static List<String> getMessageIds() {
        return new ArrayList<>(SSE_EMITTER_MAP.keySet());
    }

    /**
     * 获取当前连接总数
     *
     * @return int
     */
    public static int getConnectTotal() {
        return CURRENT_CONNECT_TOTAL.intValue();
    }

    /**
     * 完成回拨
     * 断开SSE连接时的回调
     *
     * @param messageId 消息 ID
     * @return {@link Runnable}
     */
    private static Runnable completionCallBack(String messageId) {
        return () -> {
            log.info("结束连接 ==> messageId={}", messageId);
            removeMessageId(messageId);
        };
    }

    /**
     * 超时回拨
     * 连接超时时回调触发
     *
     * @param messageId 消息 ID
     * @return {@link Runnable}
     */
    private static Runnable timeOutCallBack(String messageId) {
        return () -> {
            log.info("连接超时 ==> messageId={}", messageId);
            removeMessageId(messageId);
        };
    }

    /**
     * 错误回调
     * 连接报错时回调触发。
     *
     * @param messageId 消息 ID
     * @return {@link Consumer}<{@link Throwable}>
     */
    private static Consumer<Throwable> errorCallBack(String messageId) {
        return throwable -> {
            log.error("连接异常 ==> messageId={}", messageId);
            removeMessageId(messageId);
        };
    }
}
