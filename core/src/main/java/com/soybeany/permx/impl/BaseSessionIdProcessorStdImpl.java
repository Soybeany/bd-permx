package com.soybeany.permx.impl;

import com.soybeany.permx.api.ISessionIdProcessor;
import com.soybeany.util.file.BdFileUtils;

/**
 * @author Soybeany
 * @date 2022/3/29
 */
public abstract class BaseSessionIdProcessorStdImpl<Input> implements ISessionIdProcessor<Input> {

    protected final String sessionIdKey = onSetupSessionIdKey();

    @Override
    public String getNewSessionId(Input input) {
        return BdFileUtils.getUuid();
    }

    /**
     * 配置放置sessionId的key
     */
    protected String onSetupSessionIdKey() {
        return "session_id";
    }

}
