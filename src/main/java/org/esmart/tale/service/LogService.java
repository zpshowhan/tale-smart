package org.esmart.tale.service;

import java.util.List;

import org.esmart.tale.model.Tlogs;

import com.github.pagehelper.PageInfo;

public interface LogService {

	/**
     * 记录日志
     * @param log
     */
    void save(Tlogs log);

    /**
     * 读取日志
     * @param page
     * @param limit
     * @return
     */
    PageInfo<Tlogs> getLogs(int page, int limit);
}
