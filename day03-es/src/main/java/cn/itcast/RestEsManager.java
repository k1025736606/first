package cn.itcast;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;

/**
 * @创建人 kzp
 * @创建时间
 * @描述
 */

/**
 * 准备客户端
 */
public class RestEsManager {

    private TransportClient client = null;

    /**
     * 初始化客户端，此代码是官网提供
     * @throws Exception
     */
    @Before
    public void init() throws Exception{
        // on startup
        client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));

    }

    /**
     * 客户端关闭
     */
    @After
    public void close(){
        // on shutdown
        client.close();
    }

    /*
    "hits": {
    "total": 8,
    "max_score": 1,
    "hits": [
      {
        "_index": "car",
        "_type": "orders",
        "_id": "O_DRn2wBpxqxn0O9WEcG",
        "_score": 1,
        "_source": {
          "price": 30000,
          "color": "绿",
          "make": "福特",
          "sold": "2014-05-18"
        }
      },

    */
    @Test
    public  void serach(){
        SearchResponse response = client.prepareSearch("heima")     //指定索引库
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)                //官网固定
//                .setQuery(QueryBuilders.termQuery("goodsName", "手机"))                 // Query设定查询方式   term
//                .setQuery(QueryBuilders.matchAllQuery())              // Query设定查询方式   term

                //                .setQuery(QueryBuilders.matchQuery("goodsName","小米手机"))// Query设置查询方式-分词查询
//                .setQuery(QueryBuilders.wildcardQuery("goodsName","*手*"))// Query设置查询方式-模糊查询
//                .setQuery(QueryBuilders.fuzzyQuery("goodsName", "大米").fuzziness(Fuzziness.ONE))// Query设置查询方式-容错查询

//                .setQuery(QueryBuilders.rangeQuery("price").gte(1000).lte(5000))// Query设置查询方式-区间查询
                .setQuery(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("goodsName", "手机"))
                        .mustNot(QueryBuilders.rangeQuery("price").gte(1000).lte(5000))) //组合查询


                .setPostFilter(QueryBuilders.rangeQuery("price").from(1000).to(6000))     // Filter 过滤查询
                .setFrom(0).setSize(60).setExplain(true)        //分页
                .get();     //执行查询

        //获取第一个 hits 数据
        SearchHits searchHits = response.getHits();
        // 获取总条数
        System.out.println("查询总条数 : "+ searchHits.getTotalHits());
        //获取第二个 hits 数据
        SearchHit[] hits = searchHits.getHits();
        //遍历  获取具体内容
        for (SearchHit hit : hits) {
            //  获取 _source 中的数据对象
            String source = hit.getSourceAsString();
            System.out.println(source);

        }

    }


}
