package com.code.collection.formulary.enumerationRelative;

import java.math.BigDecimal;

/**
 * 这里利用Java的枚举类型，模拟责任链模式，封装为一个静态方法提供给外部。这样外部直接输入
 * 一个值后会直接按照枚举类中的值范围来得到具体对应的值。
 * <p>
 * 这里模拟一个按具体的粉丝人数来得到支付价格的例子。具体会有免费粉丝人数。
 */
public class ChainOfResponsibilityUtils {


    /**
     * 这里使用了一个枚举内部类
     */
    public enum PushRules {

        /**
         * 粉丝数在100000,999999之间
         */
        aHundredThousand(100000, 999999, new BigDecimal(1000)) {
            @Override
            BigDecimal get(Long fansNumber, Long freeFansNumber) {
                if (fansNumber >= this.max && fansNumber < this.min && 0L != freeFansNumber) {
                    return null;
                }
                if (fansNumber - freeFansNumber >= this.min) {
                    return this.amount;
                }
                return BigDecimal.ZERO;
            }
        },
        /**
         * 粉丝数在1000000,9999999之间
         */
        million(1000000, 9999999, new BigDecimal(9800)) {
            @Override
            BigDecimal get(Long fansNumber, Long freeFansNumber) {
                if (fansNumber >= this.max && fansNumber < this.min && 0L != freeFansNumber) {
                    return null;
                }

                if (fansNumber - freeFansNumber >= this.min) {
                    return this.amount;
                }

                return PushRules.aHundredThousand.get(fansNumber - freeFansNumber, 0L);
            }
        },
        /**
         * 粉丝人数在10000000以上
         */
        tenMillion(10000000, Integer.MAX_VALUE, new BigDecimal(Integer.MAX_VALUE)) {
            @Override
            BigDecimal get(Long fansNumber, Long freeFansNumber) {
                /*也就是粉丝人数大于千万，始终都是null，除非其粉丝人数大于了20亿*/
                if (fansNumber >= this.max && fansNumber < this.min) {
                    return null;
                }

                if (fansNumber - freeFansNumber > this.min) {
                    return this.amount;
                }
                return PushRules.million.get(fansNumber - freeFansNumber, 0L);
            }
        };

        /**
         * 人数下限
         */
        protected Integer min;
        /**
         * 人数上限
         */
        protected Integer max;
        /**
         * 具体需要支付的金额
         */
        protected BigDecimal amount;

        PushRules(Integer min, Integer max, BigDecimal amount) {
            this.min = min;
            this.max = max;
            this.amount = amount;
        }

        /**
         * 传入当前粉丝数和免费粉丝数量，得到需支付的价格
         *
         * @param fansNumber     当前粉丝数
         * @param freeFansNumber 免费粉丝数
         * @return 具体需支付的价格
         */
        public static BigDecimal getAmount(Long fansNumber, Long freeFansNumber) {
            BigDecimal amount = new BigDecimal(-1);
            for (PushRules f : PushRules.values()) {
                amount = f.get(fansNumber, freeFansNumber);
                if (amount != null) {
                    return amount;
                }
            }
            return amount;
        }

        abstract BigDecimal get(Long fansNumber, Long freeFansNumber);

    }
}
