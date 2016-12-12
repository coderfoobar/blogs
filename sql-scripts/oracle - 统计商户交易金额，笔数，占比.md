```sql

select merchant.corporation_name as 商户名称,merchant.nick_name as 商户昵称,stat.* from
 ( select cust.key_id,cust.customer_no,corp.corporation_name,corp.nick_name from center.pf_customer cust
 left join center.pf_corporation_info corp
 on cust.key_id = corp.customer_id) merchant
 ,
 (
select A.merchantId as 商户号,
       sum(nvl(A.AMOUNT, 0)) as 总交易金额,
       count(1) as 总交易笔数,
       sum(case
             when A.CARDTYPE = '0' then
              1
             else
              0
           end) as 借记卡笔数,
       to_char(round(sum(case
                           when A.CARDTYPE = '0' then
                            1
                           else
                            0
                         end) * 100 / count(1),
                     2),
               'FM9990.00') || '%' as 借记卡占比,
       sum(case
             when A.Amount <= 100000 then
              1
             else
              0
           end) as 小额交易笔数,
       to_char(round((sum(case
                            when A.Amount <= 100000 then
                             1
                            else
                             0
                          end)) * 100 / count(1),
                     2),
               'FM9990.00') || '%' as 小额交易笔数占比,
       sum(case
             when A.Amount <= 100000 then
              A.Amount
             else
              0
           end) as 小额交易金额,

       to_char(round((sum(case
                            when A.Amount <= 100000 then
                             A.Amount
                            else
                             0
                          end)) * 100 / sum(nvl(A.AMOUNT, 0)),
                     2),
               'FM9990.00') || '%' as 小额交易金额占比,

       sum(case
             when A.tradehourtime >= 1 and A.tradehourtime < 6 then
              1
             else
              0
           end) as 非常规时间交易笔数,

       to_char(round(sum(case
                           when A.tradehourtime >= 1 and A.tradehourtime < 6 then
                            1
                           else
                            0
                         end) * 100 / count(1),
                     2),
               'FM9990.00') || '%' as 非常规时间交易笔数占比,

       sum(case
             when A.tradehourtime >= 1 and A.tradehourtime < 6 then
              A.Amount
             else
              0
           end) as 非常规时间交易金额,
       to_char(round(sum(case
                           when A.tradehourtime >= 1 and A.tradehourtime < 6 then
                            A.Amount
                           else
                            0
                         end) * 100 / sum(nvl(A.AMOUNT, 0)),
                     2),
               'FM9990.00') || '%' as 非常规时间交易金额占比,

       sum(case
             when mod(A.Amount / 100, 10) = 0 then
              1
             else
              0
           end) as 十的整数倍数交易笔数,

       to_char(round(sum(case
                           when mod(A.Amount / 100, 10) = 0 then
                            1
                           else
                            0
                         end) * 100 / count(1),
                     2),
               'FM9990.00') || '%' as 十的整数倍数交易占比,

       sum(case
             when mod(A.Amount / 100, 1000) = 0 then
              1
             else
              0
           end) as 千的整数倍数交易笔数,

       to_char(round(sum(case
                           when mod(A.Amount / 100, 1000) = 0 then
                            1
                           else
                            0
                         end) * 100 / count(1),
                     2),
               'FM9990.00') || '%' as 千的整数倍数交易占比,

       sum(case
             when mod(A.Amount / 100, 10000) = 0 then
              1
             else
              0
           end) as 万的整数倍数交易笔数,

       to_char(round(sum(case
                           when mod(A.Amount / 100, 10000) = 0 then
                            1
                           else
                            0
                         end) * 100 / count(1),
                     2),
               'FM9990.00') || '%' as 万的整数倍数交易占比

  from (select t.amount as amount,
               t.trxsts as status,
               t.paymode as cardType,
               o.merchant_id as merchantId,
               to_number(substr(to_char(t.create_date_time,
                                        'yyyy-mm-dd hh24:mi:ss'),
                                12,
                                2),
                         99) as tradeHourTime
          from cashier.gw_trxs t
          left join cashier.gw_orders o
            on o.order_id = t.order_id
         where t.trxsts = '1'
           and o.product_id in ('101010', '101020', '101030')
           and (t.create_date_time >=
               to_date('2016-09-01 00:00:00', 'yyyy-mm-dd hh24:mi:ss') and
               t.create_date_time <
               to_date('2016-12-08 00:00:00', 'yyyy-mm-dd hh24:mi:ss'))) A
 group by A.merchantId
 ) stat
 where merchant.customer_no = stat.商户号

```