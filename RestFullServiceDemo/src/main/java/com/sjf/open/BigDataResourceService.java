package com.sjf.open;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.common.collect.Lists;
import com.sjf.open.model.BigDataResource;

import java.util.List;

/**
 * Created by xiaosi on 16-9-5.
 */
@Path("path")
public class BigDataResourceService {


    private static List<BigDataResource> bigDataResourceList = Lists.newArrayList();

    static {
        BigDataResource bigDataResource1 = new BigDataResource("Datameer", "machine-learning", "Datameer发布了面向企业用户的Datameer 3.0数据集成和分析软件。该版本增加了“智能分析”功能，可以从Hadoop中保存的大量复杂数据中自动找出模型和关联性");
        BigDataResource bigDataResource2 = new BigDataResource("Hortonwork", "search-engine", "Hortonworks将在社区中预览下一代支持Yarn(下一代Hadoop数据处理框架)的Hortonworks Data Platform。");
        BigDataResource bigDataResource3 = new BigDataResource("Kognitio", "data-analysis", "Kognitio推出了新一代的Kognitio Analytic Platform，加强了多种编程语言之间的连接性，并提高了性能");
        BigDataResource bigDataResource4 = new BigDataResource("Pentaho", "data-integration", "添加大数据平台集成能力");

        bigDataResourceList.add(bigDataResource1);
        bigDataResourceList.add(bigDataResource2);
        bigDataResourceList.add(bigDataResource3);
        bigDataResourceList.add(bigDataResource4);
    }

    /**
     * 查询全部
     * @return
     */
    @GET
    @Path("/big-data")
    @Produces({MediaType.APPLICATION_JSON})
    public List<BigDataResource> getAllProducts() {
        return bigDataResourceList;
    }

    /**
     * 根据type查询
     * @return
     */
    @GET
    @Path("/big-data/{type}")
    @Produces("application/json")
    public BigDataResource getAllProductsByType(@PathParam("type") final String type) {
        for(BigDataResource bigDataResource : bigDataResourceList){
            if(bigDataResource.getType().equals(type)){
                return bigDataResource;
            }
        }
        return null;
    }
}
