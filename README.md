# springboot-elasticsearch
elasticsearch 分页查询，高亮查询 增删改查

添加Order对象
请求类型 POST  application/json
请求地址： http://localhost:8771/order/add/2

请求参数：
```json

{
    "storeId": 0,
    "storeName": "中国",
    "categoryId": 1,
    "categoryCode": "中国国歌",
    "productCode": null,
    "quantity": 0,
    "amount": 26.6,
    "payDate": "2020-11-28T14:03:41.010+00:00"
}

```


返回对象：
```json
{
    "code": 200,
    "message": null,
    "data": null
}
```


查询Order
请求类型 GET  
请求地址： http://localhost:8771/order/pageQuery?page=0&pageSize=2&storeName=&categoryCode=英国

返回对象：
```json
{
    "code": 200,
    "message": null,
    "data": {
        "content": [
            {
                "id": -737239183,
                "storeId": 271030570,
                "storeName": "<span style=\"color:red\">英国</span>宪法",
                "categoryId": 0,
                "categoryCode": "<span style=\"color:red\">英国</span>宪法是<span style=\"color:red\">英国</span>人的法律总纲领，保护的是<span style=\"color:red\">英国</span>公民，一切法律都不得违背宪法",
                "productCode": null,
                "quantity": -2017040387,
                "amount": 0.7617829730470107,
                "payDate": "2020-11-29T14:22:03.737+00:00"
            },
            {
                "id": -499400107,
                "storeId": -446734485,
                "storeName": "<span style=\"color:red\">英国</span>宪法",
                "categoryId": 0,
                "categoryCode": "<span style=\"color:red\">英国</span>宪法是<span style=\"color:red\">英国</span>人的法律总纲领，保护的是<span style=\"color:red\">英国</span>公民，一切法律都不得违背宪法",
                "productCode": null,
                "quantity": -1002469339,
                "amount": 0.43038714818311385,
                "payDate": "2020-11-29T14:49:50.147+00:00"
            }
        ],
        "pageable": {
            "sort": {
                "sorted": false,
                "unsorted": true,
                "empty": true
            },
            "offset": 0,
            "pageNumber": 0,
            "pageSize": 2,
            "unpaged": false,
            "paged": true
        },
        "totalPages": 1,
        "totalElements": 2,
        "last": true,
        "size": 2,
        "number": 0,
        "sort": {
            "sorted": false,
            "unsorted": true,
            "empty": true
        },
        "numberOfElements": 2,
        "first": true,
        "empty": false
    }
}

```