
# MyBatis 实现union all查询合并多个结果集

> Create Time : 2017年3月16日 Ref : https://my.oschina.net/boonya/blog/690694

以下是mybatis mapper对应的sql语句：
```sql

<select id="getProductEnableNumberInfos" resultMap="productMap" >
    SELECT CDSK_ITEM_CODE ProductCode,WMST_SKU_UNIT ProductUnit,WMST_ENABLED_NUMBER ProductNum from (
        <foreach collection="conditions.list" item="item" index="index" separator="union all">
            SELECT c.CDSK_ITEM_CODE,WMST_SKU_UNIT,SUM(WMST_ENABLED_NUMBER) WMST_ENABLED_NUMBER 
            FROM wm_stock_${conditions.esCorCode} w 
            LEFT JOIN cd_wh_itme_${conditions.esCorCode} c ON w.WMST_SKU_ID=c.CD_ITEM_ID
            WHERE c.CDSK_ITEM_CODE=#{item.ProductCode,jdbcType=VARCHAR}
                AND w.WMST_SKU_UNIT=#{item.ProductUnit,jdbcType=VARCHAR} 
                AND w.WMST_WR_ID=${conditions.wrId} AND w.WMST_CUSTOMER_CODE=#{conditions.customerCode,jdbcType=VARCHAR} 
        </foreach>
    ) alias WHERE alias.WMST_ENABLED_NUMBER>0;
</select>

```
