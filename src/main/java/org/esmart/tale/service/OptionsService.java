package org.esmart.tale.service;

import java.util.List;

import org.esmart.tale.model.Toptions;

public interface OptionsService {

	/**
     * 保存一组配置
     *
     * @param options
     */
    void saveOptions(List<Toptions> options);
    
    /**
     * 保存配置
     * @param key
     * @param value
     */
    void saveOption(Toptions option);
    
    /**
     * 获取系统配置
     * @return
     */
    List<Toptions>  getOptions();
    
    /**
     * 根据key删除配置项
     * @param key
     */
    void deleteOption(String key);
    
    Toptions getOptionsOne(String key);
}
