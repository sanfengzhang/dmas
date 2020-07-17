package com.hanl.dams.common.config;

import java.util.List;

/**
 * @author: Hanl
 * @date :2020/5/22
 * @desc:
 */
public interface ConfigManager {

    public static final String DAMS_CONFIG_PATH = "dams.config.path";

    /**
     * 持久化Config配置，如果存在配置的话，直接覆盖
     *
     * @param path
     * @param config
     */
    public void persistConfig(String path, Config config);


    /**
     * 更新某个配置中的只
     *
     * @param config
     * @param key
     * @param value
     */
    public void put(Config config, String key, Object value);





    /**
     * 获取所有的配置
     * @return
     */
    public List<Config> getConfigs();
}
