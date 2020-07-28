package com.blanc.event.sharding;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * 强制路由算法表达式
 *
 * @author blanc
 */
@Component
public class HintTableExpression {

    private static final String EXPRESSION_PRE_FIX = "HINT_";

    private static final String EXPRESSION_SUB_FIX = "_END";

    /**
     * 正则表达式
     */
    private static final String EXPRESSION_PATTERN = EXPRESSION_PRE_FIX + "[0-9]*" + EXPRESSION_SUB_FIX;


    /**
     * 功能：生成物理表的路由key
     *
     * @param index 路由索引
     * @return 路由表达式
     */
    public String genExpression(int index) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(EXPRESSION_PRE_FIX);
        buffer.append(index);
        buffer.append(EXPRESSION_SUB_FIX);
        return buffer.toString();
    }


    /**
     * 功能：正则表达式判断是否是路由表达式
     *
     * @param expression 路由表达式
     * @return true or false
     */
    public boolean matchExpression(String expression) {
        //判断数据是否为空
        if (expression == null || expression.isEmpty()) {
            throw new NullPointerException("hitExpression is null:" + expression);
        }
        return Pattern.matches(EXPRESSION_PATTERN, expression);
    }


    /**
     * 功能：根据具体内容获取具体的表路由信息
     *
     * @param expression
     * @return
     */
    public int getTableIndex(String expression) {
        if (!StringUtils.isEmpty(expression)) {
            expression = expression.replace(EXPRESSION_PRE_FIX, "");
            expression = expression.replace(EXPRESSION_SUB_FIX, "");
            return Integer.parseInt(expression);
        }
        return 0;
    }

    public static void main(String[] args) {
        HintTableExpression expression = new HintTableExpression();
        for (int i = 0; i < 64; i++) {
            String info = expression.genExpression(i);
            boolean flag = expression.matchExpression(info);
            int index = expression.getTableIndex(info);

            System.out.println(info + " " + flag + " " + index);
        }
    }

}
