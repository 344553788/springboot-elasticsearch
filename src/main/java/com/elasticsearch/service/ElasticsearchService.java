package com.elasticsearch.service;

import com.elasticsearch.entity.ESUser;
import com.elasticsearch.entity.Order;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ElasticsearchService {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    public Page<ESUser> esUserPages(Integer pageNum, Integer pageSize, String desc, List<String> tags, String name) {

        // 先构建查询条件
        BoolQueryBuilder defaultQueryBuilder = QueryBuilders.boolQuery();
        if (!StringUtils.isEmpty(desc)) {
            defaultQueryBuilder.should(QueryBuilders.termQuery("desc", desc));
        }
        if (!StringUtils.isEmpty(name)) {
            defaultQueryBuilder.should(QueryBuilders.termQuery("name", name));
        }
        if (!CollectionUtils.isEmpty(tags)) {
            for (String tag : tags) {
                defaultQueryBuilder.must(QueryBuilders.termQuery("tags", tag));
            }
        }
        // 分页条件
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize);
        // 高亮条件
        HighlightBuilder highlightBuilder = getHighlightBuilder("desc", "tags");
        // 排序条件
        FieldSortBuilder sortBuilder = SortBuilders.fieldSort("age").order(SortOrder.DESC);
        //组装条件
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(defaultQueryBuilder)
                .withHighlightBuilder(highlightBuilder)
                .withPageable(pageRequest)
                .withSort(sortBuilder).build();

        SearchHits<ESUser> searchHits = elasticsearchRestTemplate.search(searchQuery, ESUser.class);
        // 高亮字段映射
        List<ESUser> userList = new ArrayList<>();
        for (SearchHit<ESUser> searchHit : searchHits) {
            ESUser content = searchHit.getContent();
            Map<String, List<String>> highlightFields = searchHit.getHighlightFields();
            for (String highlightField : highlightFields.keySet()) {
                if ("tags".equals(highlightField)) {
                    content.setTags(highlightFields.get(highlightField));
                } else if ("desc".equals(highlightField)) {
                    content.setDesc(highlightFields.get(highlightField).get(0));
                }

            }
            userList.add(content);
        }

        // 组装分页对象
        Page<ESUser> userPage = new PageImpl<>(userList, pageRequest, searchHits.getTotalHits());

        return userPage;

    }

    // 设置高亮字段
    private HighlightBuilder getHighlightBuilder(String... fields) {
        // 高亮条件
        HighlightBuilder highlightBuilder = new HighlightBuilder(); //生成高亮查询器
        for (String field : fields) {
            highlightBuilder.field(field);//高亮查询字段
        }
        highlightBuilder.requireFieldMatch(false);     //如果要多个字段高亮,这项要为false
        highlightBuilder.preTags("<span style=\"color:red\">");   //高亮设置
        highlightBuilder.postTags("</span>");
        //下面这两项,如果你要高亮如文字内容等有很多字的字段,必须配置,不然会导致高亮不全,文章内容缺失等
        highlightBuilder.fragmentSize(800000); //最大高亮分片数
        highlightBuilder.numOfFragments(0); //从第一个分片获取高亮片段

        return highlightBuilder;
    }


    public Page<Order> orderPages(String categoryCode, String storeName, Integer pageNum, Integer pageSize) {

        // 分页条件
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize);
        //条件搜索
        // 先构建查询条件
        BoolQueryBuilder defaultQueryBuilder = QueryBuilders.boolQuery();
        if (!StringUtils.isEmpty(categoryCode)) {
            defaultQueryBuilder.should(QueryBuilders.termQuery("categoryCode", categoryCode));
        }
        if (!StringUtils.isEmpty(storeName)) {
            defaultQueryBuilder.should(QueryBuilders.termQuery("storeName", storeName));
        }

        // 高亮显示
        HighlightBuilder highlightBuilder = getHighlightBuilder("categoryCode", "storeName");
        // 排序条件
        // FieldSortBuilder sortBuilder = SortBuilders.fieldSort("age").order(SortOrder.DESC);
        //组装条件
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(defaultQueryBuilder)
                .withHighlightBuilder(highlightBuilder)
                .withPageable(pageRequest).build();

        SearchHits<Order> searchHits = elasticsearchRestTemplate.search(searchQuery, Order.class);
        // 高亮字段映射
        List<Order> orderList = new ArrayList<>();
        for (SearchHit<Order> searchHit : searchHits) {
            Order order = searchHit.getContent();
            Map<String, List<String>> highlightFields = searchHit.getHighlightFields();
            for (String highlightField : highlightFields.keySet()) {
                if ("categoryCode".equals(highlightField)) {
                    order.setCategoryCode(highlightFields.get(highlightField).get(0));
                }
                if ("storeName".equals(highlightField)) {
                    order.setStoreName(highlightFields.get(highlightField).get(0));
                }

            }
            orderList.add(order);
        }
        // 组装分页对象
        Page<Order> orderPage = new PageImpl<>(orderList, pageRequest, searchHits.getTotalHits());

        // 组装分页对象
        return orderPage;
    }

}
