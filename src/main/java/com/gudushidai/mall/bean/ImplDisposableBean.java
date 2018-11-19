package com.gudushidai.mall.bean;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.LogManager;

@Component
public class ImplDisposableBean implements DisposableBean, ExitCodeGenerator {

    @Override
    public void destroy() throws Exception {
        LogManager.shutdown();
    }

    @Override
    public int getExitCode() {
        return 0;
    }
}