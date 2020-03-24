package com.hanl.dams.entity.pdm;

import lombok.Data;

/**
 * @author: Hanl
 * @date :2020/3/17
 * @desc:
 */
@Data
public class PDMStoreEntity {

    private String storageType;//存储类型

    private String clusterName;//集群名称，可以为空

    private String url;//存储库的访问路径,比如JDBC,ES:http://127.0.0.1:9200

}
