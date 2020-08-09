package com.code.collection.formulary.circulationDataFetch;

import com.code.collection.formulary.distrubutedLocked.normalRedis.RedisProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 这里通过任务推送来模拟这种轮询取数据并推送的场景
 * <p>
 * 具体逻辑是得到待推送的任务号，然后依次轮询查询该任务号下的待推送实例，一次取一部分(mybatis倒叙取)，然后放入第三方推送服务进行推送(
 * 如极光推送或自己的服务)。推送完成后将任务状态改成已推送。
 * <p>
 * 之后开另一个定时任务(这里不再写出)，来以当前时间为基点，调用推送服务的接口查询之前一段时间已推送的推送实例是否推送成功，来修改其具体
 * 推送实例的状态(推送中————>已推送)
 * <p>
 * 这里的具体推送实例对应了一个待推送的粉丝
 */

public class TaskPushManager {


    @Autowired
    private RedisProxy redisProxy;

    /**
     * 模拟待推送的任务
     */
    private class PushTask {

    }

    /**
     * 模拟具体推送任务下的待推送实例
     */
    private class Fans {

    }


    /**
     * 模拟推送任务和待推送实例的中间表对应的对象
     */
    private class PushTaskToFans {
        private Long id;

        public Long getId() {
            return id;
        }

        public PushTaskToFans setId(Long id) {
            this.id = id;
            return this;
        }
    }


    /**
     * 每隔6min检查一次数据库，找出state为prepared 的任务（无论是定时任务还是立即推送任务，也不管具体任务是哪个公司的），都发送！！！
     * 通过在Manager中的SpringTask 轮询搜集数据，然后调用service的方法进行对数据库的操作，这样既保证了service层的事务，也避免直接将springTask放入service层导致事务过大。
     */
    @Scheduled(cron = "* 0/6 * * * ?")
    public void checkSchedule() {

        int page = 1;
        int rows = 20;
        while (true) {
            /*得到待发送任务的集合,该集合一次只取了一定数量（现为20）的任务状态为prepared 出来*/
            /*
             * 这里在实际项目中，可以通过一定参数来得到待推送的任务集合(每个任务集合中才是具体的需要推送的推送实例)
             * 这里直接写死
             */
            List<PushTask> pushableList = new ArrayList<>();


            /*集合为空，跳出第一层任务的循环*/
            if (CollectionUtils.isEmpty(pushableList)) {
                break;
            }

            pushableList.forEach(p -> {
                /*使用任务id 得到redis中锁的键的唯一标识*/
                /*
                 * 这里
                 */
                String key = "pushTaskLock";
                /*先判断是否上锁,对一个任务的上锁时间为15min*/
                if (redisProxy.lock(key, 900L)) {
                    try {
                        /*循环单个任务下的粉丝数，收集粉丝集合并发送*/
                        Long maxId = Long.MAX_VALUE;
                        while (true) {
                        /*每一个任务，一次取出980个粉丝，进行推送（极光限定了一次推送的数量）；
                        先不考虑推送中断的事，中断下次重新推送。*/
                            /*这里分页取推送实例对应的id，一次取一部分出来。这里模拟就写死得到的集合。*/
                            List<PushTaskToFans> fteList = new ArrayList<>();
                            /*如果集合为空，则跳出该任务循环，查找下一个任务.*/
                            if (CollectionUtils.isEmpty(fteList)) {
                                break;
                            } else {
                                /*这里需要说明下，因为后台的mysql在轮询取数据的时候，是从最后开始取得,所以这里通过这种方法来取得当前
                                 * 已经取了得数据得最小id，下次或其他分布式主机该段程序就接着这个id来取*/
                                /*SELECT
                                   id,
                                   device_id
                                FROM
                                   fans_to_enterprise fe
                                WHERE
                                   fe.subscribe = #{subscribe}
                                AND
                                   fe.fk_enterprise_id = #{enterpriseId}
                                AND
                                   id &lt; #{maxId}
                                ORDER BY id  DESC
                                LIMIT 0, ${rows}*/
                                maxId = fteList.get(fteList.size() - 1).getId();


                                /*这里是具体得推送，可能需要调用第三方得一些api*/
                                System.out.println("调用接口进行推送");
                            }
                        }

                        /*对于一个任务，上述对该任务下的粉丝数进行全部推送且没报错后，将该任务的状态改变为finished*/
                        System.out.println("其中一个pushtask完成");
                    } finally {
                        /*成功处理完一个任务并将任务的状态变为finished之后，释放锁*/
                        redisProxy.del(key);
                    }
                }
            });
        }
    }
}
