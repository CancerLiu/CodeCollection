package com.code.collection.java.collectionAndStreamAndLambdaCode;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamTest {

    private final static Logger logger = LoggerFactory.getLogger(StreamTest.class);

    /**
     * 用于集成stream流创建的一些api
     */
    private static void createStream() {
        //按规则生成
        Stream.iterate(2, (s) -> s + 2);
        Stream.generate(() -> new Random().nextInt());

        //类似建造者模式
        IntStream.Builder streamBuilder = IntStream.builder();
        streamBuilder.add(1);
        IntStream stream = streamBuilder.build();

        //从数组构造
        Stream.of(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
        Stream.of(1, 2, 3, 8);
        Arrays.stream(new int[]{1, 2, 34, 5});

        //从Collection系统
        List<Integer> intLists = Lists.newArrayList(1, 2, 3, 4);
        Stream oneStream = intLists.stream();

        //创建一个空流
        Stream twoStream = Stream.empty();

        //拼接流
        Stream threeStream = Stream.concat(oneStream, twoStream);
    }

    /**
     * 用于集成一些常用的stream中间方法
     */
    private static void middleStream() {
        //map   flatMap   peek    limit   skip   distinct   sorted

        List<String> stringList = Lists.newArrayList("liu", "chao", "stream", "learn");
        List<Integer> intList = Lists.newArrayList(1, 5, 45, 6, 22, 22);
        List<String> strFlatMapList = Lists.newArrayList("好,好,学", "习,天,天", "向,上");


        //map
        System.out.println("map之后" + stringList.stream().map(String::length).collect(Collectors.toList()));

        //peek  因为peek无返回值，所以其不会返回的stream和之前一样
        System.out.println("peek之后" + stringList.stream().peek(String::length).collect(Collectors.toList()));
        stringList.stream().peek(System.out::println);

        //filter
        System.out.println("filter之后" + intList.stream().filter(s -> s < 40).collect(Collectors.toList()));

        //skip and  limit
        System.out.println("skip之后" + intList.stream().skip(3).collect(Collectors.toList()));
        System.out.println("limit之后" + intList.stream().limit(3).collect(Collectors.toList()));

        //distinct
        System.out.println("distinct之后" + intList.stream().distinct().collect(Collectors.toList()));

        //sorted
        System.out.println("sorted之后(默认排序)" + intList.stream().sorted().collect(Collectors.toList()));
        System.out.println("sorted之后(外部比较器啊)" + intList.stream().sorted(Comparator.reverseOrder()));

        //flatMap
        System.out.println("flatMap之后" + strFlatMapList.stream().map(s -> s.split(",")).flatMap(Arrays::stream).collect(Collectors.toList()));
    }

    /**
     * 末端方法
     */
    private static void terminalStream() {
        /**
         *   count   max   min   findFirst   findLast  findAny   anyMatch   allMatch   noneMatch   forEach
         *   forEachOrder   reduce
         */
        List<String> stringList = Lists.newArrayList("liu", "chao", "stream", "learn");
        List<Integer> intList = Lists.newArrayList(1, 5, 45, 6, 22, 22, 2, 4, 56, 16, 5, 81, 35);

        //count
        System.out.println("count之后" + intList.stream().count());
        ;

        //max and min   这个和sorted不同，均需要传入比较器
        System.out.println("max之后" + intList.stream().max(Comparator.naturalOrder()));
        System.out.println("min之后" + intList.stream().max(Comparator.naturalOrder()));

        //findFirst and findAny
        System.out.println("findFirst之后" + intList.stream().findFirst().get());
        System.out.println("findAny之后" + intList.stream().findAny().get());

        //XxxMatch
        System.out.println("anyMatch之后" + stringList.stream().anyMatch(s -> s.length() == 5));
        System.out.println("allMatch之后" + stringList.stream().allMatch(s -> s.length() == 5));
        System.out.println("noneMatch之后" + stringList.stream().noneMatch(s -> s.length() == 8));

        //forEach   和peek很像  不过一个是末端方法一个是中间方法
        stringList.stream().forEach(System.out::println);
        stringList.stream().forEachOrdered(System.out::println);

        //◉ reduce 蛮重要的一个方法，一共有三个重载形式
        //形式1 o1是上次o1+o2的执行结果，o2是stream流中的下一个元素.返回的是Optional包装类。
        System.out.println("reduce求和(单参数)" + intList.stream().reduce((o1, o2) -> o1 + o2).get());

        //形式2 和单参数的区别不大，给出的参数作为首次参数使用
        System.out.println("reduce求和(双参数)" + intList.stream().reduce(10, (o1, o2) -> o1 + o2));

        //形式3  具体
        System.out.println(intList.stream().reduce(10, (o1, o2) -> o1 + o2, (o1, o2) -> o1 + o2));
    }

    /**
     * 末端收集器的demo
     */
    private static void terminalCollectionStream() {
        /**
         * summarizingXxx  maxBy/minBy   averagingXxx    summaryingXxx     counting   partitioningBy   groupingBy
         * toSet   toMap   toList   toCollection   joining    mapping   collectingAndThen    reducing
         */
        List<Integer> intList = Lists.newArrayList(1, 5, 45, 6, 22, 22, 2, 4, 56, 16, 5, 81, 35);
        List<Employee> employeeList = Lists.newArrayList(
                new Employee(10, "liuchao1"),
                new Employee(20, "liuchao1"),
                new Employee(30, "liuchao2"),
                new Employee(40, "liuchao2"),
                new Employee(50, "liuchao5"));

        //summarizingXxx
        IntSummaryStatistics statistics = intList.stream().collect(Collectors.summarizingInt(Integer::intValue));
        System.out.println("最大值" + statistics.getMax() + "    最小值" + statistics.getMin() + "    平均值" + statistics.getAverage()
                + "      数量" + statistics.getCount() + "     总和" + statistics.getSum());

        //maxBy/minBy
        System.out.println("最大值:" + intList.stream().collect(Collectors.maxBy(Comparator.naturalOrder())).get());
        System.out.println("最小值:" + intList.stream().collect(Collectors.minBy(Comparator.naturalOrder())).get());

        // averagingXxx    summaryingXxx     counting
        System.out.println("平均值:" + intList.stream().collect(Collectors.averagingInt(Integer::intValue)));
        System.out.println("总和:" + intList.stream().collect(Collectors.summingInt(Integer::intValue)));
        System.out.println("元素数量:" + intList.stream().collect(Collectors.counting()));

        //partitioningBy   groupingBy
        Map<Boolean, List<Integer>> intMap = intList.stream().collect(Collectors.partitioningBy(s -> s > 22));
        System.out.println("partitioningBy之后:" + intMap);

        Map<String, List<Employee>> employeeListSingleParam = employeeList.stream().collect(Collectors.groupingBy(Employee::getName));
        System.out.println("groupingBy之后(单参数)" + employeeListSingleParam);

        Map<String, Double> employeeListDoubleParam = employeeList.stream()
                .collect(Collectors.groupingBy(Employee::getName, Collectors.averagingInt(Employee::getSalary)));
        System.out.println("groupingBy之后(双参数)" + employeeListDoubleParam);

        //此处第二个参数操作的对象应该是Map<String,List<Employee>>。而stream中貌似操作map的方法不多
        Map<String, Double> employeeListTrippleParam = employeeList.stream()
                .collect(Collectors.groupingBy(Employee::getName, () -> new TreeMap<String, Double>(), Collectors.averagingInt(Employee::getSalary)));
        System.out.println("groupingBy之后(三参数)" + employeeListTrippleParam);

        //toSet  toList都不做过多的整理，这里主要就toMap进行
        //最基本的两个参数
        Map<Integer, Employee> employeeMapOne = employeeList.stream().collect(Collectors.toMap(Employee::getSalary, Function.identity()));
        System.out.println("toMap之后(基本的两参数)" + employeeMapOne);

        //第三个参数解决多个元素有相同键的情况，自己写逻辑进行取舍(这里就是取最新的值放入键)
        Map<Integer, Employee> employeeMapTwo = employeeList.stream().collect(Collectors.toMap(Employee::getSalary, Function.identity(), (o1, o2) -> o2));
        System.out.println("toMap之后(三参数)" + employeeMapTwo);

        //还可以有第四个参数，即包装


        //joining
        String employeeName = employeeList.stream().map(s -> s.getName()).collect(Collectors.joining(",", "{", "}"));
        System.out.println("joining之后:" + employeeName);

        //mapping
        List<String> nameList = employeeList.stream().collect(Collectors.mapping(s -> s.getName(), Collectors.toList()));
        System.out.println("mapping之后:" + nameList);

        //collectingAndThen  这里collectingAndThen第二个参数逻辑处理的对象是第一个参数的结果
        Integer listSize = employeeList.stream().collect(Collectors.collectingAndThen(Collectors.toList(), s -> s.size()));
        System.out.println("collectingAndThen之后:"+listSize);

        //reducing方法和terminal方法的reduce差不多，这里不再整理

        //其他
//        (1)e.g.1
//        Map<String, List<String>> collect = conditions.stream()
//                .collect(Collectors.groupingBy(Condition::getCondName,
//                        Collectors.mapping(Condition::getCondValue,
//                                Collectors.collectingAndThen(Collectors.toList(), lists -> lists.stream().flatMap(List::stream).collect(Collectors.toList())))));
//
//        (2)e.g.2
//        Map<String, List<String>> collect2 = conditions.stream()
//                .collect(Collectors.groupingBy(Condition::getCondName,
//                        Collectors.mapping(Condition::getCondValue,
//                                Collectors.reducing(new ArrayList<>(), (l1, l2) -> Stream.concat(l1.stream(), l2.stream()).collect(Collectors.toList())))));
//
//        (3)e.g.3
//        Map<String, List<String>> collect1 = conditions.stream().collect(
//                Collectors.toMap(Condition::getCondName, Condition::getCondValue, (c1, c2) -> Stream.concat(c1.stream(), c2.stream()).collect(Collectors.toList())));
    }


    public static void main(String[] args) {
//        StreamTest.createStream();
//        StreamTest.middleStream();
//        StreamTest.terminalStream();
        StreamTest.terminalCollectionStream();
    }
}
