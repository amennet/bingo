package com.framework.common.bean.enums;

public enum ErrorCode {

    // 校验
    CHECK_TOPIC(0x900100, "topic校验不通过"),

    // 网络错误
    NETMESSAGE_ERROR(0x90001, "未知的数据来源和数据类型"),
    NET_CLIENT_ERROR(0x90006, "网络消息路由错误"),

    SESSION_INVALID(0x00040001, "登录时间已过期，请重新登录。"),
    USER_NO(0x00040002, "用户不存在。"),
    UNKONW_ERR(0x00040003, "系统错误。"),
    JUMP_ROOM(0x00040004, "玩家不在该服务器上。"),
    ROOM_NO(0x00040005, "房间不存在。"),
    SOFT_CASH_LESS(0x00040006, "筹码不足，不能进入。"),
    NOT_IN_ROOM(0x00040007, "玩家不在房间中。"),
    NOT_IN_TABLE(0x00040008, "玩家不在游戏桌中。"),
    IN_TABLE(0x00040009, "玩家已在游戏中。"),
    NO_NOTE_CARD(0x0004000A, "你没有记牌器卡或该道具已经过期。"),
    SOFT_CASH_UPLIMIT_ROOM(0x0004000B, "筹码大于了进上限，请到高倍房间参与游戏。"),
    PLAYER_IN_TABLE(0x0004000C, "游戏中，不能退出。"),
    NO_PULL_STATE(0x0004000D, "不是出牌阶段，不能托管。"),
    NOTICE_TIME_OUT(0x00040010, "您本局多次缓慢出牌，您的出牌操作时间减少");

    private int errorCode;
    private String errorMSG;

    ErrorCode(int errorCode, String errorMSG) {
        this.errorCode = errorCode;
        this.errorMSG = errorMSG;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMSG() {
        return errorMSG;
    }

    public static ErrorCode getErrorCode(int value) {
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.getErrorCode() == value) {
                return errorCode;
            }
        }
        throw new RuntimeException("没有此类型，防御性异常");
    }
}
